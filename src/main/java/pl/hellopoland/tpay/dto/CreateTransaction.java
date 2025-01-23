package pl.hellopoland.tpay.dto;

import java.math.BigDecimal;

public class CreateTransaction {

  public String description;
  public String hiddenDescription;
  public BigDecimal amount;
  public Payer payer;
  public Callbacks callbacks;
}
