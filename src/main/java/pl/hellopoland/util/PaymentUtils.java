package pl.hellopoland.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class PaymentUtils {

  public static Map<String, String> queryToMap(String query) throws UnsupportedEncodingException {
    Map<String, String> query_pairs = new LinkedHashMap<String, String>();
    String[] pairs = query.split("&");
    for (String pair : pairs) {
      int idx = pair.indexOf("=");
      query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
          URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
    }
    return query_pairs;
  }

  public static String mapToQuery(Map<String, String> map) {
    StringBuilder string = new StringBuilder();

    for (Map.Entry<String, String> entry : map.entrySet()) {
      string.append(entry.getKey());
      string.append("=");
      string.append(entry.getValue());
      string.append("&");
    }
    return string.toString();
  }

  public static String MD5(String string) {
    try {
      java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
      byte[] array = md.digest(string.getBytes());
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < array.length; ++i) {
        sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
      }
      return sb.toString();
    } catch (java.security.NoSuchAlgorithmException e) {
    }
    return null;
  }
}
