package pl.hellopoland.rest.dto;

import pl.hellopoland.enums.PromotionCodeType;

import java.util.ArrayList;
import java.util.List;

public class PromotionCodeSetupIRO {

  public String fileName;
  public List<String> codes = new ArrayList<>();
  public String fixedCode;
  public Integer generateCount;
  public Integer generatedCodeLength;
  public String generatedCodePrefix;
  public String generatedCodeSeparator;
  public PromotionCodeType codeType;
  public Integer maxRedemptions;
  public Integer maxRedemptionsPerCustomer;
  public Integer maxRedemptionsPerDay;
}
