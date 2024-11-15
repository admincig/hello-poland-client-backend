package pl.hellopoland.service.api.partner;

import org.apache.commons.lang3.tuple.Pair;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.rest.dto.UploadFilesResult;
import pl.hellopoland.service.LibraryFileService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;

@Stateless
public class FileServicePartnerAPI {

  @Inject
  private LibraryFileService service;

  @RolesAllowed("partner")
  public UploadFilesResult uploadFiles(List<Pair<String, byte[]>> pairs) {
    Partner partner = service.getLoggedPartner();
    return service.uploadFiles(pairs, partner.getId());
  }

  @RolesAllowed("partner")
  public void deleteFile(Long fileId, boolean isImage) {
    Partner partner = service.getLoggedPartner();
    service.deleteFile(fileId, partner.getId(), isImage);
  }
}
