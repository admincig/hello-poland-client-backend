package pl.hellopoland.service;

import org.apache.commons.lang3.tuple.Pair;
import pl.hellopoland.bo.FileDescriptor;
import pl.hellopoland.bo.ImageCollector;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.rest.dto.UploadFilesResult;
import pl.hellopoland.util.DtoMapper;

import jakarta.ejb.EJBAccessException;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.io.ByteArrayInputStream;
import java.util.List;

@Stateless
public class LibraryFileService extends ServiceSuperclass {

  @Inject
  private ImageService imageService;
  @Inject
  private FileDescriptorService fileDescriptorService;
  @Inject
  private PartnerService partnerService;
  @Inject
  private SightService sightService;
  @Inject
  private SightEventService sightEventService;

  public UploadFilesResult uploadFiles(List<Pair<String, byte[]>> pairs, Long partnerId) {
    Partner partner = partnerService.get(partnerId);
    UploadFilesResult result = new UploadFilesResult();
    pairs.stream()
        .forEach(pair -> {
          String name = pair.getLeft();
          String extension = name.substring(name.lastIndexOf('.') + 1);
          ByteArrayInputStream bais = new ByteArrayInputStream(pair.getRight());
          if (isImage(extension)) {
            ImageCollector ic =
                imageService.validateAndStoreImageCollector(name, bais, extension, null);
            ic.setPartner(partner);
            result.images.add(DtoMapper.getDTO(ic));
          } else {
            FileDescriptor fd =
                fileDescriptorService.storeLibraryFile(bais, extension, partner);
            result.files.add(DtoMapper.getDTO(fd));
          }
        });
    return result;
  }

  private static boolean isImage(String name) {
    return name.equals("jpg")
        || name.equals("jpeg")
        || name.equals("png");
  }


  public void deleteFile(Long fileId, Long partnerId, boolean isImage) {
    if (isImage) {
      ImageCollector image = imageService.get(fileId);
      if (partnerId == null //helpdesk case
          || image.getPartner() != null && partnerId.equals(image.getPartner().getId())) {
        sightService.dereferenceImage(fileId);
        sightEventService.dereferenceImage(fileId);
        imageService.delete(fileId);
      } else { //no partner or not owned by partnerId
        throw new EJBAccessException();
      }
    } else {
      FileDescriptor file = fileDescriptorService.get(fileId);
      if (partnerId == null //helpdesk case
          || file.getPartner() != null && partnerId.equals(file.getPartner().getId())) {
        sightEventService.dereferenceAttachment(fileId);
        fileDescriptorService.delete(fileId);
      } else { //no partner or not owned by partnerId
        throw new EJBAccessException();
      }
    }
  }
}
