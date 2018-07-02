package pl.hellopoland.sight;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.ServiceSuperclass;
import pl.hellopoland.dto.DateType;
import pl.hellopoland.dto.Location;
import pl.hellopoland.dto.SightEventDefinition;
import pl.hellopoland.dto.TicketDefinition;
import pl.hellopoland.exception.ExceptionFactory;
import pl.hellopoland.image.Image;
import pl.hellopoland.image.ImageService;
import pl.hellopoland.partner.Partner;
import pl.hellopoland.partner.PartnerService;
import pl.hellopoland.security.dto.CurrentUser;

@LocalBean
@Stateless
public class SightService extends ServiceSuperclass {

  @Inject
  private ImageService imageService;

  @Inject
  private PartnerService partnerService;

  @Inject
  private SightEventService sightEventService;

  @Inject
  private ExceptionFactory exceptionFactory;

  public pl.hellopoland.dto.Sight add(pl.hellopoland.dto.Sight sightDTO, CurrentUser currentUser) {
    Sight sight = new Sight();

    fillInSightWithDTOData(sight, sightDTO, currentUser);

    if (sightDTO.generalAdmission) {
      createGeneralAdmissionSightEvent(sight, currentUser);
    }

    return sightDTO;
  }

  public pl.hellopoland.dto.Sight get(Long sightId) {
    Sight sight = em.createQuery("from Sight sight where sight.id=:sightId", Sight.class)
        .setParameter("sightId", sightId)
        .getSingleResult();

    return createSightDTO(sight);
  }

  public List<pl.hellopoland.dto.Sight> get() {
    return em.createQuery("from Sight sight where sight.active=True", Sight.class)
        .getResultStream()
        .map(this::createSightDTO)
        .collect(toList());
  }

  public List<pl.hellopoland.dto.Sight> getAllForPartner(CurrentUser currentUser) {
    Partner partner = partnerService.findByUserEmail(currentUser.getEmail());

    return em
        .createQuery("from Sight sight where sight.active=True and sight.partner.id=:partnerId",
            Sight.class)
        .setParameter("partnerId", partner.getId())
        .getResultStream()
        .map(this::createSightDTO)
        .collect(toList());
  }

  public pl.hellopoland.dto.Sight update(Long sightId, pl.hellopoland.dto.Sight sightDTO,
      CurrentUser currentUser) {
    Sight sight = em.createQuery("from Sight sight where sight.id=:sightId", Sight.class)
        .setParameter("sightId", sightId)
        .getSingleResult();

    fillInSightWithDTOData(sight, sightDTO, currentUser);

    return sightDTO;
  }

  public void delete(Long sightId) {
    Sight sight = em.createQuery("from Sight sight where sight.id=:sightId", Sight.class)
        .setParameter("sightId", sightId)
        .getSingleResult();

    if (hasActiveSightEvents(sight.getSightEvents())) {
      throw exceptionFactory.sightHasAssignedSightEventsException();
    } else {
      sight.setActive(false);
    }
  }

  private boolean hasActiveSightEvents(List<SightEvent> sightEvents) {
    for (SightEvent sightEvent : sightEvents) {
      if (sightEvent.isActive()) {
        return true;
      }
    }

    return false;
  }

  private void fillInSightWithDTOData(Sight sight, pl.hellopoland.dto.Sight sightDTO,
      CurrentUser currentUser) {
    Partner partner = partnerService.findByUserEmail(currentUser.getEmail());

    sight.setName(sightDTO.name);
    sight.setLead(sightDTO.lead);
    sight.setDescription(sightDTO.description);
    sight.setMainImage(imageService
        .downloadImage(sightDTO.mainImage == null ? null : sightDTO.mainImage.ImageURL));
    sight.setEmail(sightDTO.email);
    sight.setPhone(sightDTO.phone);
    sight.setPartner(partner);

    fillInSightLocationWithDTOData(sight, sightDTO);

    em.persist(sight);

    sightDTO.id = sight.getId();

    if (sight.getMainImage() != null) {
      sightDTO.mainImage.path = sight.getMainImage().getPath();
      sightDTO.mainImage.hash = sight.getMainImage().getHash();
      sightDTO.mainImage.extension = sight.getMainImage().getExtension();
      sightDTO.mainImage.ImageURL = sight.getMainImage().getImageURL();
    }
  }

