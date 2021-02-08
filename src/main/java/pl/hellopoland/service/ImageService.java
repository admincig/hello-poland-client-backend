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
import javax.inject.Inject;
import javax.persistence.NoResultException;
import pl.hellopoland.bo.ImageCollector;
import pl.hellopoland.bo.ImageVariant;
import pl.hellopoland.bo.ImageVariant.Variant;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.util.Imaged;
import pl.hellopoland.util.webp.WebpIO;

@LocalBean
@Stateless
public class ImageService extends ServiceSuperclass {
  @Inject
  FileDescriptorService fileDescriptorService;

  public ImageCollector validateAndStoreImageCollector(InputStream is, String extension,
      String url) {
    String name = url.substring(url.lastIndexOf('/') + 1);
    return validateAndStoreImageCollector(name, is, extension, url);
  }

  public ImageCollector validateAndStoreImageCollector(InputStream is, String extension) {
    return validateAndStoreImageCollector(null, is, extension, null);
  }

  public ImageCollector validateAndStoreImageCollector(String name, InputStream is, String extension,
      String url) {
    BufferedImage buffImage = validate(is);
    return storeImageCollectorInternal(name, buffImage, extension, url);
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

  private ImageCollector storeImageCollector(String name, InputStream is, String extension,
      String url) {
    try {
      BufferedImage imageIO = ImageIO.read(is);
      return storeImageCollectorInternal(name, imageIO, extension, url);
    } catch (IOException e) {
      throw new ConflictingException("Failed to load image", e);
    }
  }

  public ImageCollector storeImageCollector(InputStream is, String extension) {
    return storeImageCollector(null, is, extension, null);
  }

  private ImageCollector storeImageCollectorInternal(String name, BufferedImage buffImage, String extension,
      String url) {
    var collector = new ImageCollector();
    collector.setName(name);
    collector.setImageURL(url);
    em.persist(collector);
    var qvga = storeImageVariant(scaleImage(buffImage, 320), extension, ImageVariant.Variant.QVGA,
        collector);
    var vga = storeImageVariant(scaleImage(buffImage, 640), extension, ImageVariant.Variant.VGA,
        collector);
    var xga = storeImageVariant(scaleImage(buffImage, 1024), extension, ImageVariant.Variant.XGA,
        collector);
    var sxga = storeImageVariant(scaleImage(buffImage, 1280), extension, ImageVariant.Variant.SXGA,
        collector);
    var hd = storeImageVariant(scaleImage(buffImage, 720), extension, ImageVariant.Variant.HD,
        collector);
    var fhd = storeImageVariant(scaleImage(buffImage, 1920), extension, ImageVariant.Variant.FHD,
        collector);
    var fourK = storeImageVariant(scaleImage(buffImage, 3840), extension,
        ImageVariant.Variant.FOURK, collector);
    var orginal = storeImageVariant(buffImage, extension, ImageVariant.Variant.ORIGINAL, collector);
    collector.setQvga(qvga);
    collector.setVga(vga);
    collector.setXga(xga);
    collector.setSxga(sxga);
    collector.setHd(hd);
    collector.setFhd(fhd);
    collector.setFourK(fourK);
    collector.setOrginal(orginal);
    collector.setQvgaWebp(storeWebpImageVariant(qvga));
    collector.setVgaWebp(storeWebpImageVariant(vga));
    collector.setXgaWebp(storeWebpImageVariant(xga));
    collector.setSxgaWebp(storeWebpImageVariant(sxga));
    collector.setHdWebp(storeWebpImageVariant(hd));
    collector.setFhdWebp(storeWebpImageVariant(fhd));
    collector.setFourKWebp(storeWebpImageVariant(fourK));
    collector.setOrginalWebp(storeWebpImageVariant(orginal));
    return collector;
  }

  private ImageVariant storeImageVariant(BufferedImage buffImage, String extension, Variant variant,
      ImageCollector collector) {
    String hash = UUID.randomUUID().toString().replace('-', 'x');
    String path = properties.getProperty("dms.root.path") + File.separator
        + "images"
        + File.separator
        + hash.substring(0, 1)
        + File.separator
        + hash.substring(1, 2)
        + File.separator;
    int size = 0;
    try {
      final File file = fileDescriptorService.createEmptyFileOnDisc(path + hash + "." + extension);
      ImageIO.write(buffImage, extension, file);
      logger.log(Logger.Level.DEBUG, "Saved file " + extension + " of size" + size);
    } catch (Exception ioe) {
      throw new RuntimeException("File NOT stored: " + ioe.getMessage(), ioe);
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

  private ImageVariant storeWebpImageVariant(ImageVariant source) {
    final var path = source.getPath();
    final var hash = source.getHash();
    final String extension = "webp";
    final File webpFile =
        fileDescriptorService.createEmptyFileOnDisc(path + hash + "." + extension);
    WebpIO.create().toWEBP(new File(path + hash + "." + source.getExtension()), webpFile);
    logger.log(Logger.Level.DEBUG, "Saved file webp");
    ImageVariant image = new ImageVariant();
    image.setPath(path);
    image.setHash(hash);
    image.setExtension(extension);
    image.setCollector(source.getCollector());
    image.setVariant(ImageVariant.Variant.valueOf(source.getVariant().name() + "_WEBP"));
    em.persist(image);
    return image;
  }

  private BufferedImage scaleImage(BufferedImage buffImage, int width) {
    var img = buffImage.getScaledInstance(width, -1, BufferedImage.SCALE_SMOOTH);
    width = img.getWidth(null);
    var height = img.getHeight(null);
    int imageType = buffImage.getType();
    if(imageType == 0) imageType = 5;
    var bImg = new BufferedImage(width, height, imageType);
    var g2d = bImg.createGraphics();
    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    g2d.setRenderingHint(RenderingHints.KEY_RESOLUTION_VARIANT,
        RenderingHints.VALUE_RESOLUTION_VARIANT_SIZE_FIT);
    g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
        RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
        RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    g2d.drawImage(img, 0, 0, width, height, null);
    g2d.dispose();
    return bImg;
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
