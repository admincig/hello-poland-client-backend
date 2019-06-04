package pl.hellopoland.soap.p24.object;

import javax.validation.constraints.NotBlank;
import pl.hellopoland.dto.ContactPersonDTO;

public class ContactPerson {
  public ContactPerson() {}

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
