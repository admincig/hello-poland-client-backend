package pl.hellopoland.service.api.helpdesk;

import org.apache.commons.lang3.tuple.Pair;
import pl.hellopoland.rest.dto.UploadFilesResult;
import pl.hellopoland.service.ImageService;
import pl.hellopoland.service.LibraryFileService;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class FileServiceHelpdeskAPI {

  @Inject
  private LibraryFileService service;
  @Inject
  private ImageService imageService;

  @RolesAllowed({"admin", "salesman"})
  public UploadFilesResult uploadImages(List<Pair<String, byte[]>> pairs, Long partnerId) {
    return service.uploadImages(pairs, partnerId);
  }

  @RolesAllowed({"admin", "salesman"})
  public void deleteImage(Long imageId) {
    imageService.delete(imageId);
  }
}
