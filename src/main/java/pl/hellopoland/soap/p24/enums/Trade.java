package pl.hellopoland.soap.p24.enums;

import java.util.Arrays;

public enum Trade {
//@formatter:off
  AGD("agd"), AGD_RTV("agdrtv"), ALCOHOLS("alkoh"), PHARMACY("apteki"), LABORATORY_ARTICLES("artlab"), MEDICAL_ARTICLE("artmed"),
  FOODSTUFFS("artspoz"), AUCTIONS("aukcje"), BHP("behape"), UNDERWEAR("blizna"), TICKETS("bilety"), BOOKMAKER("buki"), JEWELRY_WATCHES("biz"),
  ARCHITECTURE("budow"), CHEMISTRY("chemia"), MAGAZINES("czaspis"), DECOR("dekor"), DEVOTIONAL_ARTICLES("dewoc"), HOME_GARDEN("domiogr"),
  CHILD("dziecko"), ELECTRONICS("elektronika"), E_CIGARETTES("epapier"), ESOTERIC("ezoter"), PHILATELY("filatel"), FINANCES("finanse"),
  PHOTOGRAPHY("fotogr"), FUNDATION("fundacja"), GALLANTRY("galant"), GADGETS("gadzet"), GAMES("gry"), HOSTING("komphost"), HOTELS("hotel"),
  INSTITUTIONS("instyt"), COMPUTERS("komputery"), BOOKS("ksiazki"), COSMETICS("kosmetyki"), BOOKSTORE("ksieg"), FLOWERS_GIFTS("kip"),
  MASS_INVOICES("mwf"), MACHINES("maszyny"), OFFICE_SUPPLIES("matbiur"), FOIL_MATERIALS("matfol"), PAPER_MATERIALS("matpap"),
  MILITARY_ARTILCES("militaria"), MOTORING("motoryz"), MULTIMEDIA_MUSIC("mim"), HEADSTONES("nagrob"), TOOLS("narzedzia"),
  SCIENCE_EDUCATION("nis"), NUMISMATICS("numiz"), FOOTWEAR("obuwie"), CLOTHING("odziez"), CLASSIFIEDS("ogl"), GARDEN("ogrod"), SOFTWEAR("oprogra"),
  LIGHTING("oswietl"), HABERDASHERY("pasman"), TRAVELS("podroze"), DATING_SITE("randki"), E_WALLET("portfel"), PRESS("prasa"), LAW("prawo"),
  CURIER_POSTS("kurier"), ADVERTISEMENT("reklama"), HANDICRAFT("rekodz"), PARENTS("rodzice"), RTV("rtv"), INTERNET_SERVICE("serint"),
  MUSIC_STORE("sklmuz"), SPORT_LEISURE("siw"), DIETARY_SUPPLEMENTS("suplem"), GLASS("szklo"), TRAINING("szkol"), ART("sztuka"),
  WEDDING("slubne"), THEATER_FILM("tif"), TELECOMMUNICATION("telek"), TEXTILES("tkan"), WEBSITE_DEVELOPMENT("twstrwww"), INSURANCE("ubezp"),
  SERVICES("uslugi"), MULTI_BRANCH("wielob"), WP_CONCEPTSHOP_PL("wpc"), APARTMENT_EQUIPMENT("wypmiesz"), SHOPS_EQUIPMENT("wypsklep"),
  TOBACCO_PRODUCTS("wyrtyt"), EYESIGHT_GLASSES("wio"), VOD("vod"), TOYS("zabawki"), HEALTH_COSMETICS("zik"), ANIMALS("zwierz");
//@formatter:on
  private String value;

  private Trade(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static Trade getTrade(String value) {
    return Arrays.stream(Trade.values()).filter(t -> t.getValue().equals(value)).findFirst()
        .orElse(null);
  }

}
