package pl.hellopoland.sight;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import pl.hellopoland.ModelSuperclass;

@Entity
public class Ticket extends ModelSuperclass {
  private static final long serialVersionUID = 574062027966116452L;

  @NotNull
  private String name;
  @NotNull
  private Integer price;
  @ManyToOne(optional = false)
  private Sight sight;
  @NotNull
  private boolean predefinedDate;
  private Date date;
  private Long externalId;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public Sight getSight() {
    return sight;
  }

  public void setSight(Sight sight) {
    this.sight = sight;
  }

  public boolean isPredefinedDate() {
    return predefinedDate;
  }

  public void setPredefinedDate(boolean predefinedDate) {
    this.predefinedDate = predefinedDate;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Long getExternalId() {
    return externalId;
  }

  public void setExternalId(Long externalId) {
    this.externalId = externalId;
  }

}
