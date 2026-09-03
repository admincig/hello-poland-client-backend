package pl.hellopoland.rest.dto;

public class TechnicalCheckORO {

  public boolean ok;
  public String status;
  public String message;
  public Long responseTimeMs;

  public static TechnicalCheckORO ok(String message, long responseTimeMs) {
    TechnicalCheckORO result = new TechnicalCheckORO();
    result.ok = true;
    result.status = "OK";
    result.message = message;
    result.responseTimeMs = responseTimeMs;
    return result;
  }

  public static TechnicalCheckORO failed(String message, long responseTimeMs) {
    TechnicalCheckORO result = new TechnicalCheckORO();
    result.ok = false;
    result.status = "FAILED";
    result.message = message;
    result.responseTimeMs = responseTimeMs;
    return result;
  }
}
