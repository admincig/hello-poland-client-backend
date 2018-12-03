package pl.hellopoland.util;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;
import pl.hellopoland.bo.Agreement;
import pl.hellopoland.bo.FileDescriptor;
import pl.hellopoland.bo.ImageCollector;
import pl.hellopoland.bo.Location;
import pl.hellopoland.bo.OpeningHours;
import pl.hellopoland.bo.Order;
import pl.hellopoland.bo.OrderDateEntry;
import pl.hellopoland.bo.OrderDetails;
import pl.hellopoland.bo.OrderEntry;
import pl.hellopoland.bo.OrderSightEntry;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.bo.Sight;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.TicketDefinition;
import pl.hellopoland.bo.User;
import pl.hellopoland.bo.UserRole;
import pl.hellopoland.dto.AgreementDTO;
import pl.hellopoland.dto.FileDescriptorDTO;
import pl.hellopoland.dto.ImageDTO;
import pl.hellopoland.dto.LocationDTO;
import pl.hellopoland.dto.OpeningHoursDTO;
import pl.hellopoland.dto.P24PassageCartDTO;
import pl.hellopoland.dto.P24PassageCartEntryDTO;
import pl.hellopoland.dto.P24PassageTransactionParamsDTO;
import pl.hellopoland.dto.PartnerDTO;
import pl.hellopoland.dto.RoleDTO;
import pl.hellopoland.dto.SightDTO;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.dto.TicketDefinitionDTO;
import pl.hellopoland.dto.UserDTO;

public class DtoMapper {

  private static final Properties PROPERTIES = System.getProperties();

  public static void copy(SightDTO source, Sight target) {
    target.setName(source.name);
    target.setLead(source.lead);
    target.setDescription(source.description);
    target.setEmail(source.email);
    target.setPhone(source.phone);
    target.setScore(source.score);
    if (source.blocked != null) {
      target.setBlocked(source.blocked);
    }
    if (source.published != null) {
      target.setPublished(source.published);
    }
    copyLocation(source.location, target);
  }

  public static OpeningHours copy(OpeningHoursDTO source, OpeningHours target) {
    target.setCloseTime(source.closeTime);
    target.setDay(source.day);
    target.setOpenTime(source.openTime);
    return target;
  }

  public static Agreement copy(AgreementDTO source, Agreement target) {
    if (source.linkUrl != null && !source.linkUrl.isEmpty()) {
      target.setLinkUrl(source.linkUrl);
    }
    if (source.text != null && !source.text.isEmpty()) {
      target.setText(source.text);
    }
    if (source.obligatory != null) {
      target.setObligatory(source.obligatory);
    }
    return target;
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
    dto.blocked = bo.isBlocked();
    dto.published = bo.isPublished();
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
    if (bo.getOpeningHours() != null && !bo.getOpeningHours().isEmpty()) {
      dto.openingHours = bo.getOpeningHours().stream().map(DtoMapper::getDTO).collect(toList());
    }
    if (bo.getAgreements() != null && !bo.getAgreements().isEmpty()) {
      dto.agreements = bo.getAgreements().stream().map(DtoMapper::getDTO).collect(toList());
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
    dto.sightId = bo.getSight() != null ? bo.getSight().getId() : null;
    dto.blocked = bo.isBlocked();
    dto.published = bo.isPublished();
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
    if (bo.getOpeningHours() != null && !bo.getOpeningHours().isEmpty()) {
      dto.openingHours = bo.getOpeningHours().stream().map(DtoMapper::getDTO).collect(toList());
    }
    if (bo.getAgreements() != null && !bo.getAgreements().isEmpty()) {
      dto.agreements = bo.getAgreements().stream().map(DtoMapper::getDTO).collect(toList());
    }
    dto.pdfAttachment = bo.getPdfAttachment() != null ? getDTO(bo.getPdfAttachment()) : null;
    return dto;
  }

  public static FileDescriptorDTO getDTO(FileDescriptor bo) {
    var dto = new FileDescriptorDTO();
    dto.id = bo.getId();
    dto.created = bo.getCreated();
    dto.type = bo.getMimeType().toString();
    return dto;
  }

  public static FileDescriptorDTO getFullDTO(FileDescriptor bo) {
    var dto = getDTO(bo);
    dto.path = bo.getPath();
    return dto;
  }

  private static OpeningHoursDTO getDTO(OpeningHours bo) {
    var dto = new OpeningHoursDTO();
    dto.sight = ofNullable(bo.getSight()).map(DtoMapper::getDTO).orElse(null);
    dto.sightEvent = ofNullable(bo.getSightEvent()).map(DtoMapper::getDTO).orElse(null);
    dto.day = bo.getDay();
    dto.openTime = bo.getOpenTime();
    dto.closeTime = bo.getCloseTime();
    return dto;
  }

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
    target.setScore(source.score);
    if (source.blocked != null) {
      target.setBlocked(source.blocked);
    }
    if (source.published != null) {
      target.setPublished(source.published);
    }
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
    gatherOrderEntries(o.getEntries()).forEach(oe -> {
      var cartEntry = getP24PassageCartEntryDTO(oe);
      cartEntry.description = "Hello Poland, " + o.getHash();
      passageCart.add(cartEntry);
    });
    var p24Params = getP24PassageTransactionParamsDTO(o);
    p24Params.amount =
        passageCart.stream().collect(Collectors.summingInt(f -> f.price * f.quantity));
    p24Params.sign = getP24Sign(p24Params);
    var dto = new P24PassageCartDTO();
    dto.isSandbox = Boolean.parseBoolean(PROPERTIES.getProperty("przelewy24.isSandbox"));
    dto.transactionParams = p24Params;
    // passageCart.add(getHPCommissionPassageCart(p24Params.amount, passageCart, o.getHash()));
    p24Params.passageCart = organizeByPosId(passageCart);
    // p24Params.passageCart = passageCart;
    return dto;
  }

