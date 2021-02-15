package pl.hellopoland.service.api.helpdesk;

import org.apache.commons.lang3.tuple.Pair;
import pl.hellopoland.rest.dto.UploadFilesResult;
import pl.hellopoland.service.LibraryFileService;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class FileServiceHelpdeskAPI {

  @Inject
  private LibraryFileService service;

  @RolesAllowed({"admin", "salesman"})
  public UploadFilesResult uploadFiles(List<Pair<String, byte[]>> pairs, Long partnerId) {
    return service.uploadFiles(pairs, partnerId);
  }

  @RolesAllowed({"admin", "salesman"})
  public void deleteFile(Long fileId, boolean image) {
    service.deleteFile(fileId, null, image);
  }
}
