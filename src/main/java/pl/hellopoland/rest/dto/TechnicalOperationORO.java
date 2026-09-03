package pl.hellopoland.rest.dto;

import pl.hellopoland.bo.TechnicalOperation;
import java.time.format.DateTimeFormatter;

public class TechnicalOperationORO {

  public String requestId;
  public String actor;
  public String reason;
  public String operation;
  public String status;
  public String requestedAt;
  public String startedAt;
  public String finishedAt;
  public String resultMessage;

  public TechnicalOperationORO(TechnicalOperation operation) {
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    this.requestId = operation.getRequestId();
    this.actor = operation.getActor();
    this.reason = operation.getReason();
    this.operation = operation.getOperation();
    this.status = operation.getStatus().name();
    this.requestedAt = operation.getRequestedAt().format(formatter);
    this.startedAt = operation.getStartedAt() == null ? null : operation.getStartedAt().format(formatter);
    this.finishedAt = operation.getFinishedAt() == null ? null : operation.getFinishedAt().format(formatter);
    this.resultMessage = operation.getResultMessage();
  }
}
