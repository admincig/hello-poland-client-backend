package pl.hellopoland.bo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class OrderSightEntry extends ModelSuperclass {

  private static final long serialVersionUID = 3650049507552718840L;

  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  private SightEvent sightEvent;
  @OneToMany(mappedBy = "sightEntry", cascade = CascadeType.REFRESH)
  private Collection<OrderDateEntry> entries;
  @ManyToOne(optional = false)
  private Order order;
  private String serialNumber;
  private Long externalId;


  public SightEvent getSightEvent() {
    return sightEvent;
  }

  public void setSightEvent(SightEvent sightEvent) {
    this.sightEvent = sightEvent;
  }

  public Collection<OrderDateEntry> getEntries() {
    return entries;
  }

  public void setEntries(Collection<OrderDateEntry> entries) {
    this.entries = entries;
  }


  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public Long getExternalId() {
    return externalId;
  }

  public void setExternalId(Long externalId) {
    this.externalId = externalId;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public Integer getSum() {
    if (entries == null) {
      return 0;
    }
    List<OrderEntry> entries = new ArrayList<>();
    for (OrderDateEntry e : this.entries) {
      entries.addAll(e.getEntries());
    }
    return entries.stream().collect(Collectors.summingInt(OrderEntry::getSum));
  }

  public void addEntry(OrderDateEntry entry) {
    if (this.getEntries() == null) {
      this.setEntries(new ArrayList<>());
    }
    this.getEntries().add(entry);
  }

}
