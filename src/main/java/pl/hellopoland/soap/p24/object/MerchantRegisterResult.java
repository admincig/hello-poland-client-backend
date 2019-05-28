package pl.hellopoland.soap.p24.object;

import javax.xml.bind.annotation.XmlAnyElement;

// @XmlRootElement
public class MerchantRegisterResult {
  @XmlAnyElement(lax = true)
  // @XmlMixed


  // @XmlElementRef
  public Object result;
  public GeneralError error;

}
