package pl.hellopoland.bo;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class LibraryFile extends ModelSuperclass {

  private String name;
  @ManyToOne
  private Partner owner;
  @OneToOne
  private ImageCollector image;
  @OneToOne
  private FileDescriptor nonImage;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Partner getOwner() {
    return owner;
  }

  public void setOwner(Partner owner) {
    this.owner = owner;
  }

  public ImageCollector getImage() {
    return image;
  }

  public void setImage(ImageCollector image) {
    this.image = image;
  }

  public FileDescriptor getNonImage() {
    return nonImage;
  }

  public void setNonImage(FileDescriptor nonImage) {
    this.nonImage = nonImage;
  }
}
