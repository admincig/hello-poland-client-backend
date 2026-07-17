package pl.hellopoland.service.api.partner;

import pl.hellopoland.dto.TicketDefinitionDTO;
import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.dto.TicketPoolTypeDTO;
import pl.hellopoland.exception.notfound.ResourceNotFoundException;
import pl.hellopoland.service.TicketPoolDefinitionService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class TicketPoolDefinitionServicePartnerAPI {

  @Inject
  TicketPoolDefinitionService service;

  @RolesAllowed("partner")
  public TicketPoolDefinitionDTO add(TicketPoolDefinitionDTO dto) {
    dto.poolType = TicketPoolTypeDTO.STANDARD;
    dto.visibleForPartner = true;
    dto.visibleOnPortal = true;
    return service.add(dto);
  }

  @RolesAllowed("partner")
  public TicketPoolDefinitionDTO get(Long id) {
    TicketPoolDefinitionDTO dto = service.get(id);
    requirePartnerVisible(dto);
    return dto;
  }

  @RolesAllowed("partner")
  public void delete(Long id) {
    requirePartnerVisible(service.get(id));
    service.delete(id);
  }

  @RolesAllowed("partner")
  public TicketPoolDefinitionDTO update(TicketPoolDefinitionDTO dto) {
    TicketPoolDefinitionDTO current = service.get(dto.id);
    requirePartnerVisible(current);
    dto.poolType = defaultPoolType(current.poolType);
    dto.visibleForPartner = defaultVisible(current.visibleForPartner);
    dto.visibleOnPortal = defaultVisible(current.visibleOnPortal);
    if (dto.ticketDefinitions != null) {
      for (TicketDefinitionDTO ticketDef : dto.ticketDefinitions) {
        if (ticketDef.discount != null) {
          ticketDef.discount.isCustomCommission = false;
        }
      }
    }
    return service.update(dto);
  }

  private void requirePartnerVisible(TicketPoolDefinitionDTO dto) {
    if (dto == null
        || TicketPoolTypeDTO.PROMOTIONAL.equals(defaultPoolType(dto.poolType))
        || !defaultVisible(dto.visibleForPartner)) {
      throw new ResourceNotFoundException();
    }
  }

  private TicketPoolTypeDTO defaultPoolType(TicketPoolTypeDTO poolType) {
    return poolType == null ? TicketPoolTypeDTO.STANDARD : poolType;
  }

  private boolean defaultVisible(Boolean visible) {
    return visible == null || visible;
  }

}
