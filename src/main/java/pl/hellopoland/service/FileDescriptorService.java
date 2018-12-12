package pl.hellopoland.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.System.Logger.Level;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import pl.hellopoland.bo.FileDescriptor;

@LocalBean
@Stateless
public class FileDescriptorService extends ServiceSuperclass {
  private final System.Logger logger = System.getLogger(this.getClass().getName());

  public FileDescriptor storeFile(ByteArrayInputStream byteArrayInputStream, String extension) {
    var fd = new FileDescriptor(storeFileOnDisc(byteArrayInputStream, extension));
    em.persist(fd);
    return fd;
  }

  private File storeFileOnDisc(ByteArrayInputStream byteArrayInputStream, String extension) {
    String hash = UUID.randomUUID().toString().replace('-', 'x');
    String path = properties.getProperty("dms.root.path") + File.separator
        + "sight_event_attachments" + File.separator + hash.substring(0, 1) + File.separator
        + hash.substring(1, 2) + File.separator;
    final File targetFile = createEmptyFileOnDisc(path + hash + "." + extension);
    try {
      Files.copy(byteArrayInputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      throw new RuntimeException("File NOT stored", e);
    }
    return targetFile;
  }

  private File createEmptyFileOnDisc(String path) {
    File targetFile = new File(path);
    File parent = targetFile.getParentFile();
    if (!parent.exists() && !parent.mkdirs()) {
      throw new IllegalStateException("Couldn't create dir: " + parent);
    }
    return targetFile;
  }

  public void deleteFile(FileDescriptor fileDescriptor) {
    try {
      Files.deleteIfExists(Paths.get(fileDescriptor.getPath()));
    } catch (IOException e) {
      logger.log(Level.ERROR, e.getLocalizedMessage());
      throw new IllegalStateException("Couldn't delete file: " + fileDescriptor.getPath());
    }
  }

}
