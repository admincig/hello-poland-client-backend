package pl.hellopoland.order;

import java.util.Collection;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import pl.hellopoland.ModelSuperclass;
import pl.hellopoland.sight.Sight;

@Entity
public class OrderSightEntry extends ModelSuperclass {
  private static final long serialVersionUID = 3650049507552718840L;

  @ManyToOne(optional = false)
  private Sight sight;
  @OneToMany(mappedBy = "sightEntry")
  private Collection<OrderEntry> entries;
  @ManyToOne(optional = false)
  private Order order;


  public Sight getSight() {
    return sight;
  }

  public void setSight(Sight sight) {
    this.sight = sight;
  }

  public Collection<OrderEntry> getEntries() {
    return entries;
  }

  public void setEntries(Collection<OrderEntry> entries) {
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
    return entries.stream().collect(Collectors.summingInt(OrderEntry::getSum));
  }

}
