package pl.hellopoland.soap.p24.object;

import java.util.HashMap;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

  @XmlElementDecl(name = "result", namespace = "xmlns:ns2")
  public JAXBElement<?> createResult(HashMap result) {
    return new JAXBElement<HashMap>(new QName("result"), HashMap.class, result);
  }

}
