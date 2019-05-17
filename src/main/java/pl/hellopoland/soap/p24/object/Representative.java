package pl.hellopoland.soap.p24.object;

import javax.validation.constraints.NotBlank;

public class Representative {
  @NotBlank
  public String name;

  @NotBlank
  public String pesel;
}
