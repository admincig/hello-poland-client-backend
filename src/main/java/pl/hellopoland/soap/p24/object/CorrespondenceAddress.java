package pl.hellopoland.soap.p24.object;

import javax.validation.constraints.NotBlank;

public class CorrespondenceAddress {
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
