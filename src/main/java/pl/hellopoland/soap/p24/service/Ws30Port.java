package pl.hellopoland.soap.p24.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import pl.hellopoland.soap.p24.object.MerchantRegisterRequest;
import pl.hellopoland.soap.p24.object.MerchantRegisterResult;

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
