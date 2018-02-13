package pl.hellopoland.order;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import pl.hellopoland.ModelSuperclass;

@Entity
public class OrderSightDateEntry extends ModelSuperclass {
  private static final long serialVersionUID = 2682096610461687109L;

  @NotNull
  private Date date;
  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  private OrderSightEntry sightEntry;
  @OneToMany(mappedBy = "dateEntry")
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
