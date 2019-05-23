package pl.hellopoland.soap.p24.object;

import javax.xml.bind.annotation.XmlAnyElement;

public class MerchantRegisterResult {
  @XmlAnyElement(lax = true)
  // @XmlMixed
  public Object result;
  // public Object[] result;
  public GeneralError error;

}
