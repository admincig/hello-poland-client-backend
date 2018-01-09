package pl.hellopoland.model;

import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class Sight extends ModelSuperclass {
  private static final long serialVersionUID = -34796485244638912L;

  @NotNull
  private String name;
  @ManyToOne
  private Image mainImage;
  @OneToMany(mappedBy = "sight")
  private Collection<Ticket> tickets;
  private String lead;
  @Column(columnDefinition = "text")
  private String description;
  private Integer minPrice;
  private Float score;

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

  public Collection<Ticket> getTickets() {
    return tickets;
  }

  public void setTickets(Collection<Ticket> tickets) {
    this.tickets = tickets;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getMinPrice() {
    return minPrice;
  }

  public void setMinPrice(Integer minPrice) {
    this.minPrice = minPrice;
  }

  public Float getScore() {
    return score;
  }

  public void setScore(Float score) {
    this.score = score;
  }

  public String getLead() {
    return lead;
  }

  public void setLead(String lead) {
    this.lead = lead;
  }

}
