package pl.hellopoland.image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.System.Logger;
import java.net.URL;
import java.util.UUID;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import pl.hellopoland.ServiceSuperclass;
import pl.hellopoland.util.Imaged;

@LocalBean
@Stateless
public class ImageService extends ServiceSuperclass {

  public Image storeImage(InputStream is, String extension, String url) {
    String hash = UUID.randomUUID().toString().replace('-', 'x');
    String path = properties.getProperty("dms.root.path") + File.separator + hash.substring(0, 1)
        + File.separator + hash.substring(1, 2) + File.separator;

    byte[] buf = new byte[10000];
    int size = 0;
    try {
      createEmptyFileOnDisc(path + hash + extension);
      final File file = new File(path + hash + extension);
      final OutputStream out = new FileOutputStream(file);
      int ret;
      while ((ret = is.read(buf)) > 0) {
        out.write(buf, 0, ret);
        size += ret;
      }
      out.close();
      logger.log(Logger.Level.DEBUG, "Saved file of size" + size);
    } catch (Exception ioe) {
      throw new RuntimeException("File NOT stored", ioe);
    }

    Image image = new Image();
    image.setPath(path);
    image.setHash(hash);
    image.setExtension(extension);
    image.setImageURL(url);
    em.persist(image);
    return image;
  }

  private void createEmptyFileOnDisc(String path) {
    File targetFile = new File(path);
    File parent = targetFile.getParentFile();
    if (!parent.exists() && !parent.mkdirs()) {
      throw new IllegalStateException("Couldn't create dir: " + parent);
    }
  }

  public File getImage(String name) {
    String path =
        em.createQuery("select path from Image where concat(hash, extension)=:name", String.class)
            .setParameter("name", name).getSingleResult();
    return new File(path + name);
  }

  public Image downloadImage(String url) {
    Image im = null;
    if (url != null) {
      try {
        logger.log(Logger.Level.INFO, "Downloading image " + url);
        im = storeImage(new URL(url).openConnection().getInputStream(), "jpg", url);
      } catch (Exception e) {
        logger.log(Logger.Level.WARNING, e.getMessage());
      }
    }
    return im;
  }

  public void update(Imaged bo, String importUrl) {
    Image boImage = bo.getMainImage();
    Image image = getOrDownload(importUrl);
    if (boImage == null || !boImage.getId().equals(image.getId())) {
      bo.setMainImage(image);
    }
  }

  private Image getOrDownload(String importUrl) {
    try {
      return em.createQuery("from Image where imageURL=:url", Image.class)
          .setParameter("url", importUrl).getSingleResult();
    } catch (NoResultException e) {
      return downloadImage(importUrl);
    }

  }
}
