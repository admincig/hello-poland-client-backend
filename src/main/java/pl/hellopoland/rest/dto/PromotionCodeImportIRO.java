package pl.hellopoland.rest.dto;

import pl.hellopoland.enums.PromotionCodeType;

import java.util.ArrayList;
import java.util.List;

public class PromotionCodeImportIRO {

  public String fileName;
  public PromotionCodeType codeType;
  public Integer maxRedemptions;
  public Integer maxRedemptionsPerCustomer;
  public Integer maxRedemptionsPerDay;
  public List<String> codes = new ArrayList<>();
}
