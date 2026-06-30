package pl.hellopoland.service;

import pl.hellopoland.bo.*;
import pl.hellopoland.bo.UserRole.Role;
import pl.hellopoland.dto.DiscountTypeDTO;
import pl.hellopoland.dto.TicketDefinitionDTO;
import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.exception.badrequest.BadRequestException;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.exception.notfound.AccessDeniedException;
import pl.hellopoland.exception.notfound.ResourceNotFoundException;
import pl.hellopoland.util.HelloTicket;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <h3>Klasa wymaga refactoru:</h3>
 * <ul>
 * <li>nazwa klasy
 * <li>ograniczenie liczby odpowiedzialności
 * <li>metody korzystają z getLoggedUsera
 * <li>odhardkodować łączności do HPT
 * </ul>
 */
@Stateless
public class TicketPoolDefinitionService extends ServiceSuperclass {

  @Inject
  SightEventService sightEventService;
  @Inject
  PartnerService partnerService;
  @Inject
  PartnerUserAccessService partnerUserAccessService;

  public TicketPoolDefinitionDTO add(TicketPoolDefinitionDTO dto) {
    return add(dto, getCurrentPartner());
  }

  public TicketPoolDefinitionDTO add(TicketPoolDefinitionDTO dto, Partner partner) {
    final Long sightEventId = dto.sightEventId;
    if (dto.ticketDefinitions == null || dto.ticketDefinitions.isEmpty()) {
          throw new ConflictingException(
                  "Brak ticketDefinitions (dodaj przynajmniej 1 bilet)");
    }
    validateRequiredNormalTicket(dto.ticketDefinitions);

    SightEvent se = sightEventService.getForPartner(dto.sightEventId, partner);
    if (se == null) {
      throw new ResourceNotFoundException();
    }
    requirePartnerUserCanAccess(se);
    validateDates(dto);
    dto.sightEventId = se.getHptId();
    Portal portal = getPortal("Hello Ticket Cloud");
    HelloTicket hpt = new HelloTicket(portal.getUrl());
    TicketPoolDefinitionDTO resp = hpt.addTicketPoolDefinition(dto, partner.getHptToken());
    if (resp == null) {
          throw new ConflictingException("HelloTicket rejected TicketPoolDefinition (null response)");
    }
    resp.sightEventId = sightEventId;
    return resp;
  }

  private void validateDates(TicketPoolDefinitionDTO tpdDTO) {
    if (tpdDTO.endDate != null && tpdDTO.startDate.after(tpdDTO.endDate)) {
      throw new ConflictingException("Ticket pool definition's startDate after endDate.");
    }
    if (tpdDTO.entryEndDate != null && tpdDTO.entryStartDate != null
        && tpdDTO.entryStartDate.after(tpdDTO.entryEndDate)) {
      throw new ConflictingException("Ticket pool definition's entryStartDate after entryEndDate.");
    }
    if (tpdDTO.entryStartDate != null && tpdDTO.startDate != null
        && tpdDTO.entryStartDate.after(tpdDTO.startDate)) {
      throw new ConflictingException("Ticket pool definition's entryStartDate after startDate.");
    }
    var frequencyData = tpdDTO.frequencyData;
    if (frequencyData != null && frequencyData.endDate != null
        && tpdDTO.startDate.after(frequencyData.endDate)) {
      throw new ConflictingException("Ticket pool definition's startDate after frequency endDate.");
    }
    if (frequencyData != null && frequencyData.endDate != null && frequencyData.startDate != null
        && frequencyData.startDate.after(frequencyData.endDate)) {
      throw new ConflictingException(
          "Ticket pool definition's frequency startDate after frequency endDate.");
    }
  }

  public TicketPoolDefinitionDTO get(Long id) {
    return get(id, getCurrentSubject());
  }

  public TicketPoolDefinitionDTO get(Long id, HptSubject subject) {
    if (subject == null) {
      subject = getCurrentSubject();
    }
    Portal portal = getPortal("Hello Ticket Cloud");
    HelloTicket hpt = new HelloTicket(portal.getUrl());
    TicketPoolDefinitionDTO dto = hpt.getTicketPoolDefinition(subject.getHptToken(), id);
    requirePartnerUserCanAccess(dto);
    return dto;
  }

  public void delete(Long id) {
    delete(id, getCurrentSubject());
  }

  public void delete(Long id, HptSubject subject) {
    if (subject == null) {
      subject = getCurrentSubject();
    }
    Portal portal = getPortal("Hello Ticket Cloud");
    HelloTicket hpt = new HelloTicket(portal.getUrl());
    requirePartnerUserCanAccess(hpt.getTicketPoolDefinition(subject.getHptToken(), id));
    hpt.deleteTicketPoolDefinition(subject.getHptToken(), id);
  }

