package pl.hellopoland.soap.object.p24;

import javax.validation.constraints.NotBlank;

public class Representative {
  @NotBlank
  public String name;

  @NotBlank
  public String pesel;
}
