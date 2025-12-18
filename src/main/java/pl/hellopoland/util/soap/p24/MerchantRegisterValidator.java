package pl.hellopoland.util.soap.p24;

import org.apache.commons.lang3.StringUtils;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.soap.p24.object.MerchantRegisterRequest;

public class MerchantRegisterValidator {

  public static void validate(MerchantRegisterRequest merchant) {
    int intBusinesType = merchant.business_type.intValue();

    if (intBusinesType == 1
        && (!StringUtils.isNumeric(merchant.pesel) || merchant.pesel.length() != 11)) {
      throw new ConflictingException(
          "Brak lub niepoprawny nr PESEL. W przypadku rejestracji osoby fizycznej nr PESEL jest obowiązkowy.");
    }
    // TODO: check is it true?!!
    // if (intBusinesType != 1
    // && (merchant.representatives == null || merchant.representatives.length == 0)) {
    // throw new ConflictingException(
    // "Brak reprezentantów firmy. Obowiązkowe w przypadku rejestracji działalności innej niż osoba
    // fizyczna.");
    // }
    if (intBusinesType != 1 && StringUtils.isBlank(merchant.nip)) {
      throw new ConflictingException(
          "Brak nr NIP. Obowiązkowe w przypadku rejestracji działalności innej niż osoba fizyczna.");
    }
    if (intBusinesType != 1 && StringUtils.isBlank(merchant.regon)) {
      throw new ConflictingException(
          "Brak nr REGON. Obowiązkowe w przypadku rejestracji działalności innej niż osoba fizyczna.");
    }
    if (intBusinesType > 3 && intBusinesType != 11 && intBusinesType!= 12 && StringUtils.isBlank(merchant.krs)) {
      throw new ConflictingException(
          "Brak nr KRS. Obowiązkowe w przypadku rejestracji jednoosobowej działalności gospodarczej.");
    }
//    if (!StringUtils.isNumeric(merchant.phone_number)) {
//      throw new ConflictingException("Brak lub niepoprawny format nr telefonu.");
//    }
//    if (merchant.shop_url == null && merchant.services_description == null) {
//      throw new ConflictingException(
//          "Co najmniej jeden z parametrów 'shop_url' lub 'services_description' jest wymagany.");
//    }
  }

}
