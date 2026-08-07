package pl.hellopoland.service.api.helpdesk;

import pl.hellopoland.rest.dto.PromotionCampaignHelpdeskDTO;
import pl.hellopoland.rest.dto.PromotionCampaignSightEventHelpdeskDTO;
import pl.hellopoland.rest.dto.PromotionCodeBatchHelpdeskDTO;
import pl.hellopoland.rest.dto.PromotionCodeHelpdeskDTO;
import pl.hellopoland.rest.dto.PromotionCodeImportIRO;
import pl.hellopoland.rest.dto.PromotionCodeRedemptionHelpdeskDTO;
import pl.hellopoland.rest.dto.PromotionCodeSetupIRO;
import pl.hellopoland.rest.dto.PromotionTicketPoolGenerationIRO;
import pl.hellopoland.rest.dto.PromotionTicketPoolTargetPreviewORO;
import pl.hellopoland.service.PromotionManagementService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;

@Stateless
public class PromotionManagementServiceHelpdeskAPI {

  @Inject
  PromotionManagementService service;

  @RolesAllowed({"root", "admin"})
  public List<PromotionCampaignHelpdeskDTO> listCampaigns() {
    return service.listCampaigns();
  }

  @RolesAllowed({"root", "admin"})
  public PromotionCampaignHelpdeskDTO getCampaign(Long id) {
    return service.getCampaign(id);
  }

  @RolesAllowed({"root", "admin"})
  public PromotionCampaignHelpdeskDTO createCampaign(PromotionCampaignHelpdeskDTO dto) {
    return service.createCampaign(dto);
  }

  @RolesAllowed({"root", "admin"})
  public PromotionTicketPoolTargetPreviewORO previewTargets(PromotionCampaignHelpdeskDTO dto) {
    return service.previewTargets(dto);
  }

  @RolesAllowed({"root", "admin"})
  public PromotionCampaignHelpdeskDTO updateCampaign(Long id, PromotionCampaignHelpdeskDTO dto) {
    return service.updateCampaign(id, dto);
  }

  @RolesAllowed({"root", "admin"})
  public PromotionCampaignHelpdeskDTO changeCampaignStatus(Long id, PromotionCampaignHelpdeskDTO dto) {
    return service.changeCampaignStatus(id, dto);
  }

  @RolesAllowed({"root", "admin"})
  public PromotionCampaignSightEventHelpdeskDTO addSightEvent(Long campaignId,
      PromotionCampaignSightEventHelpdeskDTO dto) {
    return service.addSightEvent(campaignId, dto);
  }

  @RolesAllowed({"root", "admin"})
  public PromotionCampaignSightEventHelpdeskDTO updateSightEvent(Long campaignId, Long relationId,
      PromotionCampaignSightEventHelpdeskDTO dto) {
    return service.updateSightEvent(campaignId, relationId, dto);
  }

  @RolesAllowed({"root", "admin"})
  public void removeSightEvent(Long campaignId, Long relationId) {
    service.removeSightEvent(campaignId, relationId);
  }

  @RolesAllowed({"root", "admin"})
  public PromotionCampaignHelpdeskDTO updateSightEvents(Long campaignId,
      PromotionTicketPoolGenerationIRO iro) {
    return service.updateSightEvents(campaignId, iro);
  }

  @RolesAllowed({"root", "admin"})
  public List<PromotionCodeHelpdeskDTO> listCodes(Long campaignId) {
    return service.listCodes(campaignId);
  }

  @RolesAllowed({"root", "admin"})
  public List<PromotionCodeHelpdeskDTO> createCodes(Long campaignId, PromotionCodeSetupIRO iro) {
    return service.createCodes(campaignId, iro);
  }

  @RolesAllowed({"root", "admin"})
  public List<PromotionCodeHelpdeskDTO> replaceCodes(Long campaignId, PromotionCodeSetupIRO iro) {
    return service.replaceCodes(campaignId, iro);
  }

  @RolesAllowed({"root", "admin"})
  public String exportCodesCsv(Long campaignId) {
    return service.exportCodesCsv(campaignId);
  }

  @RolesAllowed({"root", "admin"})
  public List<PromotionCodeBatchHelpdeskDTO> listBatches(Long campaignId) {
    return service.listBatches(campaignId);
  }

  @RolesAllowed({"root", "admin"})
  public List<PromotionCodeHelpdeskDTO> importCodes(Long campaignId, PromotionCodeImportIRO iro) {
    return service.importCodes(campaignId, iro);
  }

  @RolesAllowed({"root", "admin"})
  public PromotionCodeHelpdeskDTO updateCode(Long campaignId, Long codeId,
      PromotionCodeHelpdeskDTO dto) {
    return service.updateCode(campaignId, codeId, dto);
  }

  @RolesAllowed({"root", "admin"})
  public List<PromotionCodeRedemptionHelpdeskDTO> listRedemptions(Long campaignId) {
    return service.listRedemptions(campaignId);
  }

  @RolesAllowed({"root", "admin"})
  public PromotionCampaignHelpdeskDTO generateTicketPools(Long campaignId,
      PromotionTicketPoolGenerationIRO iro) {
    return service.generateTicketPools(campaignId, iro);
  }

  @RolesAllowed({"root", "admin"})
  public PromotionTicketPoolTargetPreviewORO previewTicketPoolTargets(Long campaignId,
      PromotionTicketPoolGenerationIRO iro) {
    return service.previewTicketPoolTargets(campaignId, iro);
  }
}
