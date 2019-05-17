package pl.hellopoland.soap.p24.enums;

public enum Trade {
  AGD(agd), AGD_RTV(
      agdrtv), (alkoh), (apteki), (artlab), (artmed), (artspoz), (aukcje), (behape), (blizna), (bilety), (buki), (biz),

  private String value;

  private Trade(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
