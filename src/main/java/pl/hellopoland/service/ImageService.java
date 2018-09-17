package pl.hellopoland.service;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.net.URL;
import java.util.UUID;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.persistence.NoResultException;
import pl.hellopoland.bo.ImageCollector;
import pl.hellopoland.bo.ImageVariant;
import pl.hellopoland.bo.ImageVariant.Variant;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.util.Imaged;

@LocalBean
@Stateless
public class ImageService extends ServiceSuperclass {

  public ImageCollector validateAndStoreImageCollector(InputStream is, String extension,
      String url) {
    BufferedImage buffImage = validate(is);
    return storeImageCollector(buffImage, extension, url);
  }

  private BufferedImage validate(InputStream is) {
    try {
      BufferedImage imageIO = ImageIO.read(is);
      logger.log(Level.INFO,
          "image width: " + imageIO.getWidth() + ", height: " + imageIO.getHeight());
      if (imageIO.getWidth() < 2000) {
        throw new ConflictingException("Image width must be a minimum of 2000px");
      }
      return imageIO;
    } catch (IOException e) {
      throw new ConflictingException("Failed to validate image", e);
    }
  }

  private ImageCollector storeImageCollector(BufferedImage buffImage, String extension,
      String url) {
    var collector = new ImageCollector();
    collector.setImageURL(url);
    em.persist(collector);
    collector.setQvga(storeImageVariant(scaleImage(buffImage, 320), extension,
        ImageVariant.Variant.QVGA, collector));
    collector.setVga(storeImageVariant(scaleImage(buffImage, 640), extension,
        ImageVariant.Variant.VGA, collector));
    collector.setXga(storeImageVariant(scaleImage(buffImage, 1024), extension,
        ImageVariant.Variant.XGA, collector));
    collector.setSxga(storeImageVariant(scaleImage(buffImage, 1280), extension,
        ImageVariant.Variant.SXGA, collector));
    collector.setHd(storeImageVariant(scaleImage(buffImage, 720), extension,
        ImageVariant.Variant.HD, collector));
    collector.setFhd(storeImageVariant(scaleImage(buffImage, 1920), extension,
        ImageVariant.Variant.FHD, collector));
    collector.setFourK(storeImageVariant(scaleImage(buffImage, 3840), extension,
        ImageVariant.Variant.FOURK, collector));
    collector.setOrginal(
        storeImageVariant(buffImage, extension, ImageVariant.Variant.ORIGINAL, collector));
    return collector;
  }

  private ImageVariant storeImageVariant(BufferedImage buffImage, String extension, Variant variant,
      ImageCollector collector) {
    String hash = UUID.randomUUID().toString().replace('-', 'x');
    String path = properties.getProperty("dms.root.path") + File.separator + hash.substring(0, 1)
        + File.separator + hash.substring(1, 2) + File.separator;

    int size = 0;
    try {
      createEmptyFileOnDisc(path + hash + "." + extension);
      final File file = new File(path + hash + "." + extension);
      ImageIO.write(buffImage, extension, file);
      logger.log(Logger.Level.DEBUG, "Saved file of size" + size);
    } catch (Exception ioe) {
      throw new RuntimeException("File NOT stored", ioe);
    }

    ImageVariant image = new ImageVariant();
    image.setPath(path);
    image.setHash(hash);
    image.setExtension(extension);
    image.setCollector(collector);
    image.setVariant(variant);
    em.persist(image);
    return image;
  }

  private BufferedImage scaleImage(BufferedImage buffImage, int width) {
    var img = buffImage.getScaledInstance(width, -1, BufferedImage.SCALE_DEFAULT);
    width = img.getWidth(null);
    var height = img.getHeight(null);
    var bImg = new BufferedImage(width, height, buffImage.getType());
    var g2d = bImg.createGraphics();
    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    g2d.drawImage(img, 0, 0, width, height, null);
    g2d.dispose();
    return bImg;
  }

  private void createEmptyFileOnDisc(String path) {
    File targetFile = new File(path);
    File parent = targetFile.getParentFile();
    if (!parent.exists() && !parent.mkdirs()) {
      throw new IllegalStateException("Couldn't create dir: " + parent);
    }
  }

  public File getImage(String name) {
    var path =
        em.createQuery("select path from ImageVariant where concat(hash, '.' ,extension)=:name",
            String.class).setParameter("name", name).getSingleResult();
    return new File(path + name);
  }

  public ImageCollector downloadImage(String url) {
    ImageCollector im = null;
    if (url != null) {
      try {
        logger.log(Logger.Level.INFO, "Downloading image " + url);
        im = validateAndStoreImageCollector(new URL(url).openConnection().getInputStream(), "jpg",
            url);
      } catch (Exception e) {
        logger.log(Logger.Level.WARNING, e.getMessage());
      }
    }
    return im;
  }

  public void update(Imaged bo, String importUrl) {
    if (importUrl == null) {
      bo.setMainImage(null);
    } else {
      var boImage = bo.getMainImage();
      var image = getOrDownload(importUrl);
      if (boImage == null || !boImage.getId().equals(image.getId())) {
        bo.setMainImage(image);
      }
    }
  }

  private ImageCollector getOrDownload(String importUrl) {
    try {
      var parts = importUrl.split("\\/");
      String name = parts[parts.length - 1];
      String hash = name.split("\\.")[0];
      return em.createQuery("from ImageCollector where imageURL=:url "
          + "or orginal.hash=:hash or qvga.hash=:hash or vga.hash=:hash or hd.hash=:hash or xga.hash=:hash or sxga.hash=:hash or fhd.hash=:hash or fourK.hash=:hash",
          ImageCollector.class).setParameter("url", importUrl).setParameter("hash", hash)
          .getSingleResult();
    } catch (NoResultException e) {
      return downloadImage(importUrl);
    }

  }

  public ImageCollector get(Long id) {
    return em.createQuery("from ImageCollector where id=:id", ImageCollector.class)
        .setParameter("id", id).getSingleResult();
  }
}
