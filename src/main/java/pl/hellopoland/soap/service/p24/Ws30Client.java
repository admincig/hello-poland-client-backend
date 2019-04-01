package pl.hellopoland.soap.service.p24;

import pl.hellopoland.soap.object.p24.MerchantRegisterRequest;
import pl.hellopoland.soap.object.p24.MerchantRegisterResult;

public class Ws30Client {


  public static void main(String... strings) {
    var service = new Ws30Service();
    var port = service.getWs30Port();
    // boolean response = port.testAccess("71852", "2ee0c1a05174cdbbcfae5e271f3eae15");
    MerchantRegisterResult response = port.merchantRegister(71852,
        "2ee0c1a05174cdbbcfae5e271f3eae15", new MerchantRegisterRequest());

    System.out.println(response);
  }

}
