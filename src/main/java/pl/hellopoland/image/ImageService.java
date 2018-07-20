package pl.hellopoland.image;

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

import pl.hellopoland.ConflictingException;
import pl.hellopoland.ServiceSuperclass;
import pl.hellopoland.util.Imaged;

@LocalBean
@Stateless
public class ImageService extends ServiceSuperclass {

	public Image validateAndStoreImage(InputStream is, String extension, String url) {
		BufferedImage buffImage = validate(is);
		return storeImage(buffImage, extension, url);
	}

	private BufferedImage validate(InputStream is) {
		try {
			BufferedImage imageIO = ImageIO.read(is);
			logger.log(Level.INFO, "image width: " + imageIO.getWidth() + ", height: " + imageIO.getHeight());
			if (imageIO.getWidth() < 2000) {
				throw new ConflictingException("Image width must be a minimum of 2000px");
			}
			return imageIO;
		} catch (IOException e) {
			throw new ConflictingException("Failed to validate image", e);
		}
	}

	private Image storeImage(BufferedImage buffImage, String extension, String url) {
		String hash = UUID.randomUUID().toString().replace('-', 'x');
		String path = properties.getProperty("dms.root.path") + File.separator + hash.substring(0, 1) + File.separator
				+ hash.substring(1, 2) + File.separator;

		byte[] buf = new byte[10000];
		int size = 0;
		try {
			createEmptyFileOnDisc(path + hash + "." + extension);
			final File file = new File(path + hash + "." + extension);
			ImageIO.write(buffImage, extension, file);
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

	// TODO: discuss where and what exactly to do it:
	private BufferedImage scalImage(BufferedImage buffImage, String extension) {
		var width = 0;

		switch (extension) {
		case "qvga":
			width = 320;
			break;
		case "vga":
			width = 640;
			break;
		case "xga":
			width = 1024;
			break;
		case "sxga":
			width = 1280;
			break;
		case "fhd":
			width = 1920;
			break;
		case "fourK":
			width = 3840;
			break;
		default:
			width = buffImage.getWidth();
			break;
		}

		var img = buffImage.getScaledInstance(width, -1, BufferedImage.SCALE_DEFAULT);
		width = img.getWidth(null);
		var height = img.getHeight(null);
		var bImg = new BufferedImage(width, height, buffImage.getType());
		var g2d = bImg.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
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
		String path = em.createQuery("select path from Image where concat(hash, '.' ,extension)=:name", String.class)
				.setParameter("name", name).getSingleResult();
		return new File(path + name);
	}

	public Image downloadImage(String url) {
		Image im = null;
		if (url != null) {
			try {
				logger.log(Logger.Level.INFO, "Downloading image " + url);
				im = validateAndStoreImage(new URL(url).openConnection().getInputStream(), "jpg", url);
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
			Image boImage = bo.getMainImage();
			Image image = getOrDownload(importUrl);
			if (boImage == null || !boImage.getId().equals(image.getId())) {
				bo.setMainImage(image);
			}
		}
	}

	private Image getOrDownload(String importUrl) {
		try {
			var parts = importUrl.split("\\/");
			String name = parts[parts.length - 1];
			String hash = name.split("\\.")[0];
			return em.createQuery("from Image where imageURL=:url or hash=:hash", Image.class)
					.setParameter("url", importUrl).setParameter("hash", hash).getSingleResult();
		} catch (NoResultException e) {
			return downloadImage(importUrl);
		}

	}
}
