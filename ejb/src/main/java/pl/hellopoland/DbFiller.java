package pl.hellopoland;

import java.time.LocalTime;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import pl.hellopoland.image.Image;
import pl.hellopoland.image.ImageService;
import pl.hellopoland.sight.Agreement;
import pl.hellopoland.sight.OpeningHours;
import pl.hellopoland.sight.Portal;
import pl.hellopoland.sight.Sight;
import pl.hellopoland.sight.SightLocation;
import pl.hellopoland.sight.SightService;
import pl.hellopoland.sight.Ticket;

@Startup
@Singleton
public class DbFiller extends ServiceSuperclass {

  @Inject
  ImageService iService;
  @Inject
  SightService sService;


  @PostConstruct
  public void fillDb() {
    String hibernateStrategy = properties.get("hibernate.hbm2ddl.auto").toString();
    if (!hibernateStrategy.startsWith("create")) {
      logger.info("omitting dbfiller because hibernate.hbm2ddl.auto isnt set to create");
      return;
    }

    createKolejkowo();
    createZoo();
    createHydropolis();
    createPortals();
    runImporter();

    logger.info("dbfiller finished");
  }


  private void runImporter() {
    sService.runImporter();
  }


  private void createPortals() {
    createWooHelloPoland();
  }


  private void createWooHelloPoland() {
    Portal portal = new Portal();
    portal.setName("Wycieczki Hello Poland");
    portal.setUrl("http://woo.hello-poland.pl");
    portal.setKey("ck_5233b79180ff8b7bef81b28fe7222b2eb2b37ebe");
    portal.setSecret("cs_2c96f574d729e8bde7b71d96007c172bc12244d9");
    em.persist(portal);
  }


  private void createKolejkowo() {
    Image kolejkowoImage =
        iService.storeImage(getClass().getResourceAsStream("/kolejkowo.jpg"), ".jpg");

    Sight kolejkowo = new Sight();
    kolejkowo.setName("Kolejkowo");
    kolejkowo.setMainImage(kolejkowoImage);
    kolejkowo.setScore(5.0f);
    kolejkowo.setMinPrice(500);
    kolejkowo.setLead("Cudowny świat w miniaturze!");
    kolejkowo.setDescription(
        "Największa w Polsce makieta kolejowa, z jeżdżącymi miniaturowymi pociągami i samochodami. Przedstawione tu zostały autentyczne budowle z terenu Śląska i Dolnego Śląska. Makiety przedstawiają miniaturowych mieszkańców (m.in. leśniczych, narciarzy, kupców, pracowników budowlanych, cyrkowców i plażowiczów) w różnych sytuacjach dnia codziennego; przedstawione jest życie na wsi, wyprawy wysokogórskie, akcje ratunkowe. To jedyna makieta w Polsce, na której pływa statek oraz miejscami symulowany jest padający deszcz. W dziewięciominutowym cyklu odwzorowane są zmiany pory dnia, po których następuje noc, zapalają się uliczne latarnie oraz światła w budynkach. Makiety prezentowane są we Wrocławiu oraz w Gliwicach.");
    kolejkowo.setEmail("biuro@kolejkowo.pl");
    kolejkowo.setPhone("880008004");
    SightLocation location = new SightLocation();
    location.setStreet("pl. Orląt Lwowskich 20B");
    location.setZipCode("53-605");
    location.setCity("Wrocław");
    location.setLatitude(51.108208);
    location.setLongitude(17.019586);
    location.setCountry("Polska");
    kolejkowo.setLocation(location);
    em.persist(kolejkowo);
    Ticket kolejkowoTicket = new Ticket();
    kolejkowoTicket.setName("Normalny");
    kolejkowoTicket.setPrice(500);
    kolejkowoTicket.setSight(kolejkowo);
    em.persist(kolejkowoTicket);
    OpeningHours oh = null;
    for (int i = 1; i < 8; i++) {
      oh = new OpeningHours();
      oh.setDay(i);
      oh.setOpenTime(LocalTime.of(10, 00));
      oh.setCloseTime(LocalTime.of(18, 00));
      oh.setSight(kolejkowo);
      em.persist(oh);
    }
    Agreement ag = new Agreement();
    ag.setSight(kolejkowo);
    ag.setLinkUrl("http://hello-poland.com.pl/regulamin");
    em.persist(ag);
  }


