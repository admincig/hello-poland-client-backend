package pl.hellopoland.soap.p24.object;

import java.util.Map;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlMixed;

public class MerchantRegisterResult {
  @XmlAnyElement
  @XmlMixed
  public Map<?, ?> result;
  // public Object[] result;
  public GeneralError error;

}
