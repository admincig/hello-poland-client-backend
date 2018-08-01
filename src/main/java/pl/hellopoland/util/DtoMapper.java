package pl.hellopoland.util;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import pl.hellopoland.bo.ImageCollector;
import pl.hellopoland.bo.Location;
import pl.hellopoland.bo.Sight;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.TicketDefinition;
import pl.hellopoland.dto.DateTypeDTO;
import pl.hellopoland.dto.ImageDTO;
import pl.hellopoland.dto.LocationDTO;
import pl.hellopoland.dto.SightDTO;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.dto.TicketDefinitionDTO;

public class DtoMapper {

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

    return dto;
  }

  public static SightDTO getFullDTO(Sight bo) {
    SightDTO dto = getDTO(bo);
    if (bo.getSightEvents() != null) {
      dto.sightEvents = bo.getSightEvents().stream().map(DtoMapper::getFullDTO).collect(toList());
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
    dto.sightId = bo.getSight().getId();

    return dto;
  }

  public static SightEventDTO getFullDTO(SightEvent bo) {
    SightEventDTO dto = getDTO(bo);
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

  private static LocationDTO getDTO(Location bo) {
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
    // dto.predefinedDate = bo.isPredefinedDate();
    // dto.date = bo.getDate();
    dto.dateType = DateTypeDTO.valueOf(bo.getDateType().name());
    // dto.sightEventId = bo.getSightEvent().getId();
    dto.availableTicketsNumber = bo.getAvailableTicketsNumber();

    return dto;
  }

  private static ImageDTO getDTO(ImageCollector bo) {
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
    dto.location = ofNullable(bo.getLocation()).map(DtoMapper::getDTO).orElse(null);
    dto.generalAdmission = true;
    return dto;
  }

  public static void copy(SightEventDTO source, SightEvent target) {
    target.setName(source.name);
    // target.setDate(source.date);
    target.setLead(source.lead);
    target.setDescription(source.description);
    target.setEmail(source.email);
    target.setPhone(source.phone);
    target.setGeneralAdmission(source.generalAdmission);
    target.setHptId(source.id);

    copyLocation(source.location, target);
  }

  public static void copy(TicketDefinitionDTO source, TicketDefinition target) {
    // target.setDate(source.date);
    target.setDateType(pl.hellopoland.bo.DateType.valueOf(source.dateType.name()));
    target.setExternalId(source.id);
    target.setName(source.name);
    target.setPoolId(source.ticketPoolId);
    // target.setPredefinedDate(source.predefinedDate);
    target.setPrice(source.price);
    target.setAvailableTicketsNumber(source.availableTicketsNumber);
  }
}
