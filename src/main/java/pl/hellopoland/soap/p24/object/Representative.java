package pl.hellopoland.soap.p24.object;

import pl.hellopoland.dto.PartnerRepresentativeDTO;

import javax.validation.constraints.NotBlank;

public class Representative {
  @SuppressWarnings("unused") public Representative() {}

  public Representative(PartnerRepresentativeDTO dto) {
    this.name = dto.name;
    this.pesel = String.valueOf(dto.socialNumber);
  }

  @NotBlank
  public String name;

  @NotBlank
  public String pesel;
}
