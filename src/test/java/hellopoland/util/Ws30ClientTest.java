package hellopoland.util;

import org.junit.Test;
import pl.hellopoland.soap.object.p24.MerchantRegisterRequest;
import pl.hellopoland.soap.object.p24.MerchantRegisterResult;
import pl.hellopoland.soap.service.p24.Ws30Service;

public class Ws30ClientTest {

  @Test
  public void soapTest() {
    var service = new Ws30Service();
    var port = service.getWs30Port();
    // boolean response = port.testAccess("71852", "2ee0c1a05174cdbbcfae5e271f3eae15");
    var merchant = new MerchantRegisterRequest();
    MerchantRegisterResult response =
        port.MerchantRegister(71852, "2ee0c1a05174cdbbcfae5e271f3eae15", merchant);
    System.out.println("RESPONSE:");
    System.out.println(response.error.errorMessage);
  }

}
