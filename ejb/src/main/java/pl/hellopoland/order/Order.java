package pl.hellopoland.order;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import pl.hellopoland.ModelSuperclass;

@Entity
@Table(name = "orders")
public class Order extends ModelSuperclass {
  private static final long serialVersionUID = 3824722747352862154L;

  @OneToMany(mappedBy = "sight")
  private Collection<OrderSightEntry> entries;
  @NotNull
  private String hash;
  @Embedded
  private OrderDetails details;

  public Collection<OrderSightEntry> getEntries() {
    return entries;
  }

  public void setEntries(Collection<OrderSightEntry> entries) {
    this.entries = entries;
  }

  public String getHash() {
    return hash;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  public OrderDetails getDetails() {
    return details;
  }

  public void setDetails(OrderDetails details) {
    this.details = details;
  }

  public Integer getSum() {
    if (entries == null) {
      return 0;
    }
    return entries.stream().collect(Collectors.summingInt(OrderSightEntry::getSum));
  }

  public void generateHash() {
    this.hash = UUID.randomUUID().toString();
  }
}
