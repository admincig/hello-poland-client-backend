package pl.hellopoland.rest.dto;

import java.util.List;
import java.util.stream.Collectors;
import pl.hellopoland.bo.OrderDateEntry;

public class OrderDateEntryOnListingORO {

  public Long id;
  public SightEventSimpleRO sight;
  public String date;
  public boolean wholeDay;
  public List<OrderEntryOnListingORO> entries;

  public OrderDateEntryOnListingORO(OrderDateEntry ode) {
    this.id = ode.getId();
    this.sight = new SightEventSimpleRO(ode.getSightEntry().getSightEvent());
    this.date = DtoUtils.df.format(ode.getDate());
    this.wholeDay = ode.getSightEntry().isWholeDay();
    this.entries =
        ode.getEntries().stream().map(OrderEntryOnListingORO::new).collect(Collectors.toList());
  }
}
