package pl.hellopoland.soap.p24.service;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

@WebServiceClient(name = SoapConstants.SERVICE_NAME, targetNamespace = SoapConstants.NAMESPACE_URI,
    wsdlLocation = SoapConstants.WSDL_LOCATION)
public class Ws30Service extends Service {
  private final static Logger logger = System.getLogger(Ws30Service.class.getName());
  private final static URL WSDL_LOCATION_URL;
  static {
    URL url = null;
    try {
      url = new URL(SoapConstants.WSDL_LOCATION);
    } catch (MalformedURLException e) {
      logger.log(Level.ERROR,
          "Failed to create URL for the wsdl Location: " + SoapConstants.WSDL_LOCATION);
      logger.log(Level.ERROR, e.getMessage());
    }
    WSDL_LOCATION_URL = url;
  }

  public Ws30Service(URL wsdlLocation, QName serviceName) {
    super(wsdlLocation, serviceName);
  }

  public Ws30Service() {
    super(WSDL_LOCATION_URL, new QName(SoapConstants.NAMESPACE_URI, SoapConstants.SERVICE_NAME));
  }

  @WebEndpoint(name = SoapConstants.PORT_TYPE)
  public Ws30Port getWs30Port() {
    return super.getPort(new QName(SoapConstants.NAMESPACE_URI, SoapConstants.PORT_TYPE),
        Ws30Port.class);
  }

  /**
   *
   * @param features A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.
   *        Supported features not in the <code>features</code> parameter will have their default
   *        values.
   * @return returns Ws30Port
   */
  @WebEndpoint(name = SoapConstants.PORT_TYPE)
  public Ws30Port getWs30Port(WebServiceFeature... features) {
    return super.getPort(new QName(SoapConstants.NAMESPACE_URI, SoapConstants.PORT_TYPE),
        Ws30Port.class, features);
  }

}
