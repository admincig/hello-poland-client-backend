package pl.hellopoland.image;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import pl.hellopoland.ModelSuperclass;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "hash", name = "image_hash_unique"))
public class ImageVariant extends ModelSuperclass {

	private static final long serialVersionUID = -1655719974465476465L;

	public enum Variant {
		QVGA, VGA, XGA, SXGA, HD, FHD, FOURK, ORIGINAL
	}

	@NotNull
	private Variant variant;
	@NotNull
	@OneToOne
	private ImageCollector collector;
	@NotNull
	private String path;
	@NotNull
	private String hash;
	@NotNull
	private String extension;

	public Variant getVariant() {
		return variant;
	}

	public void setVariant(Variant variant) {
		this.variant = variant;
	}

	public ImageCollector getCollector() {
		return collector;
	}

	public void setCollector(ImageCollector collector) {
		this.collector = collector;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	@Transient
	public String getDownloadUrl() {
		return System.getProperty("base.url") + "/images/" + hash + "." + extension;
	}

}
