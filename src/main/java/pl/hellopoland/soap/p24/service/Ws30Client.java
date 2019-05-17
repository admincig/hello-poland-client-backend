package pl.hellopoland.soap.p24.service;

import pl.hellopoland.soap.p24.object.MerchantRegisterRequest;
import pl.hellopoland.soap.p24.object.MerchantRegisterResult;

// TODO: not finished!
public class Ws30Client {
  private final static Ws30Port PORT = new Ws30Service().getWs30Port();



  public static void main(String... strings) {
    // boolean response = port.testAccess("71852", "2ee0c1a05174cdbbcfae5e271f3eae15");
    MerchantRegisterResult response = PORT.merchantRegister(71852,
        "2ee0c1a05174cdbbcfae5e271f3eae15", new MerchantRegisterRequest());

    System.out.println(response);
  }

}
