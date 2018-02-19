package pl.hellopoland.rest.dto;

import java.util.List;
import java.util.stream.Collectors;
import pl.hellopoland.order.OrderDateEntry;

public class OrderDateEntryOnListingORO {
  public Long id;
  public SightSimpleRO sight;
  public String date;
  public List<OrderEntryOnListingORO> entries;

  public OrderDateEntryOnListingORO(OrderDateEntry ode) {
    this.id = ode.getId();
    this.date = DtoUtils.df.format(ode.getDate());
    this.sight = new SightSimpleRO(ode.getSightEntry().getSight());
    this.entries =
        ode.getEntries().stream().map(OrderEntryOnListingORO::new).collect(Collectors.toList());
  }
}
