package pl.hellopoland.soap.p24.object;

import javax.xml.bind.annotation.XmlAnyElement;

public class MerchantRegisterResult {
  @XmlAnyElement(lax = true)
  public Object result;
  public GeneralError error;

}
