package pl.hellopoland.soap.p24.object;

import javax.validation.constraints.NotBlank;
import pl.hellopoland.dto.LocationDTO;

public class Address {

  public Address(LocationDTO locationDTO) {
    city = locationDTO.city;
    post_code = locationDTO.zipCode;
    street = locationDTO.street;
  }

  public Address() {}

  @NotBlank
  // dostępna tylko wartość ‘PL’
  public String country = "PL";

  @NotBlank
  public String city;

  @NotBlank
  // format xx-xxx lub xxxxx
  public String post_code;

  @NotBlank
  public String street;
}
