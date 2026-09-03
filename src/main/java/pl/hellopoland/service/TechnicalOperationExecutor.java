package pl.hellopoland.service;

import pl.hellopoland.bo.TechnicalOperation.Status;
import pl.hellopoland.rest.dto.TechnicalRecoveryAgentORO;

import jakarta.ejb.Asynchronous;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class TechnicalOperationExecutor {

  @EJB
  private TechnicalOperationAuditService auditService;

  @EJB
  private TechnicalOpsAgentClient agentClient;

  @Asynchronous
  public void recoverWordpress(Long operationId, String requestId, String actor, String reason) {
    auditService.markRunning(operationId);
    try {
      TechnicalRecoveryAgentORO result = agentClient.recoverWordpress(requestId, actor, reason);
      Status status = "SKIPPED".equalsIgnoreCase(result.outcome)
          ? Status.SKIPPED
          : Status.SUCCEEDED;
      auditService.markFinished(operationId, status, result.message);
    } catch (Exception e) {
      auditService.markFinished(operationId, Status.FAILED,
          "Operacja nie powiodła się: " + e.getMessage());
    }
  }
}
