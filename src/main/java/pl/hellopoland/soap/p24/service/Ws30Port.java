package pl.hellopoland.soap.p24.service;

import pl.hellopoland.soap.p24.object.MerchantRegisterRequest;
import pl.hellopoland.soap.p24.object.MerchantRegisterResult;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

@WebService(name = SoapConstants.PORT_TYPE, targetNamespace = SoapConstants.NAMESPACE_URI)
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface Ws30Port {

  @WebMethod
  @WebResult(partName = "return")
  public boolean testAccess(@WebParam(name = "login", partName = "login") String login,
      @WebParam(name = "pass", partName = "pass") String pass);

  @WebMethod
  @WebResult(partName = "return")
  public MerchantRegisterResult merchantRegister(
      @WebParam(name = "login", partName = "login") Integer login,
      @WebParam(name = "pass", partName = "pass") String pass,
      @WebParam(name = "merchant", partName = "merchant") MerchantRegisterRequest merchant);

}