  private void createZoo() {
    Image zooImage = iService.storeImage(getClass().getResourceAsStream("/zoo.jpg"), ".jpg");

    Sight zoo = new Sight();
    zoo.setName("ZOO Wrocław");
    zoo.setMainImage(zooImage);
    zoo.setScore(4.9f);
    zoo.setMinPrice(2000);
    zoo.setLead("Czynne przez cały rok bez wyjątków.");
    zoo.setDescription(
        "Jest najstarszym na obecnych ziemiach polskich ogrodem zoologicznym w Polsce. Powierzchnia ogrodu to 33 hektary. Pod koniec 2015 wrocławskie Zoo prezentowało ponad 10 500 zwierząt (nie wliczając bezkręgowców) z 1132 gatunków (trzecie pod tym względem zoo na świecie). Jest piątym najchętniej odwiedzanym ogrodem zoologicznym w Europie.");
    zoo.setEmail("pok@zoo.wroc.pl");
    zoo.setPhone("713483024");
    SightLocation location = new SightLocation();
    location.setStreet("ul. Wróblewskiego 1-5");
    location.setZipCode("51-618");
    location.setCity("Wrocław");
    location.setLatitude(51.105637);
    location.setLongitude(17.076222);
    location.setCountry("Polska");
    zoo.setLocation(location);
    em.persist(zoo);
    Ticket zooTicket = new Ticket();
    zooTicket.setName("Normalny");
    zooTicket.setPrice(4000);
    zooTicket.setSight(zoo);
    em.persist(zooTicket);
    zooTicket = new Ticket();
    zooTicket.setName("Ulgowy");
    zooTicket.setPrice(2500);
    zooTicket.setSight(zoo);
    em.persist(zooTicket);
    zooTicket = new Ticket();
    zooTicket.setName("Grupowy (min. 10 osób)");
    zooTicket.setPrice(2000);
    zooTicket.setSight(zoo);
    em.persist(zooTicket);
    OpeningHours oh = null;
    for (int i = 1; i < 5; i++) {
      oh = new OpeningHours();
      oh.setDay(i);
      oh.setOpenTime(LocalTime.of(9, 00));
      oh.setCloseTime(LocalTime.of(15, 00));
      oh.setSight(zoo);
      em.persist(oh);
    }
    for (int i = 5; i < 8; i++) {
      oh = new OpeningHours();
      oh.setDay(i);
      oh.setOpenTime(LocalTime.of(9, 00));
      oh.setCloseTime(LocalTime.of(16, 00));
      oh.setSight(zoo);
      em.persist(oh);
    }
    Agreement ag = new Agreement();
    ag.setSight(zoo);
    ag.setLinkUrl("http://hello-poland.com.pl/regulamin");
    em.persist(ag);
  }


  private void createHydropolis() {
    Image hydropolisImage =
        iService.storeImage(getClass().getResourceAsStream("/hydropolis.jpg"), ".jpg");

    Sight hydropolis = new Sight();
    hydropolis.setName("Hydropolis");
    hydropolis.setMainImage(hydropolisImage);
    hydropolis.setScore(4.8f);
    hydropolis.setMinPrice(500);
    hydropolis.setLead("Ukazanie wody z różnych, fascynujących perspektyw.");
    hydropolis.setDescription(
        "Hydropolis to centrum wiedzy na temat wody, w którym w fascynujący sposób pokazane jest jej znaczenie dla człowieka. Wystawa zajmuje cztery tysiące metrów, w dawnych, ponad stuletnich zbiornikach czystej wody. Oryginalna scenografia oraz multimedia (ponad 60 monitorów dotykowych) sprawiają, że w Hydropolis nie można się nudzić, bo angażowane są niemal wszystkie zmysły.");
    hydropolis.setEmail("biuro@hydropolis.pl");
    hydropolis.setPhone("713409515");
    SightLocation location = new SightLocation();
    location.setStreet("Na Grobli 19-21");
    location.setZipCode("50-001");
    location.setCity("Wrocław");
    location.setLatitude(51.103833);
    location.setLongitude(17.057380);
    location.setCountry("Polska");
    hydropolis.setLocation(location);
    em.persist(hydropolis);
    Ticket hydropolisTicket = new Ticket();
    hydropolisTicket.setName("Normalny");
    hydropolisTicket.setPrice(1000);
    hydropolisTicket.setSight(hydropolis);
    em.persist(hydropolisTicket);
    hydropolisTicket = new Ticket();
    hydropolisTicket.setName("Ulgowy");
    hydropolisTicket.setPrice(500);
    hydropolisTicket.setSight(hydropolis);
    em.persist(hydropolisTicket);
    OpeningHours oh = null;
    for (int i = 1; i < 6; i++) {
      oh = new OpeningHours();
      oh.setDay(i);
      oh.setOpenTime(LocalTime.of(9, 00));
      oh.setCloseTime(LocalTime.of(18, 00));
      oh.setSight(hydropolis);
      em.persist(oh);
    }
    for (int i = 6; i < 8; i++) {
      oh = new OpeningHours();
      oh.setDay(i);
      oh.setOpenTime(LocalTime.of(10, 00));
      oh.setCloseTime(LocalTime.of(20, 00));
      oh.setSight(hydropolis);
      em.persist(oh);
    }
    Agreement ag = new Agreement();
    ag.setSight(hydropolis);
    ag.setLinkUrl("http://hello-poland.com.pl/regulamin");
    em.persist(ag);
  }
}
