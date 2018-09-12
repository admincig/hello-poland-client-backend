package pl.hellopoland.service.timer;

import static java.util.Collections.singletonList;
import java.io.InputStream;
import java.lang.System.Logger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import pl.hellopoland.bo.ImageCollector;
import pl.hellopoland.bo.Location;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.bo.Portal;
import pl.hellopoland.bo.Portal.Type;
import pl.hellopoland.bo.Sight;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.User;
import pl.hellopoland.bo.UserRole;
import pl.hellopoland.dto.FrequencyDataDTO;
import pl.hellopoland.dto.FrequencyTypeDTO;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.dto.TicketDefinitionDTO;
import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.security.password.PasswordEncoder;
import pl.hellopoland.service.ImageService;
import pl.hellopoland.service.ServiceSuperclass;
import pl.hellopoland.service.SightEventService;
import pl.hellopoland.service.TicketDefinitionService;
import pl.hellopoland.service.TicketPoolDefinitionService;
import pl.hellopoland.util.DtoMapper;

@Startup
@Singleton
@DependsOn({"Configuration"})
public class DbFiller extends ServiceSuperclass {

  @Inject
  SightEventService sService;

  @Inject
  ImageService imgService;

  @Inject
  TicketPoolDefinitionService tpdService;

  @Inject
  TicketDefinitionService tdService;

  @Inject
  private PasswordEncoder passwordEncoder;

  private User userHelloPoland;
  private User userZoo;
  private User userKolejkowo;
  private User userStadionGd;
  private ImageCollector afrImg;
  private ImageCollector hpImg;
  private ImageCollector kol1Img;
  private ImageCollector kol2Img;
  private ImageCollector meczLSImg;
  private ImageCollector meczPCImg;
  private ImageCollector parkSzcz1Img;
  private ImageCollector parkSzcz2Img;
  private ImageCollector stadGdImg;
  private ImageCollector zooImg;
  private ImageCollector zwStadImg;
  private ImageCollector zwZooImg;
  private Location hpLocation;
  private Location zooWroLocation;
  private Location stadGdLocation;
  private Location kolLocation;
  private Location parkSzczLocation;
  private Sight hp;
  private Sight zooWro;
  private Sight stadGd;
  private Sight kol;
  private SightEvent afrEvent;
  private SightEvent kolEvent;
  private SightEvent meczPCEvent;
  private SightEvent parkSzczEvent;
  private SightEvent zwStadEvent;
  private SightEvent zwZooEvent;

  @PostConstruct
  public void fillDb() {
    String hibernateStrategy = properties.get("hibernate.hbm2ddl.auto").toString();
    if (!hibernateStrategy.startsWith("create")) {
      logger.log(Logger.Level.INFO,
          "omitting dbfiller because hibernate.hbm2ddl.auto isnt set to create");
      return;
    }
    createPortals();
    createUsers();
    createImageCollectors();
    createLocations();
    createSights();
    createSightEvents();
    createTicketPoolDefinitions();
    logger.log(Logger.Level.INFO, "dbfiller finished");
  }

  private void createUsers() {
    userHelloPoland = createPartner("Hello Poland",
        "eyJhbGciOiJub25lIn0.eyJzdWIiOiI1RDU1NTEwOURBM0Y5RUQwMEVFRkQyNTY2MDMwRUQ3MjJBNEQ3NzAwREU2MDA2NjQ5NzhBNjIwOTRCNUVFN0Y0In0.",
        Integer.valueOf(properties.getProperty("przelewy24.merchantId")));
    userZoo = createPartner("Zoo",
        "eyJhbGciOiJub25lIn0.eyJzdWIiOiJDOTU1NTI0MDk2REU0MjlEQjBGODM1NTA1RUI5MzAxNzkzQzE4NEJBQzM2NTFBNzI2MDFCRDNGMUFEQTkyQzAzIn0.",
        Integer.valueOf(properties.getProperty("przelewy24.merchantId")));
    userKolejkowo = createPartner("Kolejkowo",
        "eyJhbGciOiJub25lIn0.eyJzdWIiOiI0MDc5MTkyRkI2NTQyQTYyRjc3QTcwNDZDRDU1QkJGNUM5NDAzNkE0MjRFRDI4RTM0MEYwODNCRDE1MDRFODZBIn0.",
        Integer.valueOf(properties.getProperty("przelewy24.merchantId")));
    userStadionGd = createPartner("Stadion Gdański",
        "eyJhbGciOiJub25lIn0.eyJzdWIiOiIyODQyODcyRThEQ0EzMENFNkJBOTk5REMzQjBGODJFNUNFOTNFNzA5RTJEMjlGMEQ4NjFFOTU4QjMxQ0QwQzREIn0.",
        Integer.valueOf(properties.getProperty("przelewy24.merchantId")));
  }

