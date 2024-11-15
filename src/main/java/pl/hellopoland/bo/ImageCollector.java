package pl.hellopoland.bo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class ImageCollector extends ModelSuperclass {
  private static final long serialVersionUID = 1L;

  private String name;
  @ManyToOne
  private Partner partner;

  private String imageURL;

  @OneToOne(cascade = CascadeType.REMOVE)
  private ImageVariant orginal;

  @OneToOne(cascade = CascadeType.REMOVE)
  private ImageVariant qvga;

  @OneToOne(cascade = CascadeType.REMOVE)
  private ImageVariant vga;

  @OneToOne(cascade = CascadeType.REMOVE)
  private ImageVariant hd;

  @OneToOne(cascade = CascadeType.REMOVE)
  private ImageVariant xga;

  @OneToOne(cascade = CascadeType.REMOVE)
  private ImageVariant sxga;

  @OneToOne(cascade = CascadeType.REMOVE)
  private ImageVariant fhd;

  @OneToOne(cascade = CascadeType.REMOVE)
  private ImageVariant fourK;

  @OneToOne(cascade = CascadeType.REMOVE)
  private ImageVariant orginalWebp;

  @OneToOne(cascade = CascadeType.REMOVE)
  private ImageVariant qvgaWebp;

  @OneToOne(cascade = CascadeType.REMOVE)
  private ImageVariant vgaWebp;

  @OneToOne(cascade = CascadeType.REMOVE)
  private ImageVariant hdWebp;

  @OneToOne(cascade = CascadeType.REMOVE)
  private ImageVariant xgaWebp;

  @OneToOne(cascade = CascadeType.REMOVE)
  private ImageVariant sxgaWebp;

  @OneToOne(cascade = CascadeType.REMOVE)
  private ImageVariant fhdWebp;

  @OneToOne(cascade = CascadeType.REMOVE)
  private ImageVariant fourKWebp;

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

  public ImageVariant getOrginalWebp() {
    return orginalWebp;
  }

  public void setOrginalWebp(ImageVariant orginalWebp) {
    this.orginalWebp = orginalWebp;
  }

  public ImageVariant getQvgaWebp() {
    return qvgaWebp;
  }

  public void setQvgaWebp(ImageVariant qvgaWebp) {
    this.qvgaWebp = qvgaWebp;
  }

  public ImageVariant getVgaWebp() {
    return vgaWebp;
  }

  public void setVgaWebp(ImageVariant vgaWebp) {
    this.vgaWebp = vgaWebp;
  }

  public ImageVariant getHdWebp() {
    return hdWebp;
  }

  public void setHdWebp(ImageVariant hdWebp) {
    this.hdWebp = hdWebp;
  }

  public ImageVariant getXgaWebp() {
    return xgaWebp;
  }

  public void setXgaWebp(ImageVariant xgaWebp) {
    this.xgaWebp = xgaWebp;
  }

  public ImageVariant getSxgaWebp() {
    return sxgaWebp;
  }

  public void setSxgaWebp(ImageVariant sxgaWebp) {
    this.sxgaWebp = sxgaWebp;
  }

  public ImageVariant getFhdWebp() {
    return fhdWebp;
  }

  public void setFhdWebp(ImageVariant fhdWebp) {
    this.fhdWebp = fhdWebp;
  }

  public ImageVariant getFourKWebp() {
    return fourKWebp;
  }

  public void setFourKWebp(ImageVariant fourKWebp) {
    this.fourKWebp = fourKWebp;
  }

  public Partner getPartner() {
    return partner;
  }

  public void setPartner(Partner partner) {
    this.partner = partner;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
