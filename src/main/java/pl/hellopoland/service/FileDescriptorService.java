package pl.hellopoland.service;

import pl.hellopoland.bo.FileDescriptor;
import pl.hellopoland.bo.Partner;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.System.Logger.Level;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@LocalBean
@Stateless
public class FileDescriptorService extends ServiceSuperclass {

  public FileDescriptor storeSightEventAttachment(ByteArrayInputStream bais, String extension) {
    File file = storeSightEventAttachmentOnDisc(bais, extension);
    FileDescriptor fd = new FileDescriptor(file);
    em.persist(fd);
    return fd;
  }

  public FileDescriptor storeLibraryFile(ByteArrayInputStream bais, String extension,
      Partner partner) {
    File file = storeLibraryFileOnDisc(bais, extension, partner.getId());
    FileDescriptor fd = new FileDescriptor(file, partner);
    em.persist(fd);
    return fd;
  }

  public File getFile(String name) {
    var path = em
        .createQuery("select path from FileDescriptor where path like :name or path like :name2",
            String.class)
        .setParameter("name", "%/" + name).setParameter("name2", "%\\" + name).getSingleResult();
    return new File(path);
  }

  public File createEmptyFileOnDisc(String path) {
    File targetFile = new File(path);
    File parent = targetFile.getParentFile();
    if (!parent.exists() && !parent.mkdirs()) {
      logger.log(Level.ERROR, "Couldn't create dir: " + parent);
      throw new IllegalStateException("Couldn't create dir: " + parent);
    }
    return targetFile;
  }

  public void deleteFile(Path filePath) {
    try {
      boolean deleted = Files.deleteIfExists(filePath);
      logger.log(Level.INFO, "File " + filePath + (deleted ? " deleted" : " not  exists"));
    } catch (IOException e) {
      logger.log(Level.ERROR, e.getLocalizedMessage());
      throw new IllegalStateException("Couldn't delete file: " + filePath);
    }
  }

  private File storeSightEventAttachmentOnDisc(ByteArrayInputStream bais, String extension) {
    String hash = UUID.randomUUID().toString().replace('-', 'x');
    String path = getPathForSightEventAttachment(hash);
    return storeOnDisc(bais, path, hash, extension);
  }

  private String getPathForSightEventAttachment(String hash) {
    return properties.getProperty("dms.root.path")
        + File.separator
        + "sight_event_attachments"
        + File.separator
        + hash.substring(0, 1)
        + File.separator
        + hash.substring(1, 2)
        + File.separator;
  }

  private File storeLibraryFileOnDisc(ByteArrayInputStream bais, String extension, Long partnerId) {
    String hash = UUID.randomUUID().toString().replace('-', 'x');
    String path = getPathForLibraryFile(hash, partnerId);
    return storeOnDisc(bais, path, hash, extension);
  }

  private File storeOnDisc(ByteArrayInputStream bais, String path, String hash, String extension) {
    final File targetFile = createEmptyFileOnDisc(path + hash + "." + extension);
    try {
      Files.copy(bais, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      throw new RuntimeException("File NOT stored", e);
    }
    return targetFile;
  }

  private String getPathForLibraryFile(String hash, Long partnerId) {
    return properties.getProperty("dms.root.path")
        + File.separator
        + "libraries"
        + File.separator
        + "partners"
        + File.separator
        + (partnerId == null ? "-1" : partnerId)
        + File.separator
        + hash.substring(0, 1)
        + File.separator
        + hash.substring(1, 2)
        + File.separator;
  }

  public FileDescriptor get(Long fileId) {
    return em.createQuery("from FileDescriptor where id = :id", FileDescriptor.class)
        .setParameter("id", fileId).getSingleResult();
  }

  public void delete(Long fileId) {
    FileDescriptor fd = get(fileId);
    try {
      Files.deleteIfExists(Path.of(fd.getPath()));
      em.remove(fd);
    } catch (Exception e) {
      logger.log(Level.WARNING, "Failed to delete file descriptor", e);
    }
  }
}
