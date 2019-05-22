package pl.hellopoland.bo;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import pl.hellopoland.dto.LocationDTO;

@Entity
public class Address extends ModelSuperclass {
  private static final long serialVersionUID = -1990557956651882571L;

  public Address() {}

  public Address(LocationDTO locationDTO) {
    city = locationDTO.city;
    post_code = locationDTO.zipCode;
    street = locationDTO.street;
  }

  @NotBlank
  public String country = "PL";

  @NotBlank
  public String city;

  @NotBlank
  // format xx-xxx lub xxxxx
  public String post_code;

  @NotBlank
  public String street;
}
