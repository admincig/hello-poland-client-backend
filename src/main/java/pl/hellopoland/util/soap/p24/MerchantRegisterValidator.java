package pl.hellopoland.util.soap.p24;

import org.apache.commons.lang3.StringUtils;
import pl.hellopoland.exception.ExceptionMessagesService;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.soap.p24.object.MerchantRegisterRequest;

public class MerchantRegisterValidator {

  private static final String PESEL_INVALID_KEY = "partner.validation.pesel.invalid";
  private static final String NIP_REQUIRED_KEY = "partner.validation.nip.required";
  private static final String REGON_REQUIRED_KEY = "partner.validation.regon.required";
  private static final String KRS_REQUIRED_KEY = "partner.validation.krs.required";

  public static void validate(MerchantRegisterRequest merchant,
      ExceptionMessagesService exceptionMessagesService) {
    int intBusinesType = merchant.business_type.intValue();

    if (intBusinesType == 1
        && (!StringUtils.isNumeric(merchant.pesel) || merchant.pesel.length() != 11)) {
      throw new ConflictingException(
          exceptionMessagesService.getMessageByKey(PESEL_INVALID_KEY));
    }
    if (intBusinesType != 1 && StringUtils.isBlank(merchant.nip)) {
      throw new ConflictingException(
          exceptionMessagesService.getMessageByKey(NIP_REQUIRED_KEY));
    }
    if (intBusinesType != 1 && StringUtils.isBlank(merchant.regon)) {
      throw new ConflictingException(
          exceptionMessagesService.getMessageByKey(REGON_REQUIRED_KEY));
    }
    if (intBusinesType > 3 && intBusinesType != 11 && intBusinesType!= 12 && StringUtils.isBlank(merchant.krs)) {
      throw new ConflictingException(
          exceptionMessagesService.getMessageByKey(KRS_REQUIRED_KEY));
    }
  }

}
