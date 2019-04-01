package pl.hellopoland.soap.object.p24;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlMixed;

public class MerchantRegisterResult {
  @XmlAnyElement
  @XmlMixed
  public Object[] result;
  public GeneralError error;

}
