package pl.hellopoland.soap.p24.enums;

import pl.hellopoland.dto.CountryDTO;
import pl.hellopoland.enums.LanguageVersion;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public enum Country {

  PL(pl("Polska"), en("Poland")),
  AD(pl("Andora"), en("Andorra")),
  AT(pl("Austria"), en("Austria")),
  BE(pl("Belgia"), en("Belgium")),
  CY(pl("Cypr"), en("Cyprus")),
  CZ(pl("Czechy"), en("Czech Republic")),
  DK(pl("Dania"), en("Denmark")),
  EE(pl("Estonia"), en("Estonia")),
  FI(pl("Finlandia"), en("Finland")),
  FR(pl("Francja"), en("France")),
  EL(pl("Grecja"), en("Greece")),
  ES(pl("Hiszpania"), en("Spain")),
  NL(pl("Holandia"), en("The Netherlands")),
  IE(pl("Irlandia"), en("Ireland")),
  IS(pl("Islandia"), en("Iceland")),
  LT(pl("Litwa"), en("Lithuania")),
  LV(pl("Łotwa"), en("Latvia")),
  LU(pl("Luxemburg"), en("Luxembourg")),
  MT(pl("Malta"), en("Malta")),
  NO(pl("Norwegia"), en("Norway")),
  PT(pl("Portugalia"), en("Portugal")),
  SM(pl("San Marino"), en("San Marino")),
  SK(pl("Słowacja"), en("Slovakia")),
  SI(pl("Słowenia"), en("Slovenia")),
  CH(pl("Szwajcaria"), en("Switzerland")),
  SE(pl("Szwecja"), en("Sweden")),
  HU(pl("Węgry"), en("Hungary")),
  GB(pl("Wielka Brytania"), en("Great Britain")),
  IT(pl("Włochy"), en("Italy")),
  US(pl("USA"), en("US")),
  CA(pl("Kanada"), en("Canada")),
  JP(pl("Japonia"), en("Japan")),
  UA(pl("Ukraina"), en("Ukraine")),
  BY(pl("Białoruś"), en("Belarus")),
  RU(pl("Rosja"), en("Russia"));

  private static final Map<Country, String> p24Languages;
  static {
    p24Languages = new EnumMap<>(Country.class);
    for (Country c : Country.values()) {
      p24Languages.put(c, "en");
    }
    p24Languages.put(Country.PL, "pl");
    p24Languages.put(Country.ES, "es");
    p24Languages.put(Country.IT, "it");
  }

  private Map<LanguageVersion, String> labels = new EnumMap<>(LanguageVersion.class);

  private Country(Map.Entry<LanguageVersion, String>... entries) {
    if (entries != null) {
      Stream.of(entries).forEach(e -> {
        labels.put(e.getKey(), e.getValue());
      });
    }
  }

  private static Map.Entry<LanguageVersion, String> pl(String label) {
    return Map.entry(LanguageVersion.PL_PL, label);
  }

  private static Map.Entry<LanguageVersion, String> en(String label) {
    return Map.entry(LanguageVersion.EN_GB, label);
  }

  public String getP24Language() {
    return p24Languages.get(this);
  }

  public static List<CountryDTO> values(LanguageVersion lv) {
    return Stream.of(values()).map(c -> {
      CountryDTO dto = new CountryDTO();
      dto.symbol = c.name();
      if (c.labels.containsKey(lv)) {
        dto.label = c.labels.get(lv);
        dto.language = lv.getLanuage();
      } else {
        dto.label = c.labels.get(LanguageVersion.EN_GB);
        dto.language = LanguageVersion.EN_GB.getLanuage();
      }
      return dto;
    }).collect(Collectors.toList());
  }
}
