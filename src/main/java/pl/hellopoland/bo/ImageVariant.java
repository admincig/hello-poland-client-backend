package pl.hellopoland.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"hash", "extension"},
    name = "image_hash_extension_unique"))
public class ImageVariant extends ModelSuperclass {

  private static final long serialVersionUID = -1655719974465476465L;

  public enum Variant {
    QVGA, QVGA_WEBP, VGA, VGA_WEBP, XGA, XGA_WEBP, SXGA, SXGA_WEBP, HD, HD_WEBP, FHD, FHD_WEBP, FOURK, FOURK_WEBP, ORIGINAL, ORIGINAL_WEBP
  }

  @NotNull
  @Enumerated(EnumType.STRING)
  private Variant variant;
  @OneToOne(fetch = FetchType.LAZY)
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
