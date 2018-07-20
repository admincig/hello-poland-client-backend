package pl.hellopoland.imagevariant;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import pl.hellopoland.ModelSuperclass;
import pl.hellopoland.image.Image;

/**
 * Entity implementation class for Entity: ImageVariant
 *
 */
@Entity

public class ImageVariant extends ModelSuperclass {
	private static final long serialVersionUID = 1L;

	@NotNull
	private String variant;

	@NotNull
	@ManyToOne
	private Image image;

	private String variantURL;

	public String getVariant() {
		return variant;
	}

	public void setVariant(String variant) {
		this.variant = variant;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String getVariantURL() {
		return variantURL;
	}

	public void setVariantURL(String variantURL) {
		this.variantURL = variantURL;
	}

}
