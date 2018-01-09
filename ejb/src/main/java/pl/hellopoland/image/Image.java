package pl.hellopoland.image;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import pl.hellopoland.ModelSuperclass;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "hash", name = "image_hash_unique"))
public class Image extends ModelSuperclass {
  private static final long serialVersionUID = -1655719974465476465L;

  @NotNull
  private String path;
  @NotNull
  private String hash;
  @NotNull
  private String extension;

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


}