  private User createPartner(String partnerName, String token, Integer p24Id) {
    Partner helloPolandPartner = new Partner();
    helloPolandPartner.setName(partnerName + " Partner");
    helloPolandPartner.setHptToken(token);
    helloPolandPartner.setP24Id(p24Id);

    User user = new User();

    UserRole userRole = new UserRole();
    userRole.setRole("partner");
    userRole.setUser(user);

    String email = partnerName.toLowerCase().replaceAll(" ", "") + "@"
        + partnerName.toLowerCase().replaceAll(" ", "") + ".pl";
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(partnerName.toLowerCase().replaceAll(" ", "")));
    user.setRoles(singletonList(userRole));
    user.setPartner(helloPolandPartner);

    em.persist(user);

    return user;
  }

  private void createPortals() {
    createHelloTicketCloud();
  }

  private void createHelloTicketCloud() {
    Portal portal = new Portal();
    portal.setName("Hello Ticket Cloud");
    portal.setUrl(properties.getProperty("hpt.cloud.url"));
    portal.setType(Type.HELLOTICKET_CLOUD_1);
    em.persist(portal);
  }

  private void createImageCollectors() {
    afrImg = createImageCollector(this.getClass().getResourceAsStream("/images/afrykarium.jpg"));
    hpImg = createImageCollector(this.getClass().getResourceAsStream("/images/HP.jpg"));
    kol1Img = createImageCollector(this.getClass().getResourceAsStream("/images/kolekowo_1.jpg"));
    kol2Img = createImageCollector(this.getClass().getResourceAsStream("/images/Kolejkowo_2.jpg"));
    meczLSImg =
        createImageCollector(this.getClass().getResourceAsStream("/images/mecz_lechia_slask.jpg"));
    meczPCImg =
        createImageCollector(this.getClass().getResourceAsStream("/images/mecz_polsa_czechy.jpg"));
    parkSzcz1Img =
        createImageCollector(this.getClass().getResourceAsStream("/images/park_szczytnicki_1.jpg"));
    parkSzcz2Img =
        createImageCollector(this.getClass().getResourceAsStream("/images/park_szczytnicki_2.jpg"));
    stadGdImg = createImageCollector(
        this.getClass().getResourceAsStream("/images/Stadion_w_Gdańsku_1.jpg"));
    zooImg = createImageCollector(this.getClass().getResourceAsStream("/images/zoo.jpg"));
    zwStadImg = createImageCollector(
        this.getClass().getResourceAsStream("/images/zwiedzanie_stadionu.jpg"));
    zwZooImg =
        createImageCollector(this.getClass().getResourceAsStream("/images/zwiedzanie_zoo.jpg"));
  }

  private ImageCollector createImageCollector(InputStream is) {
    return imgService.validateAndStoreImageCollector(is, "jpg", null);
  }

  private void createLocations() {
    hpLocation = createLocation(51.111746, 17.0240973, "Świętego Mikołaja 19", "50-128", "Wrocław",
        "Polska");
    zooWroLocation =
        createLocation(51.1041258, 17.0720083, "Wróblewskiego 1-5", "51-618", "Wrocław", "Polska");
    stadGdLocation = createLocation(54.3900786, 18.6371082, "Pokoleń Lechii Gdańsk 1", "80-560",
        "Gdańsk", "Polska");
    kolLocation = createLocation(51.108029, 17.0180163, "plac Orląt Lwowskich 20B", "53-605",
        "Wrocław", "Polska");
    parkSzczLocation = createLocation(51.1114018, 17.0799816, "", "50-001", "Wrocław", "Polska");
  }

  private Location createLocation(Double latitude, Double longitude, String street, String zipCode,
      String city, String country) {
    var location = new Location();
    location.setLatitude(latitude);
    location.setLongitude(longitude);
    location.setStreet(street);
    location.setZipCode(zipCode);
    location.setCity(city);
    location.setCountry(country);
    return location;
  }

  private void createSights() {
    hp = createSight(hpImg, "Wycieczki Hello Poland we Wrocławiu",
        "Kupuj taniej, zwiedzaj łatwiej!",
        "Specjalizujemy się w obsłudze zorganizowanych grup turystycznych oraz biznesowych z kraju i zagranicy.",
        userHelloPoland.getPartner(), hpLocation);
    zooWro = createSight(zooImg, "ZOO we Wrocławiu", "Ogród Zoologiczny we Wrocławiu",
        "Ogród Zoologiczny we Wrocławiu – ogród zoologiczny znajdujący się przy ul. Wróblewskiego 1–5 we Wrocławiu, otwarty 10 lipca 1865. Jest najstarszym na obecnych ziemiach polskich ogrodem zoologicznym w Polsce. Powierzchnia ogrodu to 33 hektary.",
        userZoo.getPartner(), zooWroLocation);
    stadGd = createSight(stadGdImg, "Stadion w Gdańsku", "Stadion Energa Gdańsk",
        "Stadionowe atrakcje skupione są przede wszystkim w FUN ARENIE – stworzonym na ponad 9 tysiącach metrów kwadratowych na Stadionie Energa Gdańsk, tworzącym największy park rozrywki w Trójmieście, który rozszerza ofertę jednej z najpiękniejszych aren w Europie.\n"
            + "\n"
            + "Oprócz tego stadion zapewnia całą gamę dodatkowych atrakcji – zarówno płatnych jak i bezpłatnych. Multifunkcjonalność gdańskiego obiektu potwierdza nasza oferta przygotowana zarówno dla dzieci, młodzieży, rodzin jak i seniorów.",
        userStadionGd.getPartner(), stadGdLocation);
    kol = createSight(kol2Img, "Kolejkowo",
        "Odwiedź Kolejkowo i przeżyj z nami niezapomnianą przygodę!",
        "W Kolejkowie zabierzemy Cię w podróż przez miniaturowy świat. Twórcy Kolejkowa stworzyli ogromną makietę, atrakcję tętniącą życiem i przedstawiającą wspaniały Dolny Śląsk. Dołożyli wszelkich starań, aby jak najdokładniej odwzorować rzeczywistość i przedstawić życie miniaturowych mieszkańców Kolejkowa, ukazując ich w przeróżnych sytuacjach dnia codziennego. W Kolejkowie zobaczymy życie na wsi, potowarzyszymy turystom podczas górskich wypraw, weźmiemy udział w akcji ratunkowej, wcielimy się w leśniczych, narciarzy, kupców, pracowników budowlanych, cyrkowców czy plażowiczów.\n"
            + "\n"
            + "Atrakcja Kolejkowo mieści się na terenie jednego z najstarszych dworców kolejowych we Wrocławiu. Dworzec Świebodzki zlokalizowany jest w ścisłym centrum Wrocławia przy Placu Orląt Lwowskich, niespełna 800 m od wrocławskiego rynku. Odwiedzając miniaturowy świat, w tym wspaniałym późnoklasycystycznym obiekcie, zobaczyć można m.in. makiety obiektów z Wrocławia oraz całego Dolnego Śląska. Swoje miejsce znalazły tam takie perełki architektoniczne jak: Dworzec Świebodzki,  kamienice wrocławskiego rynku, obserwatorium meteorologiczne na Śnieżce, Karkonoskie Tajemnice w Karpaczu, schronisko Szwajcarka w Sudetach Zachodnich.",
        userKolejkowo.getPartner(), kolLocation);
  }

  private Sight createSight(ImageCollector mainImage, String name, String lead, String description,
      Partner partner, Location location) {
    var bo = new Sight();
    bo.setMainImage(mainImage);
    bo.setName(name);
    bo.setLead(lead);
    bo.setDescription(description);
    bo.setPartner(partner);
    bo.generateRandomScore();
    bo.setLocation(location);
    em.persist(bo);
    return bo;
  }

  private void createSightEvents() {
    afrEvent = createSightEvent(afrImg, "Wieczorne zwiedzanie Afrykarium",
        "Chcielibyście zobaczyć, co się dzieje w Afrykarium, gdy wyjdą ostatni zwiedzający? Hello Poland zaprasza na zwiedzanie Afrykarium po godzinach otwarcia obiektu! Jest to niepowtarzalna okazja, aby poznać ten unikatowy na skalę Polski obiekt bez tłumu turystów, w niedostępnej porze i w towarzystwie przewodnika, który zdradzi Państwu tajemnice tego fascynującego miejsca.\n"
            + "\n"
            + "Nasz przewodnik pomoże Państwu odkryć niezwykłe Afrykarium! Dowiecie się, dlaczego akrylowe szyby do akwariów sprowadzono aż z Japonii, jak powstawał osiemnastometrowy podwodny tunel oraz które zwierzęta były pierwszymi lokatorami Afrykarium. Te i wiele innych ciekawostek czeka na Państwa już 29 września (sobota) o godzinie 19:00.\n"
            + "\n"
            + "Podczas zwiedzania zaprosimy Państwa także na słodki poczęstunek do Laguna Bistro & Cafe. Skosztujecie Państwo kawy lub herbaty, a w ramach deseru zaserwujemy Państwu pyszne ciasto.\n"
            + "\n" + "Zwiedzanie w cenie 79 zł/os. obejmuje:\n"
            + "– wejście do Afrykarium po godzinach otwarcia obiektu,\n"
            + "– zwiedzanie Afrykarium z przewodnikiem,\n"
            + "– poczęstunek w Laguna Bistro & Cafe\n" + "Dzieci do lat 3 – wstęp wolny.",
        false, hp.getId(), zooWroLocation, userHelloPoland.getPartner());
    parkSzczEvent = createSightEvent(parkSzcz2Img,
        "Park Szczytnicki – najstarszy z wrocławskich parków",
        "Park Szczytnicki to największy i najstarszy z wrocławskich parków, najcenniejszy pod względem składu botanicznego, związany nierozerwalnie od XVIII wieku z losami miasta, jego ambicjami i tragediami. Znają go chyba wszyscy mieszkańcy Wrocławia, ale czy na pewno…? Przekonacie się podczas wycieczki w czwartek, 2 sierpnia o 17:30.\n"
            + "\n"
            + "Na terenie parku można odnaleźć wiele niezwykłych miejsc, zaskakujących budowli i wyjątkowych okazów roślin. Zapraszamy na spacer przez te „zielone płuca miasta”, a po drodze zobaczymy ponad 100 letnie alpinarium, przeprawimy się przez modernistyczny „wgłębnik”, odwiedzimy drewniany kościół skrywający mroczne tajemnice, poszukamy willi twórcy Hali Stulecia, dowiemy się do czego służą pneumatofory i spróbujemy zapolować na brytyjską wiewiórkę, oczywiście bezkrwawo.",
        false, hp.getId(), parkSzczLocation, userHelloPoland.getPartner());
    zwZooEvent = createSightEvent(zwZooImg, "Zwiedzanie ZOO",
        "Zakupiony bilet oraz karta roczna uprawniają do zwiedzania całego ZOO, w tym AFRYKARIUM. Wszystkie bilety uprawniają do jednorazowego wstępu.\n"
            + "\n" + "Bilet ulgowy, jednorazowy przysługuje:\n" + "\n"
            + "dzieciom od ukończenia 3. roku życia do 7 lat\n"
            + "uczniom szkół podstawowych, gimnazjów i szkół ponadgimnazjalnych do ukończenia 21. roku życia\n"
            + "dzieciom oraz młodzieży od 7 do 18 lat, nieobjętym obowiązkiem szkolnym z powodu choroby lub niepełnosprawności\n"
            + "emerytom i rencistom\n"
            + "osobom posiadającym orzeczenie Zespołu ds. Orzekania o Stopniu Niepełnosprawności lub Legitymację Osoby Niepełnosprawnej \n"
            + "opiekunowi osoby niepełnosprawnej (jednemu opiekunowi na jedną osobę niepełnosprawną) w przypadku wejścia wraz z podopieczną osobą, pod warunkiem, iż osoba niepełnosprawna, nad którą sprawuje opiekę posiada odpowiedni dokument potwierdzający konieczność stałej opieki drugiej osoby (są to dzieci do ukończenia 16 r.ż. niezależnie od stopnia niepełnosprawności oraz osoby ze znacznym stopniem niepełnosprawności). Za opiekuna uznaje się osobę, która posiada co najmniej ograniczoną zdolność do czynności prawnych, czyli która ukończyła 13 lat\n"
            + " warunkiem skorzystania z ulgi jest okazanie ważnej legitymacji, a w przypadku emerytów i rencistów dodatkowo dowodu osobistego lub Legitymacji Osoby Niepełnosprawnej\n"
            + "\n" + "Bilet rodzinny:\n"
            + "Do skorzystania z biletu rodzinnego uprawnione są dwie osoby dorosłe wraz z maksymalnie trójką podopiecznych (przysługują dzieciom, młodzieży szkolnej oraz osobom uczącym się w szkołach ponadgimnazjalnych do ukończenia 21 roku życia jak i  osobom studiującym do ukończenia 26 roku życia pod warunkiem, że przynajmniej jeden z opiekunów jest rodzicem studenta - podopiecznego).",
        true, zooWro.getId(), zooWroLocation, userZoo.getPartner());
    zwStadEvent = createSightEvent(zwStadImg, "Zwiedzanie stadionu",
        "Trasa zwiedzania obejmuje zakątki i lokalizacje, których nie zobaczymy przychodząc na mecz. Po zobaczeniu emocjonujących atrakcji FUN ARENY wycieczki poprowadzone zostaną do: szatni zawodników, sali konferencyjnej w której przed i po meczach występują trenerzy drużyn, kaplicy stadionowej, strefy mixed zone, czyli miejsca z którego zawodnicy wychodzą na boisko.\n"
            + "\n"
            + "Specjalnie ułożona trasa z miejscami niedostępnymi podczas standardowej wycieczki, możliwość domówienia cateringu, więcej informacji pod numerem telefonu 58 768 84 44, mail: tours@arenagdansk.com\n"
            + "\n"
            + "W ramach wzbogacenia naszej oferty zwiedzania stadionu od marca 2015 obligatoryjnym punktem na trasie wycieczki będzie Muzeum Lechii Gdańsk. Muzeum Lechii Gdańsk to historia nie tylko klubu piłkarskiego. Obejrzeć tu można eksponaty związane z futbolem, ale również innymi dyscyplinami – kolarstwem, podnoszeniem ciężarów, rugby, lekką atletyką czy tenisem – w których zawodnicy występowali na przestrzeni lat w barwach Lechii.\n"
            + "\n"
            + "Zwiedzanie stadionu z Arenką - maskotką FUN ARENY to gwarancja dobrej zabawy. Będzie towarzyszyła ona przewodnikowi podczas oprowadzania. Nasza maskotka przeprowadzi także rozgrzewkę w salce treningowej i na obrzeżach murawy, gdzie dzieci będą mogły zrobić wspólne zdjęcie z Arenką. Gwarantujemy dużo radości i śmiechu.\n"
            + "Czas trwania: 1 h. Wiek - minimum 4 lata.\n"
            + "Cena: 15 zł od dziecka, jeden opiekun na 10 podopiecznych: gratis.Oferta skierowana do grup powyżej 15 osób.",
        true, stadGd.getId(), stadGdLocation, userStadionGd.getPartner());
    meczPCEvent = createSightEvent(meczPCImg, "Mecz towarzyski Polska-Czechy",
        "Zapadła decyzja o organizacji jesiennych meczy towarzyskich Reprezentacji Polski. Biało-Czerwoni zmierzą się na Stadionie Energa Gdańsk z reprezentacją Czech 15 listopada 2018 r.\n"
            + "\n"
            + "Biało-Czerwoni zagrają na Stadionie Energa Gdańsk już po raz ósmy. Dotychczasowy bilans kadry na obiekcie w Letnicy wynosi dwa zwycięstwa, dwa remisy i trzy porażki. Po raz ostatnim reprezentacja Polski zagrała na gdańskim stadionie w listopadzie ubiegłego roku z Meksykiem.\n"
            + "\n"
            + "Spotkanie towarzyskie na Stadionie Energa Gdańsk będzie poprzedzało wyjazdowy mecz z Portugalią w ramach Ligi Narodów UEFA. Przypomnijmy, że Polska znajduje się w najwyżej sklasyfikowanej w rozgrywkach Dywizji A. Natomiast Czechy, na podstawie rankingu UEFA uplasowali się w Dywizji B.\n"
            + "\n"
            + "Listopadowy mecz z Czechami będzie 27 konfrontacją z naszym południowym sąsiadem. Historyczny bilans spotkań każe upatrywać w Czechach faworytów spotkania. Polacy zwyciężali dotychczas w ośmiu pojedynkach, Czesi w trzynastu grach, zaś w pięciu meczach padał wynik remisowy.\n"
            + "\n"
            + "Największymi gwiazdami współczesnej reprezentacji Czech są występujący w linii pomocy Antonin Barak z Udinese Calcio, oraz napastnik Romy Patrick Schick. Pierwszy z wymienionych piłkarzy to jeden z najlepszych strzelców drużyny z Udine. Urodzony w Pribramie zawodnik w bieżącej kampanii ligowej zdobył dla swojej drużyny sześć goli. Natomiast Patrick Schick zdobył dla klubu ze stolicy Włoch jedną bramkę w aktualnych rozgrywkach Serie A.\n"
            + "\n"
            + "Czechy w dotychczasowej historii zagrały zaledwie raz na Mistrzostwach Świata. Drużyna z takimi piłkarzami w składzie jak Jan Koller, Pavel Nedved, czy Petr Cech, wystąpiła na turnieju w Niemczech w 2006 roku. Zespół prowadzony przez Karela Brucknera, odpadł z turnieju już po fazie grupowej. Czesi zwyciężyli jedynie w pierwszym meczu ze Stanami Zjednoczonymi 3:0, natomiast w kolejnych przegrali dwukrotnie 0:2 z Ghaną i Włochami.",
        false, stadGd.getId(), stadGdLocation, userStadionGd.getPartner());
    kolEvent = createSightEvent(kol1Img, "Zwiedzanie Kolejkowa",
        "Czynne 365 dni w roku, również w niedziele i święta w godzinach 10:00–18:00.", true,
        kol.getId(), kolLocation, userKolejkowo.getPartner());
  }

  private SightEvent createSightEvent(ImageCollector mainImage, String name, String description,
      Boolean generalAdmission, Long sightId, Location location, Partner partner) {
    var dto = new SightEventDTO();
    dto.name = name;
    dto.mainImage = DtoMapper.getDTO(mainImage);
    dto.description = description;
    dto.generalAdmission = generalAdmission;
    dto.sightId = sightId;
    dto.location = DtoMapper.getDTO(location);
    return sService.create(dto, partner);
  }

  private void createTicketPoolDefinitions() {
    Date todayMidnight = new Date();
    todayMidnight.setSeconds(0);
    todayMidnight.setMinutes(0);
    todayMidnight.setHours(0);

    Date todaySecondToTommorow = new Date();
    todaySecondToTommorow.setHours(23);
    todaySecondToTommorow.setMinutes(59);
    todaySecondToTommorow.setSeconds(59);

    createTicketPoolDefinition("Wieczorne zwiedzanie Afrykarium", 25, false, todayMidnight,
        todaySecondToTommorow, afrEvent.getId(), userHelloPoland.getPartner(),
        createTicketDefinition("Normalny", 25, 7900, userHelloPoland.getPartner()));
    createTicketPoolDefinition("Park Szczytnicki", 15, false, todayMidnight, todaySecondToTommorow,
        parkSzczEvent.getId(), userHelloPoland.getPartner(),
        createTicketDefinition("Normalny", 15, 2900, userHelloPoland.getPartner()));
    createTicketPoolDefinition("Zwiedzanie ZOO", null, true, todayMidnight, todaySecondToTommorow,
        zwZooEvent.getId(), userZoo.getPartner(),
        createTicketDefinition("Normalny", null, 4500, userZoo.getPartner()),
        createTicketDefinition("Ulgowy", null, 3500, userZoo.getPartner()),
        createTicketDefinition("Dzieci", null, 0, userZoo.getPartner()),
        createTicketDefinition("Studencki", null, 4000, userZoo.getPartner()),
        createTicketDefinition("Rodzinny (dwoje dorosłych i max 3 dzieci)", null, 15000,
            userZoo.getPartner()));
    createTicketPoolDefinition("Zwiedzanie stadionu", null, true, todayMidnight,
        todaySecondToTommorow, zwStadEvent.getId(), userStadionGd.getPartner(),
        createTicketDefinition("Normalny", null, 1700, userStadionGd.getPartner()),
        createTicketDefinition("Ulgowy", null, 1200, userStadionGd.getPartner()),
        createTicketDefinition("Rodzinny (2+2)", null, 3600, userStadionGd.getPartner()));
    Date match = new Date();
    match.setHours(19);
    match.setMinutes(00);
    match.setSeconds(00);
    createTicketPoolDefinition("Mecz towarzyski Polska-Czechy", 50, false, match, match,
        meczPCEvent.getId(), userStadionGd.getPartner(),
        createTicketDefinition("Normalny", 40, 12500, userStadionGd.getPartner()),
        createTicketDefinition("VIP", 10, 24000, userStadionGd.getPartner()));
    createTicketPoolDefinition("Zwiedzanie Kolejkowa", null, true, todayMidnight,
        todaySecondToTommorow, kolEvent.getId(), userKolejkowo.getPartner(),
        createTicketDefinition("Normalny", null, 1900, userKolejkowo.getPartner()),
        createTicketDefinition("Ulgowy", null, 1500, userKolejkowo.getPartner()));
  }

  private void createTicketPoolDefinition(String name, Integer availableTicketsNumber,
      boolean cyclicalPool, Date startDate, Date endDate, Long sightEventId, Partner partner,
      TicketDefinitionDTO... ticketDefinitions) {
    var dto = new TicketPoolDefinitionDTO();
    dto.name = name;
    dto.availableTicketsNumber = availableTicketsNumber;
    dto.isCyclic = cyclicalPool;
    if (cyclicalPool) {
      dto.frequencyData = new FrequencyDataDTO();
      dto.frequencyData.frequencyType = FrequencyTypeDTO.DAILY;
      dto.frequencyData.frequency = 1;
      dto.frequencyData.startDate = startDate;
      Calendar cal = Calendar.getInstance();
      cal.set(Calendar.DAY_OF_YEAR, 365);
      dto.frequencyData.endDate = cal.getTime();
    }
    if (!cyclicalPool) {
      startDate.setMonth(startDate.getMonth() + 1);
      endDate.setMonth(endDate.getMonth() + 1);
    }
    dto.startDate = startDate;
    dto.endDate = endDate;
    dto.sightEventId = sightEventId;
    dto.ticketDefinitions = Arrays.asList(ticketDefinitions);
    tpdService.add(dto, partner);
  }

  private TicketDefinitionDTO createTicketDefinition(String name, Integer availableTicketsNumber,
      int price, Partner partner) {
    var dto = new TicketDefinitionDTO();
    dto.name = name;
    dto.availableTicketsNumber = availableTicketsNumber;
    dto.price = price;
    return tdService.add(dto, partner);
  }

}
