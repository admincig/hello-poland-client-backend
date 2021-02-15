package pl.hellopoland.soap.p24.object;

import pl.hellopoland.dto.LocationDTO;

import javax.validation.constraints.NotBlank;

public class CorrespondenceAddress {
  @SuppressWarnings("unused") public CorrespondenceAddress() {}

  public CorrespondenceAddress(LocationDTO locationDTO) {
    city = locationDTO.city;
    post_code = locationDTO.zipCode;
    street = locationDTO.street;
  }

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
