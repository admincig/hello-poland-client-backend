package pl.hellopoland.soap.p24.service;

import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.service.ServiceSuperclass;
import pl.hellopoland.soap.p24.object.MerchantRegisterRequest;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.soap.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.System.Logger.Level;
import java.nio.charset.Charset;

@LocalBean
@Stateless
public class P24SOAPClient extends ServiceSuperclass {
  private final static String MERCHANT_ID = properties.getProperty("przelewy24.merchantId");
  private final static String PASSWORD = properties.getProperty("przelewy24.password");

  /**
   * Registers the merchant on the Przelewy24 and return merchant id from response.
   * 
   * @return Integer merchant id from response
   */
  public Integer merchantRegistration(MerchantRegisterRequest merchant) {
    final String beginXML =
        "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:php=\"https://secure.przelewy24.pl/external/71852.php\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">"
            + "<soapenv:Header/><soapenv:Body><php:MerchantRegister soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"
            + "<login xsi:type=\"xsd:int\">" + MERCHANT_ID
            + "</login><pass xsi:type=\"xsd:string\">" + PASSWORD + "</pass>";
    final String endXML = "</php:MerchantRegister>\n</soapenv:Body>\n</soapenv:Envelope>";
    Integer merchantId = null;
    try {
      var jc = JAXBContext.newInstance(MerchantRegisterRequest.class);
      var sw = new StringWriter();
      Marshaller marshaller = jc.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
      marshaller.marshal(merchant, sw);
      var sb = new StringBuilder(sw.toString());
      sb.insert(0, beginXML).append(endXML);
      SOAPMessage soapResponse = sendSOAPRequest(sb.toString());
      SOAPBody soapBody = soapResponse.getSOAPBody();
      String errorCode =
          soapBody.getElementsByTagName("errorCode").item(0).getFirstChild().getNodeValue();

      if (!String.valueOf(0).equals(errorCode)) {
        String errorMessage =
            soapBody.getElementsByTagName("errorMessage").item(0).getFirstChild().getNodeValue();
        logger.log(Level.ERROR, "Błąd podczas tworzenia partnera w przelewy24: " + errorMessage);
        throw new ConflictingException(
            "Błąd podczas tworzenia partnera w przelewy24: " + errorMessage);
      }

      var keyElems = soapBody.getElementsByTagName("key");
      for (int i = 0; i < keyElems.getLength(); i++) {
        if ("merchant_id".equals(keyElems.item(i).getFirstChild().getNodeValue())) {
          merchantId =
              Integer.valueOf(keyElems.item(i).getNextSibling().getFirstChild().getNodeValue());
          break;
        }
      }
    } catch (JAXBException | SOAPException | IOException | NumberFormatException e) {
      logger.log(Level.ERROR,
          "Błąd podczas tworzenia partnera w przelewy24: " + e.getLocalizedMessage());
      throw new ConflictingException("Błąd podczas tworzenia partnera w przelewy24");
    }
    return merchantId;
  }

  private static SOAPMessage getSoapMessageFromString(String xml)
      throws SOAPException, IOException {
    MessageFactory factory = MessageFactory.newInstance();
    SOAPMessage message = factory.createMessage(new MimeHeaders(),
        new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8"))));
    return message;
  }

  private SOAPMessage sendSOAPRequest(String xml) throws SOAPException, IOException {
    var soapConnection = SOAPConnectionFactory.newInstance().createConnection();
    SOAPMessage soapResponse =
        soapConnection.call(getSoapMessageFromString(xml.toString()), SoapConstants.NAMESPACE_URI);
    return soapResponse;
  }

}