  private void fillInSightLocationWithDTOData(Sight sight,
      pl.hellopoland.dto.Sight sightDTO) {
    SightLocation sightLocation = sight.getSightLocation();

    if (sightDTO.sightLocation != null) {
      if (sightLocation == null) {
        sightLocation = new SightLocation();
      }

      sightLocation.setLatitude(sightDTO.sightLocation.latitude);
      sightLocation.setLongitude(sightDTO.sightLocation.longitude);
      sightLocation.setStreet(sightDTO.sightLocation.street);
      sightLocation.setZipCode(sightDTO.sightLocation.zipCode);
      sightLocation.setCity(sightDTO.sightLocation.city);
      sightLocation.setCountry(sightDTO.sightLocation.country);
    } else {
      sight.setSightLocation(null);
    }

    sight.setSightLocation(sightLocation);
  }

  private void createGeneralAdmissionSightEvent(Sight sight, CurrentUser currentUser) {
    SightEventDefinition sightEventDefinition = new SightEventDefinition();

    sightEventDefinition.name = sight.getName();
    sightEventDefinition.description = sight.getDescription();
    sightEventDefinition.mainImageUrl =
        sight.getMainImage() != null ? sight.getMainImage().getImageURL() : null;
    sightEventDefinition.email = sight.getEmail();
    sightEventDefinition.phone = sight.getPhone();
    sightEventDefinition.sightId = sight.getId();

    sightEventDefinition.location = ofNullable(sight.getSightLocation())
        .map(this::createSightLocationDTO)
        .orElse(null);

    sightEventService.addToHpt(sightEventDefinition, currentUser);
  }

  private pl.hellopoland.dto.Sight createSightDTO(Sight sight) {
    pl.hellopoland.dto.Sight sightDTO = new pl.hellopoland.dto.Sight();

    sightDTO.id = sight.getId();
    sightDTO.name = sight.getName();
    sightDTO.lead = sight.getLead();
    sightDTO.description = sight.getDescription();
    sightDTO.mainImage = sight.getMainImage() != null ? createImageDTO(sight.getMainImage()) : null;
    sightDTO.sightEventDefinitions = sight.getSightEvents().stream()
        .map(this::createSightEventDTO)
        .collect(toList());

    return sightDTO;
  }

  private SightEventDefinition createSightEventDTO(SightEvent sightEvent) {
    SightEventDefinition sightEventDefinition = new SightEventDefinition();

    sightEventDefinition.id = sightEvent.getId();
    sightEventDefinition.name = sightEvent.getName();
    sightEventDefinition.date = sightEvent.getDate();
    sightEventDefinition.availableTicketsNumber = sightEvent.getAvailableTicketsNumber();
    sightEventDefinition.description = sightEvent.getDescription();
    sightEventDefinition.duration = sightEvent.getDuration();
    sightEventDefinition.mainImage =
        sightEvent.getMainImage() != null ? createImageDTO(sightEvent.getMainImage()) : null;
    sightEventDefinition.email = sightEvent.getEmail();
    sightEventDefinition.phone = sightEvent.getPhone();
    sightEventDefinition.sightId = sightEvent.getSight().getId();

    sightEventDefinition.location = ofNullable(sightEvent.getLocation())
        .map(this::createSightLocationDTO)
        .orElse(null);

    sightEventDefinition.tickets = sightEvent.getTickets().stream()
        .map(this::createTicketDefinitionDTO)
        .collect(toList());

    return sightEventDefinition;
  }

  private Location createSightLocationDTO(SightLocation sightLocation) {
    Location location = new Location();

    location.latitude = sightLocation.getLatitude();
    location.longitude = sightLocation.getLongitude();
    location.street = sightLocation.getStreet();
    location.zipCode = sightLocation.getZipCode();
    location.city = sightLocation.getCity();
    location.country = sightLocation.getCountry();

    return location;
  }

  private TicketDefinition createTicketDefinitionDTO(Ticket ticket) {
    TicketDefinition ticketDefinitionDTO = new TicketDefinition();

    ticketDefinitionDTO.id = ticket.getId();
    ticketDefinitionDTO.name = ticket.getName();
    ticketDefinitionDTO.price = ticket.getPrice();
    ticketDefinitionDTO.predefinedDate = ticket.isPredefinedDate();
    ticketDefinitionDTO.date = ticket.getDate();
    ticketDefinitionDTO.dateType = DateType.valueOf(ticket.getDateType().name());
    ticketDefinitionDTO.sightEventId = ticket.getSightEvent().getId();

    return ticketDefinitionDTO;
  }

  private pl.hellopoland.dto.Image createImageDTO(Image image) {
    pl.hellopoland.dto.Image imageDTO = new pl.hellopoland.dto.Image();

    imageDTO.path = image.getPath();
    imageDTO.hash = image.getHash();
    imageDTO.extension = image.getExtension();
    imageDTO.ImageURL = image.getImageURL();

    return imageDTO;
  }
}
