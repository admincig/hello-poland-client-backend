package pl.hellopoland.soap.p24.object;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlMixed;

public class MerchantRegisterResult {
  @XmlAnyElement
  @XmlMixed
  public Object[] result;
  public GeneralError error;

}
