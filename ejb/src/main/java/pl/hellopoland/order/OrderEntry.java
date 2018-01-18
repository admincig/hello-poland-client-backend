package pl.hellopoland.order;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import pl.hellopoland.ModelSuperclass;
import pl.hellopoland.sight.Ticket;

@Entity
public class OrderEntry extends ModelSuperclass {
  private static final long serialVersionUID = -1590949806641422316L;

  @ManyToOne(optional = false)
  private OrderSightEntry sightEntry;
  @ManyToOne(optional = false)
  private Ticket ticket;
  @NotNull
  private Date date;
  @NotNull
  private Integer quantity;
  @NotNull
  private Integer unitPrice;


  public OrderSightEntry getSightEntry() {
    return sightEntry;
  }

  public void setSightEntry(OrderSightEntry sightEntry) {
    this.sightEntry = sightEntry;
  }

  public Ticket getTicket() {
    return ticket;
  }

  public void setTicket(Ticket ticket) {
    this.ticket = ticket;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Integer getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(Integer unitPrice) {
    this.unitPrice = unitPrice;
  }

  public Integer getSum() {
    return unitPrice * quantity;
  }

}
