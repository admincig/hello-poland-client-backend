package pl.hellopoland.soap.p24.object;

import javax.validation.constraints.NotBlank;
import pl.hellopoland.dto.PartnerRepresentativeDTO;

public class Representative {
  public Representative() {}

  public Representative(PartnerRepresentativeDTO dto) {
    this.name = dto.name;
    this.pesel = String.valueOf(dto.socialNumber);
  }

  @NotBlank
  public String name;

  @NotBlank
  public String pesel;
}
