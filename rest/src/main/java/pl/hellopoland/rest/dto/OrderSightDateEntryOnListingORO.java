package pl.hellopoland.rest.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import pl.hellopoland.order.OrderSightDateEntry;

public class OrderSightDateEntryOnListingORO {

  private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

  public Long id;
  public SightSimpleRO sight;
  public String date;
  public List<OrderEntryOnListingORO> entries;

  public OrderSightDateEntryOnListingORO(OrderSightDateEntry osde) {
    this.id = osde.getId();
    this.date = df.format(osde.getDate());
    this.sight = new SightSimpleRO(osde.getSightEntry().getSight());
    this.entries =
        osde.getEntries().stream().map(OrderEntryOnListingORO::new).collect(Collectors.toList());
  }
}
