package pl.hellopoland.soap.p24.object;

import pl.hellopoland.dto.ContactPersonDTO;

import jakarta.validation.constraints.NotBlank;

public class ContactPerson {
  @SuppressWarnings("unused") public ContactPerson() {}

  public ContactPerson(ContactPersonDTO dto) {
    name = dto.name;
    email = dto.email;
    phone_number = String.valueOf(dto.phone);
  }

  @NotBlank
  // firstname and lastname
  public String name;

  @NotBlank
  public String email;

  @NotBlank
  // format xxxxxxxxx
  public String phone_number;
}
