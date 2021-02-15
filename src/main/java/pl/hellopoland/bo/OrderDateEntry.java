package pl.hellopoland.bo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
public class OrderDateEntry extends ModelSuperclass {

  private static final long serialVersionUID = 2682096610461687109L;

  @NotNull
  private Date date;
  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  private OrderSightEntry sightEntry;
  @OneToMany(mappedBy = "dateEntry", cascade = CascadeType.REFRESH)
  private List<OrderEntry> entries;
  @NotNull
  private boolean deleted;

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public OrderSightEntry getSightEntry() {
    return sightEntry;
  }

  public void setSightEntry(OrderSightEntry sightEntry) {
    this.sightEntry = sightEntry;
  }

  public List<OrderEntry> getEntries() {
    return entries;
  }

  public void setEntries(List<OrderEntry> entries) {
    this.entries = entries;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

}
