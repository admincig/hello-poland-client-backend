package pl.hellopoland.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Facility extends ModelSuperclass {
	private static final long serialVersionUID = -34796485244638912L;

	@NotNull
	private String name;

	@ManyToOne
	private Image mainImage;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Image getMainImage() {
		return mainImage;
	}

	public void setMainImage(Image mainImage) {
		this.mainImage = mainImage;
	}

}
