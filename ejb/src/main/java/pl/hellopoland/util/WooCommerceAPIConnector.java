package pl.hellopoland.util;

import java.util.List;
import java.util.Map;
import com.icoderman.woocommerce.EndpointBaseType;
import com.icoderman.woocommerce.WooCommerce;
import com.icoderman.woocommerce.WooCommerceAPI;
import com.icoderman.woocommerce.oauth.OAuthConfig;

@SuppressWarnings("rawtypes")
public class WooCommerceAPIConnector {
  private WooCommerce client;

  public WooCommerceAPIConnector(String url, String key, String secret) {
    this.client = new WooCommerceAPI(new OAuthConfig(url, key, secret));
  }

  public List allProducts() {
    return client.getAll(EndpointBaseType.PRODUCTS.getValue());
  }

  public boolean checkAvailability(int id) {
    Map productProperties = client.get(EndpointBaseType.PRODUCTS.getValue(), id);
    return productProperties.get("in_stock").equals(true);
  }

  public Map order(int id) {
    return client.get(EndpointBaseType.ORDERS.getValue(), id);
  }

  public List allOrders() {
    return client.getAll(EndpointBaseType.ORDERS.getValue());
  }

  public Map createOrder(Map<String, Object> map) {
    return client.create(EndpointBaseType.ORDERS.getValue(), map);
  }
}
