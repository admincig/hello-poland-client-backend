package pl.hellopoland.rest.dto;

import java.util.List;
import java.util.stream.Collectors;
import pl.hellopoland.order.OrderDateEntry;

public class OrderSightDateEntryOnListingORO {
  public Long id;
  public SightSimpleRO sight;
  public String date;
  public List<OrderEntryOnListingORO> entries;

  public OrderSightDateEntryOnListingORO(OrderDateEntry osde) {
    this.id = osde.getId();
    this.date = DtoUtils.df.format(osde.getDate());
    this.sight = new SightSimpleRO(osde.getSightEntry().getSight());
    this.entries =
        osde.getEntries().stream().map(OrderEntryOnListingORO::new).collect(Collectors.toList());
  }
}
