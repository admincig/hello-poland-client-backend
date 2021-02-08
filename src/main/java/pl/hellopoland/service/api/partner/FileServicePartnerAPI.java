package pl.hellopoland.service.api.partner;

import org.apache.commons.lang3.tuple.Pair;
import pl.hellopoland.bo.ImageCollector;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.rest.dto.UploadFilesResult;
import pl.hellopoland.service.ImageService;
import pl.hellopoland.service.LibraryFileService;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBAccessException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class FileServicePartnerAPI {

  @Inject
  private LibraryFileService service;
  @Inject
  private ImageService imageService;

  @RolesAllowed("partner")
  public UploadFilesResult uploadImages(List<Pair<String, byte[]>> pairs) {
    Partner partner = service.getLoggedPartner();
    return service.uploadImages(pairs, partner.getId());
  }

  @RolesAllowed("partner")
  public void deleteImage(Long imageId) {
    Partner partner = service.getLoggedPartner();
    ImageCollector image = imageService.get(imageId);
    if (image.getPartner() != null && image.getPartner().getId().equals(partner.getId())) {
      imageService.delete(imageId);
    } else {
      throw new EJBAccessException();
    }
  }
}
