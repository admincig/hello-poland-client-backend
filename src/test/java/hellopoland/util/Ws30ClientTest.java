package hellopoland.util;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.junit.Test;
import pl.hellopoland.soap.object.p24.MerchantRegisterRequest;
import pl.hellopoland.soap.object.p24.MerchantRegisterResult;
import pl.hellopoland.soap.service.p24.SoapConstants;
import pl.hellopoland.soap.service.p24.Ws30Port;

public class Ws30ClientTest {

  @Test
  public void soapTest() {
    // var service = new Ws30Service();
    // var port = service.getWs30Port();
    // // boolean response = port.testAccess("71852", "2ee0c1a05174cdbbcfae5e271f3eae15");
    // MerchantRegisterResult response = port.merchantRegister(71852,
    // "2ee0c1a05174cdbbcfae5e271f3eae15", new MerchantRegisterRequest());
    // System.out.println("RESPONSE:");
    // System.out.println(response.getError().errorMessage);


    URL wsdlLocation;
    try {
      wsdlLocation = new URL(SoapConstants.WSDL_LOCATION);

      QName serviceName = new QName(SoapConstants.NAMESPACE_URI, SoapConstants.SERVICE_NAME);
      Service service = Service.create(wsdlLocation, serviceName);
      var port = service.getPort(Ws30Port.class);

      // boolean response = port.testAccess("71852", "2ee0c1a05174cdbbcfae5e271f3eae15");
      MerchantRegisterResult response = port.merchantRegister(71852,
          "2ee0c1a05174cdbbcfae5e271f3eae15", new MerchantRegisterRequest());


      System.out.println(response);
    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }


  }

}
