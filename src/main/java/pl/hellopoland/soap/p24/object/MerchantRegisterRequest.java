package pl.hellopoland.soap.p24.object;

import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import pl.hellopoland.dto.PartnerDTO;
import pl.hellopoland.soap.p24.enums.Trade;

public class MerchantRegisterRequest {

  public MerchantRegisterRequest() {}

  public MerchantRegisterRequest(PartnerDTO partner) {
    // acceptance = true;
    address = partner.location != null ? new Address(partner.location) : null;
    bank_account = partner.bankAccount;
    business_type = partner.businessType;
    contact_person =
        partner.contactPerson != null ? new ContactPerson(partner.contactPerson) : null;
    correspondence_address = partner.correspondenceAddress != null
        ? new CorrespondenceAddress(partner.correspondenceAddress)
        : (partner.location != null ? new CorrespondenceAddress(partner.location) : null);
    email = partner.email;
    invoice_email = partner.invoiceEmail != null ? partner.invoiceEmail : partner.email;
    krs = partner.krs;
    name = partner.name;
    nip = partner.nip;
    pesel = String.valueOf(partner.pesel);
    phone_number = partner.phoneNumber;
    regon = partner.regon;
    if (partner.representatives != null) {
      ArrayList<Representative> list = partner.representatives.stream()
          .map(r -> new Representative(r)).collect(Collectors.toCollection(ArrayList::new));
      representatives = list.toArray(new Representative[list.size()]);
    }
    services_description = partner.servicesDescription;
    shop_url = partner.shopUrl;
    technical_contact =
        partner.technicalContact != null ? new TechnicalContact(partner.technicalContact)
            : (partner.contactPerson != null ? new TechnicalContact(partner.contactPerson) : null);
    trade = Trade.SPORT_LEISURE.getValue();
  }

  @NotNull
  public Integer business_type;

  @NotBlank
  public String name;

  @Email
  public String email;

  // Wymagany w przypadku rejestracji osoby fizycznej (business type = 1)
  public String pesel;

  @NotBlank
  // w formacie xxxxxxxxx
  public String phone_number;

  @NotBlank
  public String bank_account;

  // Wymagany w przypadku rejestracji firmy (business type != 1). Reprezentanci
  public Representative[] representatives;

  @NotNull
  public ContactPerson contact_person;

  public TechnicalContact technical_contact;

  @NotNull
  public Address address;

  public CorrespondenceAddress correspondence_address;

  @Email
  // Adres email do faktur
  public String invoice_email;

  // nie jest wymagany o ile zostanie przesłane pole services_description, format
  // http(s)://nazwasklepu.pl Adres www strony internetowej
  public String shop_url;

  // nie jest wymagany o ile zostanie przesłane pole shop_url
  public String services_description;

  @NotBlank
  public String trade;

  // Tylko w przypadku business_type = 2 Numer krs
  public String krs;

  // Wymagany w przypadku rejestracji firmy (business_type != 1) Numer nip
  public String nip;

  // Wymagany w przypadku rejestracji firmy (business_type != 1) Numer regon
  public String regon;

  @NotNull
  // Określa czy konto ma zostać aktywowane natychmiast
  public boolean acceptance = true;
}
