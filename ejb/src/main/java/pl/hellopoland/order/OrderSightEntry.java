package pl.hellopoland.order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import pl.hellopoland.ModelSuperclass;
import pl.hellopoland.sight.Sight;

@Entity
public class OrderSightEntry extends ModelSuperclass {
  private static final long serialVersionUID = 3650049507552718840L;

  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  private Sight sight;
  @OneToMany(mappedBy = "sightEntry")
  private Collection<OrderSightDateEntry> entries;
  @ManyToOne(optional = false)
  private Order order;


  public Sight getSight() {
    return sight;
  }

  public void setSight(Sight sight) {
    this.sight = sight;
  }

  public Collection<OrderSightDateEntry> getEntries() {
    return entries;
  }

  public void setEntries(Collection<OrderSightDateEntry> entries) {
    this.entries = entries;
  }


  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public Integer getSum() {
    if (entries == null) {
      return 0;
    }
    List<OrderEntry> entries = new ArrayList<>();
    for (OrderSightDateEntry e : this.entries) {
      entries.addAll(e.getEntries());
    }
    return entries.stream().collect(Collectors.summingInt(OrderEntry::getSum));
  }

}
