package pl.hellopoland.soap.service.p24;

import java.io.ByteArrayOutputStream;
import javax.xml.soap.SOAPMessage;
import pl.hellopoland.soap.object.p24.MerchantRegisterRequest;
import pl.hellopoland.soap.object.p24.MerchantRegisterResult;

public class Ws30Client {


  public static void main(String... strings) {
    var service = new Ws30Service();
    var port = service.getWs30Port();
    // boolean response = port.testAccess("71852", "2ee0c1a05174cdbbcfae5e271f3eae15");
    MerchantRegisterResult response = port.merchantRegister(71852,
        "2ee0c1a05174cdbbcfae5e271f3eae15", new MerchantRegisterRequest());


    // var ctx = new javax.xml.ws.handler.soap.SOAPMessageContext();
    //
    // System.out.println(getXmlMessage(SOAPMessageContext.));



    System.out.println(response);
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
