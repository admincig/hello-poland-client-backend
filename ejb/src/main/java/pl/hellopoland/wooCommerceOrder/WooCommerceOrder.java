package pl.hellopoland.wooCommerceOrder;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked"})
public class WooCommerceOrder {

  private Map<String, Object> map = new LinkedHashMap<>();

  public Map<String, Object> getAsMap() {
    return map;
  }

  public void setPaymentMethod(String payment_method) {
    map.put("payment_method", payment_method);
  }

  public String getPaymentMethod() {
    return (String) map.get("payment_method");
  }

  public void setPaymentMethodTitle(String payment_method_title) {
    map.put("payment_method_title", payment_method_title);
  }

  public String getPaymentMethodTitle() {
    return (String) map.get("payment_method_title");
  }

  public void set_setPaid(boolean set_paid) {
    map.put("set_paid", set_paid);
  }

  public boolean get_setPaid() {
    return (boolean) map.get("set_paid");
  }

  public void addBilling(String first_name, String last_name, String address_1, String address_2,
      String city, String state, String postcode, String country, String email, String phone) {
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

  public LinkedHashMap<String, Object> getBilling() {
    return (LinkedHashMap<String, Object>) map.get("billing");
  }

  public void addShipping(String first_name, String last_name, String address_1, String address_2,
      String city, String state, String postcode, String country) {
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

  public LinkedHashMap<String, Object> getShipping() {
    return (LinkedHashMap<String, Object>) map.get("shipping");
  }

  public void addLineItem(int productId, int quantity) {
    if (!map.containsKey("line_items")) {
      map.put("line_items", new ArrayList<Map<String, Object>>());
    }
    Map<String, Object> item = new LinkedHashMap<>();
    item.put("product_id", productId);
    item.put("quantity", quantity);
    ((List<Map<String, Object>>) map.get("line_items")).add(item);
  }

  public List<Map<String, Object>> getLineItems() {
    return (List<Map<String, Object>>) map.get("line_items");
  }

  public void addShippingLine(String method_id, String method_title) {
    if (!map.containsKey("shipping_lines")) {
      map.put("shipping_lines", new ArrayList<Map<String, Object>>());
    }
    Map<String, Object> shipping_line = new LinkedHashMap<>();
    shipping_line.put("method_id", method_id);
    shipping_line.put("method_title", method_title);
    ((List<Map<String, Object>>) map.get("shipping_line")).add(shipping_line);
  }

  public List<Map<String, Object>> getShippingLines() {
    return (List<Map<String, Object>>) map.get("shipping_line");
  }
}