  public List<LocalDate> getStartDates(Long hptId, LocalDate date, LocalDate halfYearLater) {
    HelloTicket hpt = new HelloTicket(getPortal("Hello Ticket Cloud").getUrl());
    return hpt.checkAvailableDates(hptId, date, halfYearLater);
  }

  public TicketPoolDefinitionDTO update(TicketPoolDefinitionDTO dto) {
    return update(dto, getCurrentSubject());
  }

  public TicketPoolDefinitionDTO update(TicketPoolDefinitionDTO dto, HptSubject subject) {
    if (subject == null) {
      subject = getCurrentSubject();
    }
    validateRequiredNormalTicket(dto.ticketDefinitions);
    validateTicketDiscount(dto);
    Portal portal = getPortal("Hello Ticket Cloud");
    HelloTicket hpt = new HelloTicket(portal.getUrl());
    TicketPoolDefinitionDTO tpd = hpt.getTicketPoolDefinition(subject.getHptToken(), dto.id);
    if (tpd == null) {
      throw new AccessDeniedException();
    }
    requirePartnerUserCanAccess(tpd);
    return hpt.updateTicketPoolDefinition(dto, subject.getHptToken());
  }

  public List<TicketPoolDefinitionDTO> list(Long partnerId) {
    HptSubject subject =
        partnerId != null ? partnerService.get(partnerId) : getCurrentSubject();
    Portal portal = getPortal("Hello Ticket Cloud");
    HelloTicket hpt = new HelloTicket(portal.getUrl());
    List<TicketPoolDefinitionDTO> tpds = hpt.getTicketPoolDefinitions(subject.getHptToken(), null);
    if (partnerId != null) {
      Partner partner = partnerService.get(partnerId);
      tpds = tpds.stream().filter(tpd -> tpd.partnerId.equals(partner.getHptId()))
          .collect(Collectors.toList());
    }
    return tpds;
  }

  private HptSubject getCurrentSubject() {
    User logged = getLoggedUser();
    if (logged.hasRole(Role.ADMIN)) {
      return logged;
    }
    return getCurrentPartner();
  }

  private void validateTicketDiscount(TicketPoolDefinitionDTO poolDef) {
    SightEvent se = sightEventService.getByHptId(poolDef.sightEventId);
    BigDecimal commissionPercent = se.getPartner().getCommission();

    for (TicketDefinitionDTO ticketDef : poolDef.ticketDefinitions) {
      if (ticketDef.discount != null) {
        if (ticketDef.discount.type == DiscountTypeDTO.FLAT) {
          ticketDef.discount.amount = ticketDef.discount.value;
        } else {
          ticketDef.discount.amount =
              (int) (1.0 * ticketDef.originalPrice * ticketDef.discount.value / 100);
        }
        ticketDef.discount.price = ticketDef.originalPrice - ticketDef.discount.amount;
        BigDecimal commissionAmount = commissionPercent
            .multiply(new BigDecimal(ticketDef.originalPrice)).divide(new BigDecimal(100));
        BigDecimal priceAfterDiscount = new BigDecimal(ticketDef.discount.price);
        if (priceAfterDiscount.compareTo(commissionAmount) < 0) {
          throw new ConflictingException("Cena po rabacie nie może być mniejsza niż prowizja");
        }
      }
    }
  }

  private void validateRequiredNormalTicket(List<TicketDefinitionDTO> ticketDefinitions) {
    boolean hasNormalTicket = ticketDefinitions != null && ticketDefinitions.stream()
        .map(td -> td.ticketType)
        .filter(Objects::nonNull)
        .anyMatch(ticketType -> "NORMALNY".equals(ticketType.code));
    if (!hasNormalTicket) {
      throw new BadRequestException("Oferta musi zawierać bilet typu Normalny.");
    }
  }

  private Partner getCurrentPartner() {
    String login = Optional.ofNullable(ctx.getCallerPrincipal())
        .map(principal -> principal.getName())
        .orElse(null);
    if (login != null) {
      return partnerService.findByUserEmail(login);
    }
    return getLoggedPartner();
  }

  private void requirePartnerUserCanAccess(TicketPoolDefinitionDTO dto) {
    if (dto == null || dto.sightEventId == null || !isCurrentUserPartnerLogin()) {
      return;
    }
    requirePartnerUserCanAccess(sightEventService.getByHptId(dto.sightEventId));
  }

  private void requirePartnerUserCanAccess(SightEvent sightEvent) {
    if (isCurrentUserPartnerLogin()) {
      partnerUserAccessService.requireCanAccessSightEvent(sightEvent);
    }
  }

  private boolean isCurrentUserPartnerLogin() {
    User logged = getLoggedUser();
    return logged != null && logged.hasRole(Role.PARTNER);
  }
}
