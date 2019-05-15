package pl.hellopoland.soap.service.p24;

public interface SoapConstants {
  final static String BASE_NAMESPACE_URI = "https://secure.przelewy24.pl/external/71852.";
  final static String WSDL_LOCATION = BASE_NAMESPACE_URI + "wsdl";
  final static String NAMESPACE_URI = BASE_NAMESPACE_URI + "php";
  final static String PORT_TYPE = "ws30Port";
  final static String SERVICE_NAME = "ws30Service";
}
