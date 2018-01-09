package pl.hellopoland.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Ticket extends ModelSuperclass {
  private static final long serialVersionUID = 574062027966116452L;

  @NotNull
  private String name;
  @NotNull
  private Integer price;
  @ManyToOne(optional = false)
  private Sight sight;


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

}
