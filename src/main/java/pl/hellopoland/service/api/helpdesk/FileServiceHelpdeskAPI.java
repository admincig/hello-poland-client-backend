package pl.hellopoland.service.api.helpdesk;

import org.apache.commons.lang3.tuple.Pair;
import pl.hellopoland.rest.dto.UploadFilesResult;
import pl.hellopoland.service.HelpdeskAccessService;
import pl.hellopoland.service.LibraryFileService;
import pl.hellopoland.service.PartnerService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;

@Stateless
public class FileServiceHelpdeskAPI {

  @Inject
  private LibraryFileService service;
  @Inject
  private PartnerService partnerService;
  @Inject
  private HelpdeskAccessService accessService;

  @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager",
      "helpdesk_content_manager"})
  public UploadFilesResult uploadFiles(List<Pair<String, byte[]>> pairs, Long partnerId) {
    accessService.requirePartnerAccess(partnerId != null ? partnerService.get(partnerId) : null);
    return service.uploadFiles(pairs, partnerId);
  }

  @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager",
      "helpdesk_content_manager"})
  public void deleteFile(Long fileId, Long partnerId, boolean image) {
    accessService.requirePartnerAccess(partnerId != null ? partnerService.get(partnerId) : null);
    service.deleteFile(fileId, partnerId, image);
  }
}
