package pl.hellopoland.util;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import pl.hellopoland.bo.ImageCollector;
import pl.hellopoland.bo.Location;
import pl.hellopoland.bo.Order;
import pl.hellopoland.bo.OrderDateEntry;
import pl.hellopoland.bo.OrderDetails;
import pl.hellopoland.bo.OrderEntry;
import pl.hellopoland.bo.OrderSightEntry;
import pl.hellopoland.bo.Sight;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.TicketDefinition;
import pl.hellopoland.dto.ImageDTO;
import pl.hellopoland.dto.LocationDTO;
import pl.hellopoland.dto.P24PassageCartDTO;
import pl.hellopoland.dto.P24PassageCartEntryDTO;
import pl.hellopoland.dto.P24PassageTransactionParamsDTO;
import pl.hellopoland.dto.SightDTO;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.dto.TicketDefinitionDTO;

public class DtoMapper {

  private static final Properties PROPERTIES = System.getProperties();

  public static void copy(SightDTO source, Sight target) {
    target.setName(source.name);
    target.setLead(source.lead);
    target.setDescription(source.description);
    target.setEmail(source.email);
    target.setPhone(source.phone);

    copyLocation(source.location, target);
  }

  public static SightDTO getDTO(Sight bo) {
    SightDTO dto = new SightDTO();

    dto.id = bo.getId();
    dto.name = bo.getName();
    dto.lead = bo.getLead();
    dto.description = bo.getDescription();
    dto.mainImage = bo.getMainImage() != null ? getDTO(bo.getMainImage()) : null;
    dto.location = ofNullable(bo.getLocation()).map(DtoMapper::getDTO).orElse(null);
    dto.email = bo.getEmail();
    dto.phone = bo.getPhone();
    dto.score = bo.getScore();

    return dto;
  }

  public static SightDTO getFullDTO(Sight bo) {
    SightDTO dto = getDTO(bo);
    if (bo.getSightEvents() != null) {
      dto.sightEvents = bo.getSightEvents().stream().map(DtoMapper::getFullDTO).collect(toList());
    }
    if (bo.getImages() != null && !bo.getImages().isEmpty()) {
      dto.images = bo.getImages().stream().map(DtoMapper::getDTO).collect(toList());
    }
    return dto;
  }

  public static SightEventDTO getDTO(SightEvent bo) {
    SightEventDTO dto = new SightEventDTO();

    dto.id = bo.getId();
    dto.name = bo.getName();
    dto.lead = bo.getLead();
    dto.description = bo.getDescription();
    dto.mainImage = bo.getMainImage() != null ? getDTO(bo.getMainImage()) : null;
    dto.email = bo.getEmail();
    dto.phone = bo.getPhone();
    dto.duration = bo.getDuration();
    dto.location = ofNullable(bo.getLocation()).map(DtoMapper::getDTO).orElse(null);
    // dto.date = bo.getDate();
    dto.generalAdmission = bo.getGeneralAdmission();
    dto.score = bo.getScore();
    dto.sightId = bo.getSight().getId();

    return dto;
  }

  public static SightEventDTO getFullDTO(SightEvent bo) {
    SightEventDTO dto = getDTO(bo);
    if (bo.getImages() != null && !bo.getImages().isEmpty()) {
      dto.images = bo.getImages().stream().map(DtoMapper::getDTO).collect(toList());
    }
    // if (bo.getTickets() != null && !bo.getTickets().isEmpty()) {
    // dto.ticketDefinitions = bo.getTickets().stream().map(DtoMapper::getDTO).collect(toList());
    // }
    // if (bo.getOpeningHours() != null && !bo.getOpeningHours().isEmpty()) {
    // dto.openingHours = bo.getOpeningHours().stream().map(DtoMapper::getDTO).collect(toList());
    // }
    // if (bo.getAgreements() != null && !bo.getAgreements().isEmpty()) {
    // dto.agreements = bo.getAgreements().stream().map(DtoMapper::getDTO).collect(toList());
    // }
    return dto;
  }

  // private static AgreementDTO getDTO(Agreement bo) {
  // var dto = new AgreementDTO();
  //
  // dto.name = ofNullable(bo.getSightEvent()).map(SightEvent::getName).orElse(null);
  // dto.url = bo.getLinkUrl();
  //
  // return dto;
  // }
  //
  // private static pl.hellopoland.dto.OpeningHours getDTO(OpeningHours bo) {
  // var dto = new pl.hellopoland.dto.OpeningHours();
  //
  // dto.day = bo.getDay();
  // dto.openTime = bo.getOpenTime();
  // dto.closeTime = bo.getCloseTime();
  //
  // return dto;
  // }

  public static LocationDTO getDTO(Location bo) {
    LocationDTO dto = new LocationDTO();

    dto.latitude = bo.getLatitude();
    dto.longitude = bo.getLongitude();
    dto.street = bo.getStreet();
    dto.zipCode = bo.getZipCode();
    dto.city = bo.getCity();
    dto.country = bo.getCountry();

    return dto;
  }

  public static TicketDefinitionDTO getDTO(TicketDefinition bo) {
    TicketDefinitionDTO dto = new TicketDefinitionDTO();

    dto.id = bo.getId();
    dto.name = bo.getName();
    dto.price = bo.getPrice();
    // dto.sightEventId = bo.getSightEvent().getId();
    dto.availableTicketsNumber = bo.getAvailableTicketsNumber();

    return dto;
  }

