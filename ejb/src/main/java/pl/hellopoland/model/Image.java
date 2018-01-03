package pl.hellopoland.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
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
