package pl.hellopoland.service;

import pl.hellopoland.bo.TechnicalOperation;
import pl.hellopoland.bo.TechnicalOperation.Status;
import pl.hellopoland.exception.conflict.ConflictingException;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class TechnicalOperationAuditService extends ServiceSuperclass {

  private static final String RECOVERY_OPERATION = "WORDPRESS_RECOVERY";
  private static final int COOLDOWN_MINUTES = 5;

  public TechnicalOperation createRecovery(String actor, String reason) {
    Long recentCount = em.createQuery(
            "select count(o) from TechnicalOperation o "
                + "where o.operation = :operation and o.requestedAt >= :since",
            Long.class)
        .setParameter("operation", RECOVERY_OPERATION)
        .setParameter("since", LocalDateTime.now().minusMinutes(COOLDOWN_MINUTES))
        .getSingleResult();
    if (recentCount > 0) {
      throw new ConflictingException(
          "Operację odzyskiwania WordPressa można uruchomić najwyżej raz na 5 minut.");
    }

    TechnicalOperation operation = new TechnicalOperation();
    operation.setRequestId(UUID.randomUUID().toString());
    operation.setActor(actor);
    operation.setReason(reason);
    operation.setOperation(RECOVERY_OPERATION);
    operation.setStatus(Status.REQUESTED);
    operation.setRequestedAt(LocalDateTime.now());
    em.persist(operation);
    em.flush();
    return operation;
  }

  public TechnicalOperation markRunning(Long id) {
    TechnicalOperation operation = em.find(TechnicalOperation.class, id);
    operation.setStatus(Status.RUNNING);
    operation.setStartedAt(LocalDateTime.now());
    return operation;
  }

  public void markFinished(Long id, Status status, String message) {
    TechnicalOperation operation = em.find(TechnicalOperation.class, id);
    operation.setStatus(status);
    operation.setFinishedAt(LocalDateTime.now());
    operation.setResultMessage(limit(message));
  }

  public TechnicalOperation findByRequestId(String requestId) {
    return em.createQuery("from TechnicalOperation where requestId = :requestId",
            TechnicalOperation.class)
        .setParameter("requestId", requestId)
        .getSingleResult();
  }

  public List<TechnicalOperation> recent() {
    return em.createQuery("from TechnicalOperation order by requestedAt desc",
            TechnicalOperation.class)
        .setMaxResults(20)
        .getResultList();
  }

  private String limit(String value) {
    if (value == null) {
      return null;
    }
    return value.length() <= 2000 ? value : value.substring(0, 2000);
  }
}
