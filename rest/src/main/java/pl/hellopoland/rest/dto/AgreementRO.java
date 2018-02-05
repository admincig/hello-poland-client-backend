package pl.hellopoland.rest.dto;

import pl.hellopoland.sight.Agreement;

public class AgreementRO {
  public String linkText;
  public String linkUrl;
  public String text;

  public AgreementRO(Agreement a) {
    this.linkText = a.getLinkText();
    this.linkUrl = a.getLinkUrl();
    this.text = a.getText();
  }
}
