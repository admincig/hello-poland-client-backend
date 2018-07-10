package pl.hellopoland.util;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import pl.hellopoland.dto.DateType;
import pl.hellopoland.image.Image;
import pl.hellopoland.sight.Location;
import pl.hellopoland.sight.Sight;
import pl.hellopoland.sight.SightEvent;
import pl.hellopoland.sight.Ticket;

public class HplMapper {

  public static void copy(pl.hellopoland.dto.Sight source, Sight target) {
    target.setName(source.name);
    target.setLead(source.lead);
    target.setDescription(source.description);
    target.setEmail(source.email);
    target.setPhone(source.phone);

    copyLocation(source.location, target);
  }

  public static pl.hellopoland.dto.Sight getDTO(Sight bo) {
    pl.hellopoland.dto.Sight dto = new pl.hellopoland.dto.Sight();

    dto.id = bo.getId();
    dto.name = bo.getName();
    dto.lead = bo.getLead();
    dto.description = bo.getDescription();
    dto.mainImage = bo.getMainImage() != null ? getDTO(bo.getMainImage()) : null;
    dto.location = ofNullable(bo.getLocation()).map(HplMapper::getDTO).orElse(null);

    return dto;
  }

  public static pl.hellopoland.dto.Sight getFullDTO(Sight bo) {
    pl.hellopoland.dto.Sight dto = getDTO(bo);
    if (bo.getSightEvents() != null) {
      dto.sightEvents = bo.getSightEvents().stream().map(HplMapper::getFullDTO).collect(toList());
    }
    return dto;
  }

  public static pl.hellopoland.dto.SightEvent getDTO(SightEvent bo) {
    pl.hellopoland.dto.SightEvent dto = new pl.hellopoland.dto.SightEvent();

    dto.id = bo.getId();
    dto.name = bo.getName();
    dto.date = bo.getDate();
    dto.description = bo.getDescription();
    dto.duration = bo.getDuration();
    dto.mainImage = bo.getMainImage() != null ? getDTO(bo.getMainImage()) : null;
    dto.email = bo.getEmail();
    dto.phone = bo.getPhone();
    dto.sightId = bo.getSight().getId();
    dto.generalAdmission = bo.getGeneralAdmission();
    dto.location = ofNullable(bo.getLocation()).map(HplMapper::getDTO).orElse(null);

    return dto;
  }

  public static pl.hellopoland.dto.SightEvent getFullDTO(SightEvent bo) {
    pl.hellopoland.dto.SightEvent dto = getDTO(bo);
    dto.tickets = bo.getTickets().stream().map(HplMapper::getDTO).collect(toList());
    return dto;
  }

  private static pl.hellopoland.dto.Location getDTO(Location bo) {
    pl.hellopoland.dto.Location dto = new pl.hellopoland.dto.Location();

    dto.latitude = bo.getLatitude();
    dto.longitude = bo.getLongitude();
    dto.street = bo.getStreet();
    dto.zipCode = bo.getZipCode();
    dto.city = bo.getCity();
    dto.country = bo.getCountry();

    return dto;
  }

  private static pl.hellopoland.dto.TicketDefinition getDTO(Ticket bo) {
    pl.hellopoland.dto.TicketDefinition dto = new pl.hellopoland.dto.TicketDefinition();

    dto.id = bo.getId();
    dto.name = bo.getName();
    dto.price = bo.getPrice();
    dto.predefinedDate = bo.isPredefinedDate();
    dto.date = bo.getDate();
    dto.dateType = DateType.valueOf(bo.getDateType().name());
    dto.sightEventId = bo.getSightEvent().getId();

    return dto;
  }

  private static pl.hellopoland.dto.Image getDTO(Image bo) {
    if (bo == null) {
      return null;
    }
    pl.hellopoland.dto.Image dto = new pl.hellopoland.dto.Image();
    dto.original = bo.getDownloadUrl();
    return dto;
  }

  private static void copyLocation(pl.hellopoland.dto.Location source, Located target) {
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

  public static pl.hellopoland.dto.SightEvent getGAEventDTO(Sight bo) {
    pl.hellopoland.dto.SightEvent dto = new pl.hellopoland.dto.SightEvent();
    dto.sightId = bo.getId();
    dto.name = bo.getName();
    dto.description = bo.getDescription();
    dto.mainImage = getDTO(bo.getMainImage());
    dto.email = bo.getEmail();
    dto.phone = bo.getPhone();
    dto.location = ofNullable(bo.getLocation()).map(HplMapper::getDTO).orElse(null);
    dto.generalAdmission = true;
    return dto;
  }

  public static void copy(pl.hellopoland.dto.SightEvent source, SightEvent target) {
    target.setName(source.name);
    target.setDate(source.date);
    target.setLead(source.lead);
    target.setDescription(source.description);
    target.setEmail(source.email);
    target.setPhone(source.phone);
    target.setGeneralAdmission(source.generalAdmission);
    target.setHptId(source.id);

    copyLocation(source.location, target);
  }

}
