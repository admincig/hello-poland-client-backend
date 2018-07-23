package pl.hellopoland.bo;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class ImageCollector extends ModelSuperclass {
  private static final long serialVersionUID = 1L;

  private String imageURL;

  @OneToOne
  private ImageVariant orginal;

  @OneToOne
  private ImageVariant qvga;

  @OneToOne
  private ImageVariant vga;

  @OneToOne
  private ImageVariant hd;

  @OneToOne
  private ImageVariant xga;

  @OneToOne
  private ImageVariant sxga;

  @OneToOne
  private ImageVariant fhd;

  @OneToOne
  private ImageVariant fourK;

  public String getImageURL() {
    return imageURL;
  }

  public void setImageURL(String imageURL) {
    this.imageURL = imageURL;
  }

  public ImageVariant getOrginal() {
    return orginal;
  }

  public void setOrginal(ImageVariant orginal) {
    this.orginal = orginal;
  }

  public ImageVariant getQvga() {
    return qvga;
  }

  public void setQvga(ImageVariant qvga) {
    this.qvga = qvga;
  }

  public ImageVariant getVga() {
    return vga;
  }

  public void setVga(ImageVariant vga) {
    this.vga = vga;
  }

  public ImageVariant getHd() {
    return hd;
  }

  public void setHd(ImageVariant hd) {
    this.hd = hd;
  }

  public ImageVariant getXga() {
    return xga;
  }

  public void setXga(ImageVariant xga) {
    this.xga = xga;
  }

  public ImageVariant getSxga() {
    return sxga;
  }

  public void setSxga(ImageVariant sxga) {
    this.sxga = sxga;
  }

  public ImageVariant getFhd() {
    return fhd;
  }

  public void setFhd(ImageVariant fhd) {
    this.fhd = fhd;
  }

  public ImageVariant getFourK() {
    return fourK;
  }

  public void setFourK(ImageVariant fourK) {
    this.fourK = fourK;
  }

}
