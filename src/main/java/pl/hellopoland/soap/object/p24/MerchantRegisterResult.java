package pl.hellopoland.soap.object.p24;

import java.io.Serializable;

public class MerchantRegisterResult implements Serializable {
  private static final long serialVersionUID = 3678448778619637681L;

  private Object[] result;
  private GeneralError error;

  public Object[] getResult() {
    return result;
  }

  public void setResult(Object[] result) {
    this.result = result;
  }

  public GeneralError getError() {
    return error;
  }

  public void setError(GeneralError error) {
    this.error = error;
  }

}
