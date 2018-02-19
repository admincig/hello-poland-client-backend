package pl.hellopoland.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.icoderman.woocommerce.EndpointBaseType;
import com.icoderman.woocommerce.WooCommerce;
import com.icoderman.woocommerce.WooCommerceAPI;
import com.icoderman.woocommerce.oauth.OAuthConfig;
import pl.hellopoland.image.Image;
import pl.hellopoland.order.OrderDateEntry;
import pl.hellopoland.order.OrderDetails;
import pl.hellopoland.order.OrderEntry;
import pl.hellopoland.order.OrderSightEntry;
import pl.hellopoland.sight.Sight;
import pl.hellopoland.sight.Ticket;

@SuppressWarnings({"unchecked", "rawtypes"})
public class Woo {
  private static Logger logger = Logger.getLogger(Woo.class.getName());

  private APIConnector connector;
  private OrderPlacer orderPlacer;
  private Importer importer;

  public Woo(String url, String key, String secret) {
    connector = new APIConnector(url, key, secret);
    orderPlacer = new OrderPlacer();
    importer = new Importer();
  }

  public boolean checkAvailability(int productId) {
    return orderPlacer.checkAvailability(productId);
  }

  public Map<String, Object> placeOrder(OrderSightEntry ose) {
    OrderPlacer.Order order = orderPlacer.new Order();
    OrderDetails details = ose.getOrder().getDetails();
    order.addBilling(details.getFirstName(), details.getLastName(), "", "", details.getCity(), "",
        "", details.getCountry(), details.getEmail(), details.getPhone());

    for (OrderDateEntry ode : ose.getEntries()) {
      for (OrderEntry oe : ode.getEntries()) {
        order.addLineItem(oe.getExternalId().intValue(), oe.getQuantity());
      }
    }
    return orderPlacer.place(order);
  }

  public List<Sight> importSights() {
    return importer.run();
  }

  private class Importer {

    public List<Sight> run() {
      List<Sight> listOfSights = new ArrayList<Sight>();
      List<Map<String, Object>> list = connector.allProducts();

      for (Map<String, Object> map : list) {
        Sight sight = new Sight();
        sight.setName((String) map.get("name"));
        sight.setDescription(Utils.clearHtml((String) map.get("description")));
        sight.setMinPrice((int) (100 * Double.valueOf((String) map.get("price".toString()))));

        Ticket ticket = new Ticket();
        ticket.setName("Normalny");
        ticket.setSight(sight);
        ticket.setPrice(sight.getMinPrice());
        ticket.setPredefinedDate(true);
        ticket.setExternalId(Long.valueOf(map.get("id").toString()));
        ticket.setDate(Utils.getDateFromSightName((String) map.get("name")));
        sight.setTickets(new ArrayList<>());
        sight.getTickets().add(ticket);

        List listOfImages = (List) map.get("images");
        Map mapOfOneImage = (Map) listOfImages.get(0);
        Image image = new Image();
        image.setImageURL((String) mapOfOneImage.get("src"));
        sight.setMainImage(image);

        listOfSights.add(sight);
      }
      return listOfSights;
    }
  }

  private class OrderPlacer {

    private Map<String, Object> place(Order order) {
      return connector.createOrder(order);
    }

    private boolean checkAvailability(int id) {
      return connector.checkAvailability(id);
    }

    private class Order {

      private Map<String, Object> map = new LinkedHashMap<>();

      private Map<String, Object> getAsMap() {
        return map;
      }

      private void setPaymentMethod(String payment_method) {
        map.put("payment_method", payment_method);
      }

      private String getPaymentMethod() {
        return (String) map.get("payment_method");
      }

      private void setPaymentMethodTitle(String payment_method_title) {
        map.put("payment_method_title", payment_method_title);
      }

      private String getPaymentMethodTitle() {
        return (String) map.get("payment_method_title");
      }

      private void set_setPaid(boolean set_paid) {
        map.put("set_paid", set_paid);
      }

      private boolean get_setPaid() {
        return (boolean) map.get("set_paid");
      }

      private void addBilling(String first_name, String last_name, String address_1,
          String address_2, String city, String state, String postcode, String country,
          String email, String phone) {
        if (!map.containsKey("billing")) {
          map.put("billing", new LinkedHashMap<>());
        }
        ((Map<String, Object>) map.get("billing")).put("first_name", first_name);
        ((Map<String, Object>) map.get("billing")).put("last_name", last_name);
        ((Map<String, Object>) map.get("billing")).put("address_1", address_1);
        ((Map<String, Object>) map.get("billing")).put("address_2", address_2);
        ((Map<String, Object>) map.get("billing")).put("city", city);
        ((Map<String, Object>) map.get("billing")).put("state", state);
        ((Map<String, Object>) map.get("billing")).put("postcode", postcode);
        ((Map<String, Object>) map.get("billing")).put("country", country);
        ((Map<String, Object>) map.get("billing")).put("email", email);
        ((Map<String, Object>) map.get("billing")).put("phone", phone);
      }

