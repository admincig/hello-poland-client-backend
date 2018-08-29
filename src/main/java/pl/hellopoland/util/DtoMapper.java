package pl.hellopoland.util;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import pl.hellopoland.bo.ImageCollector;
import pl.hellopoland.bo.Location;
import pl.hellopoland.bo.OpeningHours;
import pl.hellopoland.bo.Sight;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.Ticket;
import pl.hellopoland.dto.ImageDTO;
import pl.hellopoland.dto.LocationDTO;
import pl.hellopoland.dto.OpeningHoursDTO;
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

  public static OpeningHours copy(OpeningHoursDTO source, OpeningHours target) {
    target.setCloseTime(source.closeTime);
    target.setDay(source.day);
    target.setOpenTime(source.openTime);
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
    if (bo.getOpeningHours() != null && !bo.getOpeningHours().isEmpty()) {
      dto.openingHours = bo.getOpeningHours().stream().map(DtoMapper::getDTO).collect(toList());
    }
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

  public static TicketDefinitionDTO getDTO(Ticket bo) {
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

  public static void copy(TicketDefinitionDTO source, Ticket target) {
    target.setExternalId(source.id);
    target.setName(source.name);
    target.setPoolId(source.poolId);
    target.setPrice(source.price);
    target.setAvailableTicketsNumber(source.availableTicketsNumber);
  }

}
