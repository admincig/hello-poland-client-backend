package pl.hellopoland.soap.object.p24;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MerchantRegisterRequest implements Serializable {
  private static final long serialVersionUID = 6436845648588027603L;

  @NotNull
  // Rodzaj działalności, wartości dopuszczalne w tabeli „Rodzaj działaności”
  public Integer business_type;

  @NotBlank
  public String name;

  @NotBlank
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

  @NotBlank
  // Adres email do faktur
  public String invoice_email;

  // nie jest wymagany o ile zostanie przesłane pole services_description, format
  // http(s)://nazwasklepu.pl Adres www strony internetowej
  public String shop_url;

  // nie jest wymagany o ile zostanie przesłane pole shop_url
  public String services_description;

  @NotBlank
  // Branża, lista dostępna w tabeli „Branża”
  public String trade;

  // Tylko w przypadku business_type = 2 Numer krs
  public String krs;

  // Wymagany w przypadku rejestracji firmy (business_type != 1) Numer nip
  public String nip;

  // Wymagany w przypadku rejestracji firmy (business_type != 1) Numer regon
  public String regon;

  @NotNull
  // Określa czy konto ma zostać aktywowane natychmiast
  public Boolean acceptance;
}
