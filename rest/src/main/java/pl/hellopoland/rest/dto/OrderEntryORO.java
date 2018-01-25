package pl.hellopoland.rest.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import pl.hellopoland.order.OrderEntry;

public class OrderEntryORO {

  private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

  public String name;
  public String date;
  public String sightName;
  public Integer quantity;
  public List<String> numbers;

  public OrderEntryORO(OrderEntry oe) {
    this.date = df.format(oe.getDate());
    this.numbers = oe.getNumbers();
    this.quantity = oe.getQuantity();
    this.name = oe.getName();
    this.sightName = oe.getSightEntry().getSight().getName();
  }
}
