package pl.hellopoland.soap.object.p24;

import javax.validation.constraints.NotBlank;

public class ContactPerson {

  @NotBlank
  // Imię oraz nazwisko
  public String name;

  @NotBlank
  public String email;

  @NotBlank
  // format xxxxxxxxx
  public String phone_number;
}
