package pl.hellopoland.soap.p24.object;

import javax.validation.constraints.NotBlank;

public class Representative {
  public Representative() {}

  public Representative(@NotBlank String name, @NotBlank String pesel) {
    this.name = name;
    this.pesel = pesel;
  }

  @NotBlank
  public String name;

  @NotBlank
  public String pesel;
}
