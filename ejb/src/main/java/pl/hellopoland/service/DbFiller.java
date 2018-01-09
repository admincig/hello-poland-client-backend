package pl.hellopoland.service;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import pl.hellopoland.model.Image;
import pl.hellopoland.model.Sight;
import pl.hellopoland.model.Ticket;

@Startup
@Singleton
public class DbFiller extends ServiceSuperclass {

  @Inject
  ImageService iService;


  @PostConstruct
  public void fillDb() {
    String hibernateStrategy = properties.get("hibernate.hbm2ddl.auto").toString();
    if (!hibernateStrategy.startsWith("create")) {
      logger.info("omitting dbfiller because hibernate.hbm2ddl.auto isnt set to create");
      return;
    }
    Image zooImage = iService.storeImage(getClass().getResourceAsStream("/zoo.jpg"), ".jpg");
    Image kolejkowoImage =
        iService.storeImage(getClass().getResourceAsStream("/kolejkowo.jpg"), ".jpg");
    Image hydropolisImage =
        iService.storeImage(getClass().getResourceAsStream("/hydropolis.jpg"), ".jpg");

    // Kolejkowo
    Sight kolejkowo = new Sight();
    kolejkowo.setName("Kolejkowo");
    kolejkowo.setMainImage(kolejkowoImage);
    kolejkowo.setScore(5.0f);
    kolejkowo.setMinPrice(500);
    kolejkowo.setLead("Cudowny świat w miniaturze!");
    kolejkowo.setDescription(
        "Największa w Polsce makieta kolejowa, z jeżdżącymi miniaturowymi pociągami i samochodami. Przedstawione tu zostały autentyczne budowle z terenu Śląska i Dolnego Śląska. Makiety przedstawiają miniaturowych mieszkańców (m.in. leśniczych, narciarzy, kupców, pracowników budowlanych, cyrkowców i plażowiczów) w różnych sytuacjach dnia codziennego; przedstawione jest życie na wsi, wyprawy wysokogórskie, akcje ratunkowe. To jedyna makieta w Polsce, na której pływa statek oraz miejscami symulowany jest padający deszcz. W dziewięciominutowym cyklu odwzorowane są zmiany pory dnia, po których następuje noc, zapalają się uliczne latarnie oraz światła w budynkach. Makiety prezentowane są we Wrocławiu oraz w Gliwicach.");
    em.persist(kolejkowo);
    Ticket kolejkowoTicket = new Ticket();
    kolejkowoTicket.setName("Normalny");
    kolejkowoTicket.setPrice(500);
    kolejkowoTicket.setSight(kolejkowo);
    em.persist(kolejkowoTicket);

    // ZOO
    Sight zoo = new Sight();
    zoo.setName("ZOO Wrocław");
    zoo.setMainImage(zooImage);
    zoo.setScore(4.9f);
    zoo.setMinPrice(2000);
    zoo.setLead("Czynne przez cały rok bez wyjątków.");
    zoo.setDescription(
        "Jest najstarszym na obecnych ziemiach polskich ogrodem zoologicznym w Polsce. Powierzchnia ogrodu to 33 hektary. Pod koniec 2015 wrocławskie Zoo prezentowało ponad 10 500 zwierząt (nie wliczając bezkręgowców) z 1132 gatunków (trzecie pod tym względem zoo na świecie). Jest piątym najchętniej odwiedzanym ogrodem zoologicznym w Europie.");
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

    // Hydropolis
    Sight hydropolis = new Sight();
    hydropolis.setName("Hydropolis");
    hydropolis.setMainImage(hydropolisImage);
    hydropolis.setScore(4.8f);
    hydropolis.setMinPrice(500);
    hydropolis.setLead("Ukazanie wody z różnych, fascynujących perspektyw.");
    hydropolis.setDescription(
        "Hydropolis to centrum wiedzy na temat wody, w którym w fascynujący sposób pokazane jest jej znaczenie dla człowieka. Wystawa zajmuje cztery tysiące metrów, w dawnych, ponad stuletnich zbiornikach czystej wody. Oryginalna scenografia oraz multimedia (ponad 60 monitorów dotykowych) sprawiają, że w Hydropolis nie można się nudzić, bo angażowane są niemal wszystkie zmysły.");
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

    logger.info("dbfiller finished");
  }
}
