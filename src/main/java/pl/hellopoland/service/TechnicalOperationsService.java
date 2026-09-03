package pl.hellopoland.service;

import pl.hellopoland.bo.TechnicalOperation;
import pl.hellopoland.rest.dto.TechnicalCheckORO;
import pl.hellopoland.rest.dto.TechnicalLogsORO;
import pl.hellopoland.rest.dto.TechnicalOperationORO;
import pl.hellopoland.rest.dto.TechnicalStatusORO;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ServiceUnavailableException;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Stateless
@RolesAllowed("helpdesk_technical")
public class TechnicalOperationsService extends ServiceSuperclass {

  private static final Set<String> LOG_SOURCES = Set.of("wordpress", "apache", "docker");

  @EJB
  private TechnicalOpsAgentClient agentClient;

  @EJB
  private TechnicalOperationAuditService auditService;

  @EJB
  private TechnicalOperationExecutor executor;

  public TechnicalStatusORO getStatus() {
    long databaseStarted = System.nanoTime();
    TechnicalCheckORO databaseStatus;
    try {
      em.createNativeQuery("select 1").getSingleResult();
      databaseStatus = TechnicalCheckORO.ok("Baza HP odpowiada.", elapsedMs(databaseStarted));
    } catch (Exception e) {
      databaseStatus = TechnicalCheckORO.failed("Baza HP nie odpowiada.", elapsedMs(databaseStarted));
    }

    TechnicalStatusORO status;
    if (!agentClient.isConfigured()) {
      status = new TechnicalStatusORO();
      status.checkedAt = Instant.now().toString();
      status.agentConfigured = false;
      status.agentMessage = "Agent operacyjny nie jest skonfigurowany.";
    } else {
      try {
        status = agentClient.getStatus();
        status.agentConfigured = true;
      } catch (Exception e) {
        status = new TechnicalStatusORO();
        status.checkedAt = Instant.now().toString();
        status.agentConfigured = true;
        status.agentMessage = "Agent operacyjny jest niedostępny: " + safeMessage(e);
      }
    }
    status.hpDatabase = databaseStatus;
    return status;
  }

  public TechnicalLogsORO getLogs(String requestedSource) {
    String source = requestedSource == null ? "" : requestedSource.toLowerCase(Locale.ROOT);
    if (!LOG_SOURCES.contains(source)) {
      throw new BadRequestException("Nieobsługiwane źródło logów.");
    }
    requireAgent();
    try {
      return agentClient.getLogs(source);
    } catch (Exception e) {
      throw new ServiceUnavailableException("Nie można pobrać logów: " + safeMessage(e));
    }
  }

  public TechnicalOperationORO requestWordpressRecovery(String reason) {
    String normalizedReason = reason == null ? "" : reason.trim();
    if (normalizedReason.length() < 5 || normalizedReason.length() > 250) {
      throw new BadRequestException("Powód musi mieć od 5 do 250 znaków.");
    }
    requireAgent();
    String actor = ctx.getCallerPrincipal().getName();
    TechnicalOperation operation = auditService.createRecovery(actor, normalizedReason);
    executor.recoverWordpress(operation.getId(), operation.getRequestId(), actor, normalizedReason);
    return new TechnicalOperationORO(operation);
  }

  public TechnicalOperationORO getOperation(String requestId) {
    return new TechnicalOperationORO(auditService.findByRequestId(requestId));
  }

  public List<TechnicalOperationORO> getRecentOperations() {
    return auditService.recent().stream().map(TechnicalOperationORO::new).toList();
  }

  private void requireAgent() {
    if (!agentClient.isConfigured()) {
      throw new ServiceUnavailableException("Agent operacyjny nie jest skonfigurowany.");
    }
  }

  private long elapsedMs(long started) {
    return Math.max(0, (System.nanoTime() - started) / 1_000_000);
  }

  private String safeMessage(Exception e) {
    String message = e.getMessage();
    if (message == null || message.isBlank()) {
      return "brak szczegółów";
    }
    return message.length() <= 300 ? message : message.substring(0, 300);
  }
}
