package pl.hellopoland.rest.dto;

public class TechnicalRecoveryAgentIRO {

  public String requestId;
  public String actor;
  public String reason;

  public TechnicalRecoveryAgentIRO() {}

  public TechnicalRecoveryAgentIRO(String requestId, String actor, String reason) {
    this.requestId = requestId;
    this.actor = actor;
    this.reason = reason;
  }
}
