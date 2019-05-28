package hellopoland.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBResult;
import javax.xml.bind.util.JAXBSource;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.ws.Service;
import org.junit.Test;
import pl.hellopoland.soap.p24.enums.Trade;
import pl.hellopoland.soap.p24.object.Address;
import pl.hellopoland.soap.p24.object.ContactPerson;
import pl.hellopoland.soap.p24.object.GeneralError;
import pl.hellopoland.soap.p24.object.MerchantRegisterRequest;
import pl.hellopoland.soap.p24.object.MerchantRegisterResult;
import pl.hellopoland.soap.p24.service.SoapConstants;
import pl.hellopoland.soap.p24.service.Ws30Port;

// TODO: not finished!
public class Ws30ClientTest {

  // @Test
  public void soapMerchantRegisterErrorResultTest() {
    try {
      URL wsdlLocation = new URL(SoapConstants.WSDL_LOCATION);
      QName serviceName = new QName(SoapConstants.NAMESPACE_URI, SoapConstants.SERVICE_NAME);
      Service service = Service.create(wsdlLocation, serviceName);
      var port = service.getPort(Ws30Port.class);

      // boolean response = port.testAccess("71852", "2ee0c1a05174cdbbcfae5e271f3eae15");
      MerchantRegisterResult response = port.merchantRegister(71852,
          "2ee0c1a05174cdbbcfae5e271f3eae15", new MerchantRegisterRequest());

      System.out.println(response);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Test
  public void soapMerchantRegisterSuccessResultTest() {
    try {
      URL wsdlLocation = new URL(SoapConstants.WSDL_LOCATION);
      QName serviceName = new QName(SoapConstants.NAMESPACE_URI, SoapConstants.SERVICE_NAME);
      Service service = Service.create(wsdlLocation, serviceName);
      var port = service.getPort(Ws30Port.class);

      // boolean response = port.testAccess("71852", "2ee0c1a05174cdbbcfae5e271f3eae15");

      var merchant = new MerchantRegisterRequest();
      var address = new Address();
      address.city = "Nizniy";
      address.post_code = "55-120";
      address.street = "Stumilowego Lasu 6";
      var contactPerson = new ContactPerson();
      contactPerson.email = "m@everytarget.com";
      contactPerson.name = "Michał Dusiński";
      contactPerson.phone_number = "692425966";
      merchant.address = address;
      merchant.bank_account = "68114011400000506894001001";
      merchant.business_type = 8;
      merchant.contact_person = contactPerson;
      merchant.email = "m@everytarget.com";
      merchant.invoice_email = "m@everytarget.com";
      merchant.krs = "0000593323";
      merchant.name = "Everytarget sp. z o.o.";
      merchant.nip = "9151796154";
      merchant.phone_number = "692425966";
      merchant.regon = "362596009";
      merchant.services_description = "tarcze";
      merchant.trade = Trade.SPORT_LEISURE.getValue();

      MerchantRegisterResult response =
          port.merchantRegister(71852, "2ee0c1a05174cdbbcfae5e271f3eae15", merchant);


      var k = new JAXBElement<Object>(new QName("bar"), Object.class, response.result);



      // var e = (org.w3c.dom.Element) response.result;
      // e.getClass();
      // var a = e.getElementsByTagName("value");
      // var z = a.item(0).getFirstChild().getNodeValue();

      JAXBContext jc = JAXBContext.newInstance(MerchantRegisterResult.class, GeneralError.class);
      Unmarshaller unmarshaller = jc.createUnmarshaller();
      var payload = (MerchantRegisterResult) unmarshaller.unmarshal(new JAXBSource(jc, response));

      // Marshaller marshaller = jc.createMarshaller();
      // marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      // marshaller.marshal(merchant, System.out);

      System.out.println(payload);
      // for (Object o : payload.result) {
      // System.out.println(o.getClass());
      // }
      // System.out.println(response.result);
      // System.out.println(response.error.errorCode);
      // System.out.println(response.error.errorMessage);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  // @Test
  public void soapMerchantRegisterErrorResultXMLTest() {
    try {
      SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
      SOAPConnection soapConnection = soapConnectionFactory.createConnection();
      String url = SoapConstants.NAMESPACE_URI;
      String soapMessage =
          "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:php=\"https://secure.przelewy24.pl/external/71852.php\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">\n"
              + "   <soapenv:Header/>\n" + "   <soapenv:Body>\n"
              + "      <php:MerchantRegister soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n"
              + "         <login xsi:type=\"xsd:int\">71852</login>\n"
              + "         <pass xsi:type=\"xsd:string\">2ee0c1a05174cdbbcfae5e271f3eae15</pass>\n"
              + "         <merchant xsi:type=\"php:MerchantRegisterRequest\">\n"
              + "            <!--You may enter the following 19 items in any order-->\n"
              + "            <business_type xsi:type=\"xsd:int\"></business_type>\n"
              + "            <name xsi:type=\"xsd:string\"></name>\n"
              + "            <email xsi:type=\"xsd:string\"></email>\n"
              + "            <pesel xsi:type=\"xsd:string\"></pesel>\n"
              + "            <phone_number xsi:type=\"xsd:string\"></phone_number>\n"
              + "            <bank_account xsi:type=\"xsd:string\"></bank_account>\n"
              + "            <representatives xsi:type=\"php:ArrayOfRepresentative\" soapenc:arrayType=\"php:Representative[]\"/>\n"
              + "            <contact_person xsi:type=\"php:ContactPerson\">\n"
              + "               <!--You may enter the following 3 items in any order-->\n"
              + "               <name xsi:type=\"xsd:string\"></name>\n"
              + "               <email xsi:type=\"xsd:string\"></email>\n"
              + "               <phone_number xsi:type=\"xsd:string\"></phone_number>\n"
              + "            </contact_person>\n"
              + "            <technical_contact xsi:type=\"php:TechnicalContact\">\n"
              + "               <!--You may enter the following 3 items in any order-->\n"
              + "               <name xsi:type=\"xsd:string\"></name>\n"
              + "               <email xsi:type=\"xsd:string\"></email>\n"
              + "               <phone_number xsi:type=\"xsd:string\"></phone_number>\n"
              + "            </technical_contact>\n"
              + "            <address xsi:type=\"php:Address\">\n"
              + "               <!--You may enter the following 4 items in any order-->\n"
              + "               <country xsi:type=\"xsd:string\"></country>\n"
              + "               <city xsi:type=\"xsd:string\"></city>\n"
              + "               <post_code xsi:type=\"xsd:string\"></post_code>\n"
              + "               <street xsi:type=\"xsd:string\"></street>\n"
              + "            </address>\n"
              + "            <correspondence_address xsi:type=\"php:CorrespondenceAddress\">\n"
              + "               <!--You may enter the following 4 items in any order-->\n"
              + "               <country xsi:type=\"xsd:string\"></country>\n"
              + "               <city xsi:type=\"xsd:string\"></city>\n"
              + "               <post_code xsi:type=\"xsd:string\"></post_code>\n"
              + "               <street xsi:type=\"xsd:string\"></street>\n"
              + "            </correspondence_address>\n"
              + "            <invoice_email xsi:type=\"xsd:string\"></invoice_email>\n"
              + "            <shop_url xsi:type=\"xsd:string\"></shop_url>\n"
              + "            <services_description xsi:type=\"xsd:string\"></services_description>\n"
              + "            <trade xsi:type=\"xsd:string\"></trade>\n"
              + "            <krs xsi:type=\"xsd:string\"></krs>\n"
              + "            <nip xsi:type=\"xsd:string\"></nip>\n"
              + "            <regon xsi:type=\"xsd:string\"></regon>\n"
              + "            <acceptance xsi:type=\"xsd:boolean\"></acceptance>\n"
              + "         </merchant>\n" + "      </php:MerchantRegister>\n"
              + "   </soapenv:Body>\n" + "</soapenv:Envelope>";

      SOAPMessage soapResponse = soapConnection.call(getSoapMessageFromString(soapMessage), url);
      printSOAPResponse(soapResponse);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Test
  public void soapMerchantRegisterSuccessResultXMLTest() {
    try {
      SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
      SOAPConnection soapConnection = soapConnectionFactory.createConnection();
      String url = SoapConstants.NAMESPACE_URI;
      String soapMessage =
          "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:php=\"https://secure.przelewy24.pl/external/71852.php\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">\n"
              + "   <soapenv:Header/>\n" + "   <soapenv:Body>\n"
              + "      <php:MerchantRegister soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n"
              + "         <login xsi:type=\"xsd:int\">71852</login>\n"
              + "         <pass xsi:type=\"xsd:string\">2ee0c1a05174cdbbcfae5e271f3eae15</pass>\n"
              + "         <merchant xsi:type=\"php:MerchantRegisterRequest\">\n"
              + "            <!--You may enter the following 19 items in any order-->\n"
              + "            <business_type xsi:type=\"xsd:int\">8</business_type>\n"
              + "            <name xsi:type=\"xsd:string\">Everytarget sp. z o.o.</name>\n"
              + "            <email xsi:type=\"xsd:string\">m@everytarget.com</email>\n"
              + "            <phone_number xsi:type=\"xsd:string\">692425966</phone_number>\n"
              + "            <bank_account xsi:type=\"xsd:string\">68114011400000506894001001</bank_account>\n"
              + "            <representatives xsi:type=\"php:ArrayOfRepresentative\" soapenc:arrayType=\"php:Representative[]\"/>\n"
              + "            <contact_person xsi:type=\"php:ContactPerson\">\n"
              + "               <!--You may enter the following 3 items in any order-->\n"
              + "               <name xsi:type=\"xsd:string\">Michał Dusiński</name>\n"
              + "               <email xsi:type=\"xsd:string\">m@everytarget.com</email>\n"
              + "               <phone_number xsi:type=\"xsd:string\">692425966</phone_number>\n"
              + "            </contact_person>\n"
              + "             <address xsi:type=\"php:Address\">\n"
              + "               <!--You may enter the following 4 items in any order-->\n"
              + "               <country xsi:type=\"xsd:string\">PL</country>\n"
              + "               <city xsi:type=\"xsd:string\">Nizniy</city>\n"
              + "               <post_code xsi:type=\"xsd:string\">55-120</post_code>\n"
              + "               <street xsi:type=\"xsd:string\">Stumilowego Lasu 6</street>\n"
              + "            </address>\n"
              + "            <invoice_email xsi:type=\"xsd:string\">m@everytarget.com</invoice_email>\n"
              + "            <services_description xsi:type=\"xsd:string\">tarcze</services_description>\n"
              + "            <trade xsi:type=\"xsd:string\">siw</trade>\n"
              + "            <krs xsi:type=\"xsd:string\">0000593323</krs>\n"
              + "            <nip xsi:type=\"xsd:string\">9151796154</nip>\n"
              + "            <regon xsi:type=\"xsd:string\">362596009</regon>\n"
              + "            <acceptance xsi:type=\"xsd:boolean\">true</acceptance>\n"
              + "         </merchant>\n" + "      </php:MerchantRegister>\n"
              + "   </soapenv:Body>\n" + "</soapenv:Envelope>";

      SOAPMessage soapResponse = soapConnection.call(getSoapMessageFromString(soapMessage), url);
      printSOAPResponse(soapResponse);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private static SOAPMessage getSoapMessageFromString(String xml)
      throws SOAPException, IOException {
    MessageFactory factory = MessageFactory.newInstance();
    SOAPMessage message = factory.createMessage(new MimeHeaders(),
        new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8"))));
    return message;
  }

  private static void printSOAPResponse(SOAPMessage soapResponse) throws Exception {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    var sourceContent = soapResponse.getSOAPBody();

    // sourceContent.getChildNodes()



    // Source sourceContent = soapResponse.getSOAPPart().getContent();
    System.out.print("\nResponse SOAP Message = ");
    // StreamResult result = new StreamResult(System.out);


    JAXBContext jc = JAXBContext.newInstance(MerchantRegisterResult.class);
    // Unmarshaller unmarshaller = jc.createUnmarshaller();
    // MerchantRegisterResult payload = (MerchantRegisterResult)
    // unmarshaller.unmarshal(sourceContent);


    JAXBResult result = new JAXBResult(jc);


    // transformer.transform(sourceContent, result);

    var o = (MerchantRegisterResult) result.getResult();
    System.out.println(o);
  }

}