  public static ImageDTO getDTO(ImageCollector bo) {
    if (bo == null) {
      return null;
    }
    ImageDTO dto = new ImageDTO();
    dto.original = bo.getOrginal().getDownloadUrl();
    dto.fhd = bo.getFhd().getDownloadUrl();
    dto.fourK = bo.getFourK().getDownloadUrl();
    dto.hd = bo.getHd().getDownloadUrl();
    dto.qvg = bo.getQvga().getDownloadUrl();
    dto.sxga = bo.getSxga().getDownloadUrl();
    dto.vga = bo.getVga().getDownloadUrl();
    dto.xga = bo.getXga().getDownloadUrl();
    return dto;
  }

  private static void copyLocation(LocationDTO source, Located target) {
    Location location = target.getLocation();

    if (source != null) {
      if (location == null) {
        location = new Location();
      }
      location.setLatitude(source.latitude);
      location.setLongitude(source.longitude);
      location.setStreet(source.street);
      location.setZipCode(source.zipCode);
      location.setCity(source.city);
      location.setCountry(source.country);

      target.setLocation(location);
    } else {
      target.setLocation(null);
    }
  }

  public static SightEventDTO getGAEventDTO(Sight bo) {
    SightEventDTO dto = new SightEventDTO();
    dto.sightId = bo.getId();
    dto.name = bo.getName();
    dto.description = bo.getDescription();
    dto.mainImage = getDTO(bo.getMainImage());
    dto.email = bo.getEmail();
    dto.phone = bo.getPhone();
    dto.lead = bo.getLead();
    dto.location = ofNullable(bo.getLocation()).map(DtoMapper::getDTO).orElse(null);
    dto.generalAdmission = true;
    return dto;
  }

  public static void copy(SightEventDTO source, SightEvent target) {
    target.setName(source.name);
    target.setLead(source.lead);
    target.setDescription(source.description);
    target.setEmail(source.email);
    target.setPhone(source.phone);
    target.setGeneralAdmission(source.generalAdmission);
    target.setHptId(source.id);

    copyLocation(source.location, target);
  }

  public static void copy(TicketDefinitionDTO source, TicketDefinition target) {
    target.setExternalId(source.id);
    target.setName(source.name);
    target.setPoolId(source.poolId);
    target.setPrice(source.price);
    target.setAvailableTicketsNumber(source.availableTicketsNumber);
  }

  public static P24PassageCartDTO getP24PassageCartDTO(Order o) {
    var passageCart = new ArrayList<P24PassageCartEntryDTO>();
    gatherOrderEntries(o.getEntries().stream().collect(Collectors.toList())).forEach(oe -> {
      var cartEntry = getP24PassageCartEntryDTO(oe);
      cartEntry.description = "Hello Poland, " + o.getHash();
      passageCart.add(cartEntry);
    });
    var p24Params = getP24PassageTransactionParamsDTO(o);
    p24Params.amount = passageCart.stream().collect(Collectors.summingInt(f -> f.targetAmount));
    p24Params.passageCart = passageCart;
    p24Params.sign = getP24Sign(p24Params);
    var dto = new P24PassageCartDTO();
    dto.isSandbox = Boolean.parseBoolean(PROPERTIES.getProperty("przelewy24.isSandbox"));
    dto.transactionParams = p24Params;
    return dto;
  }

  public static P24PassageCartEntryDTO getP24PassageCartEntryDTO(OrderEntry oe) {
    var dto = new P24PassageCartEntryDTO();
    dto.name = oe.getName();
    dto.number = oe.getExternalId();
    dto.price = oe.getUnitPrice();
    dto.quantity = oe.getQuantity();
    dto.targetAmount = oe.getUnitPrice() * oe.getQuantity();
    dto.targetPosId = oe.getDateEntry().getSightEntry().getSightEvent().getPartner().getP24Id();
    return dto;
  }

  public static P24PassageTransactionParamsDTO getP24PassageTransactionParamsDTO(Order o) {
    var dto = new P24PassageTransactionParamsDTO();
    OrderDetails od = o.getDetails();
    dto.address = "";
    dto.city = od.getCity();
    dto.client = od.getFirstName() + " " + od.getLastName();
    dto.country = od.getCountry();
    dto.currency = "PLN";
    dto.email = od.getEmail();
    dto.language = "pl";
    dto.phone = od.getPhone();
    dto.sessionId = o.getHash();
    dto.zip = "";
    dto.description = "Market App, " + o.getHash();
    dto.merchantId = Integer.valueOf(PROPERTIES.getProperty("przelewy24.merchantId"));
    dto.urlStatus = getAckPaymentURL(o);
    return dto;
  }

  private static String getP24Sign(P24PassageTransactionParamsDTO dto) {
    var delimiter = "|";
    var signBuilder = new StringBuilder();
    signBuilder.append(dto.sessionId).append(delimiter);
    signBuilder.append(dto.merchantId).append(delimiter);
    signBuilder.append(dto.amount).append(delimiter);
    signBuilder.append(dto.currency).append(delimiter);
    signBuilder.append(PROPERTIES.getProperty("przelewy24.crc"));
    return PaymentUtils.MD5(signBuilder.toString());
  }

  private static List<OrderEntry> gatherOrderEntries(List<OrderSightEntry> list) {
    List<OrderEntry> returnList = new ArrayList<>();
    for (OrderSightEntry se : list) {
      for (OrderDateEntry de : se.getEntries()) {
        returnList.addAll(de.getEntries());
      }
    }
    return returnList;
  }

  private static String getAckPaymentURL(Order o) {
    var url = PROPERTIES.getProperty("base.url");
    if (!url.endsWith("/")) {
      url = url.concat("/");
    }
    return url.concat("market/orders/" + o.getHash() + "/ackPayment");
  }

}