  private static ArrayList<P24PassageCartEntryDTO> organizeByPosId(
      ArrayList<P24PassageCartEntryDTO> passageCart) {
    var cart = new ArrayList<P24PassageCartEntryDTO>();
    passageCart.stream().collect(Collectors.groupingBy(c -> c.targetPosId)).forEach((k, v) -> {
      if (v.size() == 1) {
        cart.add(v.get(0));
      } else {
        var c = new P24PassageCartEntryDTO();
        c.description = v.get(0).description;
        c.targetPosId = k;
        c.name = "tickets";
        c.quantity = 1;
        c.price = v.stream().collect(Collectors.summingInt(cdto -> cdto.price));
        c.targetAmount = v.stream().collect(Collectors.summingInt(cdto -> cdto.targetAmount));
        cart.add(c);
      }
    });
    return cart;
  }

  private static P24PassageCartEntryDTO getHPCommissionPassageCart(Integer amount,
      ArrayList<P24PassageCartEntryDTO> passageCart, String orderHash) {
    var dto = new P24PassageCartEntryDTO();
    dto.name = "HP prowizja - orderHash";
    dto.quantity = 1;
    dto.targetAmount =
        amount - passageCart.stream().collect(Collectors.summingInt(f -> f.targetAmount));
    dto.price = dto.targetAmount;
    dto.targetPosId = Integer.parseInt(PROPERTIES.getProperty("przelewy24.posId"));
    return dto;
  }

  public static P24PassageCartEntryDTO getP24PassageCartEntryDTO(OrderEntry oe) {
    var dto = new P24PassageCartEntryDTO();
    dto.name = oe.getName();
    dto.number = oe.getExternalId();
    dto.quantity = 1;
    // dto.quantity = oe.getQuantity();
    dto.targetAmount = getTargetAmount(oe);
    dto.price = dto.targetAmount;
    // dto.price = getUnitPrice(dto.quantity, dto.targetAmount);
    dto.targetPosId = oe.getDateEntry().getSightEntry().getSightEvent().getPartner().getP24Id();
    return dto;
  }

  private static Integer getUnitPrice(Integer quantity, Integer targetAmount) {
    return new BigDecimal(targetAmount).divide(new BigDecimal(quantity))
        .setScale(0, RoundingMode.HALF_EVEN).intValue();
  }

  private static Integer getTargetAmount(OrderEntry oe) {
    return oe.getUnitPrice() * oe.getQuantity();
    // var total = new BigDecimal(oe.getUnitPrice() * oe.getQuantity());
    // var hundred = new BigDecimal("100");
    // var commission = hundred
    // .subtract(oe.getDateEntry().getSightEntry().getSightEvent().getPartner().getCommission())
    // .divide(new BigDecimal("100"));
    // return total.multiply(commission).setScale(0, RoundingMode.HALF_EVEN).intValue();
  }

  public static P24PassageTransactionParamsDTO getP24PassageTransactionParamsDTO(Order o) {
    var dto = new P24PassageTransactionParamsDTO();
    OrderDetails od = o.getDetails();
    dto.address = "";
    dto.city = od.getCity() != null ? od.getCity() : "";
    dto.client = (od.getFirstName() == null && od.getLastName() == null) ? ""
        : od.getFirstName() + " " + od.getLastName();
    dto.country = "PL";
    dto.currency = "PLN";
    dto.email = od.getEmail() != null ? od.getEmail() : "";
    dto.language = "pl";
    dto.phone = od.getPhone() != null ? od.getPhone() : "";
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

  private static List<OrderEntry> gatherOrderEntries(Collection<OrderSightEntry> collection) {
    List<OrderEntry> returnList = new ArrayList<>();
    for (OrderSightEntry se : collection) {
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

  public static AgreementDTO getDTO(Agreement bo) {
    var dto = new AgreementDTO();
    dto.id = bo.getId();
    dto.linkUrl = bo.getLinkUrl();
    dto.obligatory = bo.isObligatory();
    dto.text = bo.getText();
    return dto;
  }

  public static RoleDTO getDTO(UserRole bo) {
    return RoleDTO.valueOf(bo.getRole().name());
  }

  public static UserDTO getDTO(User bo) {
    var dto = new UserDTO();
    dto.id = bo.getId();
    dto.name = bo.getName();
    dto.email = bo.getEmail();
    dto.roles = Optional.ofNullable(bo.getRoles()).orElse(Collections.emptyList()).stream()
        .map(DtoMapper::getDTO).collect(Collectors.toSet());
    return dto;
  }

  public static PartnerDTO getDTO(Partner bo) {
    var dto = new PartnerDTO();
    dto.id = bo.getId();
    dto.name = bo.getName();
    dto.p24MerchantId = bo.getP24Id();
    dto.commission = bo.getCommission();
    dto.users = Optional.ofNullable(bo.getUsers()).orElse(Collections.emptyList()).stream()
        .map(DtoMapper::getDTO).collect(Collectors.toList());
    // dto.sightEvents = ;
    // dto.agreements = ;
    return dto;
  }

}
