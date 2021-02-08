package pl.hellopoland.service;

import org.apache.commons.lang3.tuple.Pair;
import pl.hellopoland.bo.FileDescriptor;
import pl.hellopoland.bo.ImageCollector;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.rest.dto.UploadFilesResult;
import pl.hellopoland.util.DtoMapper;

import javax.ejb.Stateless;
import javax.inject.Inject;
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

  public UploadFilesResult uploadImages(List<Pair<String, byte[]>> pairs, Long partnerId) {
    Partner partner = partnerService.get(partnerId);
    UploadFilesResult result = new UploadFilesResult();
    pairs.stream()
        .forEach(pair -> {
          String name = pair.getLeft();
          String extension = name.substring(name.lastIndexOf('.' + 1));
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


}
