package pl.hellopoland.rest.dto;

import java.util.List;
import java.util.stream.Collectors;
import pl.hellopoland.order.OrderSightEntry;

public class SightWithOrderEntriesRO {
  public Long id;
  public String name;
  public String city;
  public List<OrderEntryORO> entries;

  public SightWithOrderEntriesRO(OrderSightEntry ose) {
    this.id = ose.getSight().getId();
    this.name = ose.getSight().getName();
    this.city = ose.getSight().getLocation().getCity();
    this.entries = ose.getEntries().stream().map(OrderEntryORO::new).collect(Collectors.toList());
  }
}
