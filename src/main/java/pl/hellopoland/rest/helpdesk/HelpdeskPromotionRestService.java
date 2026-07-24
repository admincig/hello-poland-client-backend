package pl.hellopoland.rest.helpdesk;

import pl.hellopoland.rest.dto.PromotionCampaignHelpdeskDTO;
import pl.hellopoland.rest.dto.PromotionCampaignSightEventHelpdeskDTO;
import pl.hellopoland.rest.dto.PromotionCodeBatchHelpdeskDTO;
import pl.hellopoland.rest.dto.PromotionCodeHelpdeskDTO;
import pl.hellopoland.rest.dto.PromotionCodeImportIRO;
import pl.hellopoland.rest.dto.PromotionCodeRedemptionHelpdeskDTO;
import pl.hellopoland.rest.dto.PromotionTicketPoolGenerationIRO;
import pl.hellopoland.rest.dto.PromotionCodeSetupIRO;
import pl.hellopoland.rest.dto.PromotionTicketPoolTargetPreviewORO;
import pl.hellopoland.service.api.helpdesk.PromotionManagementServiceHelpdeskAPI;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@RequestScoped
@Path("/helpdesk/promotions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelpdeskPromotionRestService {

  @Inject
  PromotionManagementServiceHelpdeskAPI service;

  @GET
  public List<PromotionCampaignHelpdeskDTO> listCampaigns() {
    return service.listCampaigns();
  }

  @GET
  @Path("/{id}")
  public PromotionCampaignHelpdeskDTO getCampaign(@PathParam("id") Long id) {
    return service.getCampaign(id);
  }

  @POST
  public PromotionCampaignHelpdeskDTO createCampaign(PromotionCampaignHelpdeskDTO dto) {
    return service.createCampaign(dto);
  }

  @PUT
  @Path("/{id}")
  public PromotionCampaignHelpdeskDTO updateCampaign(@PathParam("id") Long id,
      PromotionCampaignHelpdeskDTO dto) {
    return service.updateCampaign(id, dto);
  }

  @PATCH
  @Path("/{id}/status")
  public PromotionCampaignHelpdeskDTO changeCampaignStatus(@PathParam("id") Long id,
      PromotionCampaignHelpdeskDTO dto) {
    return service.changeCampaignStatus(id, dto);
  }

  @POST
  @Path("/{id}/sight-events")
  public PromotionCampaignSightEventHelpdeskDTO addSightEvent(@PathParam("id") Long id,
      PromotionCampaignSightEventHelpdeskDTO dto) {
    return service.addSightEvent(id, dto);
  }

  @PUT
  @Path("/{id}/sight-events/{relationId}")
  public PromotionCampaignSightEventHelpdeskDTO updateSightEvent(@PathParam("id") Long id,
      @PathParam("relationId") Long relationId, PromotionCampaignSightEventHelpdeskDTO dto) {
    return service.updateSightEvent(id, relationId, dto);
  }

  @DELETE
  @Path("/{id}/sight-events/{relationId}")
  public Response removeSightEvent(@PathParam("id") Long id,
      @PathParam("relationId") Long relationId) {
    service.removeSightEvent(id, relationId);
    return Response.ok().build();
  }

  @GET
  @Path("/{id}/codes")
  public List<PromotionCodeHelpdeskDTO> listCodes(@PathParam("id") Long id) {
    return service.listCodes(id);
  }

  @POST
  @Path("/{id}/codes")
  public List<PromotionCodeHelpdeskDTO> createCodes(@PathParam("id") Long id,
      PromotionCodeSetupIRO iro) {
    return service.createCodes(id, iro);
  }

  @PUT
  @Path("/{id}/codes")
  public List<PromotionCodeHelpdeskDTO> replaceCodes(@PathParam("id") Long id,
      PromotionCodeSetupIRO iro) {
    return service.replaceCodes(id, iro);
  }

  @GET
  @Path("/{id}/codes/export")
  @Produces("text/csv")
  public Response exportCodes(@PathParam("id") Long id) {
    String csv = service.exportCodesCsv(id);
    return Response.ok(csv, "text/csv")
        .header("Content-Disposition", "attachment; filename=\"promotion-" + id + "-codes.csv\"")
        .build();
  }

  @POST
  @Path("/{id}/codes/import")
  public List<PromotionCodeHelpdeskDTO> importCodes(@PathParam("id") Long id,
      PromotionCodeImportIRO iro) {
    return service.importCodes(id, iro);
  }

  @PUT
  @Path("/{id}/codes/{codeId}")
  public PromotionCodeHelpdeskDTO updateCode(@PathParam("id") Long id,
      @PathParam("codeId") Long codeId, PromotionCodeHelpdeskDTO dto) {
    return service.updateCode(id, codeId, dto);
  }

  @GET
  @Path("/{id}/code-batches")
  public List<PromotionCodeBatchHelpdeskDTO> listBatches(@PathParam("id") Long id) {
    return service.listBatches(id);
  }

  @GET
  @Path("/{id}/redemptions")
  public List<PromotionCodeRedemptionHelpdeskDTO> listRedemptions(@PathParam("id") Long id) {
    return service.listRedemptions(id);
  }

  @POST
  @Path("/{id}/ticket-pools/generate")
  public PromotionCampaignHelpdeskDTO generateTicketPools(@PathParam("id") Long id,
      PromotionTicketPoolGenerationIRO iro) {
    return service.generateTicketPools(id, iro);
  }

  @POST
  @Path("/{id}/ticket-pools/preview")
  public PromotionTicketPoolTargetPreviewORO previewTicketPoolTargets(@PathParam("id") Long id,
      PromotionTicketPoolGenerationIRO iro) {
    return service.previewTicketPoolTargets(id, iro);
  }
}
