package pl.hellopoland.service;

import pl.hellopoland.bo.FileDescriptor;
import pl.hellopoland.bo.ImageCollector;
import pl.hellopoland.bo.LibraryFile;
import pl.hellopoland.bo.Partner;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;

@Stateless
public class LibraryFileService extends ServiceSuperclass {

  @Inject
  private ImageService imageService;
  @Inject
  private FileDescriptorService fileDescriptorService;

  public LibraryFile upload(String name, byte[] bytes, Partner owner) {
    name = name.toLowerCase();
    LibraryFile libFile = new LibraryFile();
    libFile.setName(name);
    libFile.setOwner(owner);
    ByteArrayInputStream is = new ByteArrayInputStream(bytes);
    String extension = name.substring(name.lastIndexOf('.') + 1);
    if (isImage(extension)) {
      ImageCollector ic = imageService.validateAndStoreImageCollector(is, extension, null);
      libFile.setImage(ic);
    } else {
      FileDescriptor fd = fileDescriptorService.storeLibraryFile(is, extension, owner.getId());
      libFile.setNonImage(fd);
    }
    em.persist(libFile);
    return libFile;
  }

  private boolean isImage(String name) {
    return name.equals("jpg")
        || name.equals("jpeg")
        || name.equals("png");
  }

}
