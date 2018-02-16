package pl.hellopoland.wooCommerceOrder;

import java.util.LinkedHashMap;
import java.util.Map;
import pl.hellopoland.util.WooCommerceAPIConnector;

@SuppressWarnings("unchecked")
public class WooCommerceOrderCreator {

  private static WooCommerceAPIConnector wooCommerceAPIConnector;

  public static void setConnector(String url, String key, String secret) {
    wooCommerceAPIConnector = new WooCommerceAPIConnector(url, key, secret);
  }

  public static Map<String, Object> createOrder(String url, String key, String secret) {
    setConnector(url, key, secret);
    WooCommerceOrder order = new WooCommerceOrder();

    // VALIDATION
    LinkedHashMap<String, Object> mapWithData =
        (LinkedHashMap<String, Object>) order.getAsMap().get("data");
    Integer statusCode = (Integer) mapWithData.get("status");
    if (statusCode >= 300) {
      // throw new Exception();
      return null;
    }

    System.out.println(wooCommerceAPIConnector.createOrder(order.getAsMap()));
    return wooCommerceAPIConnector.createOrder(order.getAsMap());
  }

  public static void getAllOrders(String url, String key, String secret) {
    setConnector(url, key, secret);
    wooCommerceAPIConnector.allOrders();

    System.out.println(wooCommerceAPIConnector.allOrders());
  }

  public static void main(String[] args) {
    String url = "http://woo.hello-poland.pl";
    String key = "ck_5233b79180ff8b7bef81b28fe7222b2eb2b37ebe";
    String secret = "cs_2c96f574d729e8bde7b71d96007c172bc12244d9";
    // System.out.println(run(url, key, secret));

    createOrder(url, key, secret);
    // System.out.print(createOrder(url, key, secret));



    getAllOrders(url, key, secret);
    System.out.println("koniec");

    // System.out.println(mapWithOrder());
  }

}