      private LinkedHashMap<String, Object> getBilling() {
        return (LinkedHashMap<String, Object>) map.get("billing");
      }

      private void addShipping(String first_name, String last_name, String address_1,
          String address_2, String city, String state, String postcode, String country) {
        if (!map.containsKey("shipping")) {
          map.put("shipping", new LinkedHashMap<>());
        }
        ((Map<String, Object>) map.get("shipping")).put("first_name", first_name);
        ((Map<String, Object>) map.get("shipping")).put("last_name", last_name);
        ((Map<String, Object>) map.get("shipping")).put("address_1", address_1);
        ((Map<String, Object>) map.get("shipping")).put("address_2", address_2);
        ((Map<String, Object>) map.get("shipping")).put("city", city);
        ((Map<String, Object>) map.get("shipping")).put("state", state);
        ((Map<String, Object>) map.get("shipping")).put("postcode", postcode);
        ((Map<String, Object>) map.get("shipping")).put("country", country);
      }

      private LinkedHashMap<String, Object> getShipping() {
        return (LinkedHashMap<String, Object>) map.get("shipping");
      }

      private void addLineItem(int productId, int quantity) {
        if (!map.containsKey("line_items")) {
          map.put("line_items", new ArrayList<Map<String, Object>>());
        }
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("product_id", productId);
        item.put("quantity", quantity);
        ((List<Map<String, Object>>) map.get("line_items")).add(item);
      }

      private List<Map<String, Object>> getLineItems() {
        return (List<Map<String, Object>>) map.get("line_items");
      }

      private void addShippingLine(String method_id, String method_title) {
        if (!map.containsKey("shipping_lines")) {
          map.put("shipping_lines", new ArrayList<Map<String, Object>>());
        }
        Map<String, Object> shipping_line = new LinkedHashMap<>();
        shipping_line.put("method_id", method_id);
        shipping_line.put("method_title", method_title);
        ((List<Map<String, Object>>) map.get("shipping_lines")).add(shipping_line);
      }

      private List<Map<String, Object>> getShippingLines() {
        return (List<Map<String, Object>>) map.get("shipping_lines");
      }
    }

  }

  private class APIConnector {
    private WooCommerce client;

    private APIConnector(String url, String key, String secret) {
      this.client = new WooCommerceAPI(new OAuthConfig(url, key, secret));
    }

    private boolean checkAvailability(int id) {
      Map productProperties = client.get(EndpointBaseType.PRODUCTS.getValue(), id);
      return productProperties.get("in_stock").equals(true);
    }

    private Map createOrder(Woo.OrderPlacer.Order order) {
      return client.create(EndpointBaseType.ORDERS.getValue(), order.getAsMap());
    }

    private List allProducts() {
      return client.getAll(EndpointBaseType.PRODUCTS.getValue());
    }
  }



  private static class Utils {
    private static Date getDateFromSightName(String text) {
      SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy");
      String regex =
          ".*?((?:(?:[0-2]?\\d{1})|(?:[3][01]{1}))[-:\\/.](?:[0]?[1-9]|[1][012])[-:\\/.](?:(?:\\d{1}\\d{1})))(?![\\d])";

      // SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
      // String regex =
      // ".*?((?:(?:[0-2]?\\d{1})|(?:[3][01]{1}))[-:\\/.](?:[0]?[1-9]|[1][012])[-:\\/.](?:(?:[1]{1}\\d{1}\\d{1}\\d{1})|(?:[2]{1}\\d{3})))(?![\\d])(\\s+)((?:(?:[0-1][0-9])|(?:[2][0-3])|(?:[0-9])):(?:[0-5][0-9])(?::[0-5][0-9])?(?:\\s?(?:am|AM|pm|PM))?)";

      Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
      Matcher m = p.matcher(text);

      if (m.find()) {
        String dateString = m.group(1);
        // String dateString = m.group(1) + m.group(2) + m.group(3);
        try {
          return formatter.parse(dateString);
        } catch (ParseException e) {
          logger.warning(e.getMessage());
        }
      }
      return null;
    }

    private static String clearHtml(String text) {
      return text.replaceAll("\\<[^>]*>", "").replaceAll("&nbsp;", " ");
    }
  }
}
