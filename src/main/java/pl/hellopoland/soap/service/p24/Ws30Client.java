package pl.hellopoland.soap.service.p24;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Service;

public class Ws30Client {


  public static void main(String... strings) {
    // var service = new Ws30Service();
    // var port = service.getWs30Port();
    // // boolean response = port.testAccess("71852", "2ee0c1a05174cdbbcfae5e271f3eae15");
    // MerchantRegisterResult response = port.MerchantRegister(71852,
    // "2ee0c1a05174cdbbcfae5e271f3eae15", new MerchantRegisterRequest());



    URL wsdlLocation;
    try {
      wsdlLocation = new URL(SoapConstants.WSDL_LOCATION);

      QName serviceName = new QName(SoapConstants.NAMESPACE_URI, SoapConstants.SERVICE_NAME);
      Service service = Service.create(wsdlLocation, serviceName);
      var port = service.getPort(Ws30Port.class);

      boolean response = port.testAccess("71852", "2ee0c1a05174cdbbcfae5e271f3eae15");


      // var ctx = new javax.xml.ws.handler.soap.SOAPMessageContext();
      //
      // System.out.println(getXmlMessage(SOAPMessageContext.));



      System.out.println(response);
    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }


  public static String getXmlMessage(SOAPMessage message) throws Exception {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    message.writeTo(os);
    final String encoding = (String) message.getProperty(SOAPMessage.CHARACTER_SET_ENCODING);
    if (encoding == null) {
      return new String(os.toByteArray());
    } else {
      return new String(os.toByteArray(), encoding);
    }
  }


}
