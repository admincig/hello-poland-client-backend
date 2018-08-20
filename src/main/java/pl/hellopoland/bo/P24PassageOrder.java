package pl.hellopoland.bo;

public class P24PassageOrder {
  private boolean isSandbox;
  private P24PassageTransactionParams transactionParams;

  public boolean isSandbox() {
    return isSandbox;
  }

  public void setSandbox(boolean isSandbox) {
    this.isSandbox = isSandbox;
  }

  public P24PassageTransactionParams getTransactionParams() {
    return transactionParams;
  }

  public void setTransactionParams(P24PassageTransactionParams transactionParams) {
    this.transactionParams = transactionParams;
  }
}
