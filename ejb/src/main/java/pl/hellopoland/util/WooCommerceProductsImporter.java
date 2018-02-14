package pl.hellopoland.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pl.hellopoland.image.Image;
import pl.hellopoland.sight.Sight;

public class WooCommerceProductsImporter {

  @SuppressWarnings("unchecked")
  public static List<LinkedHashMap<String, Object>> getListOfProductMaps(String url, String key,
      String secret) {
    WooCommerceAPIConnector wooCommerceAPIConnector = new WooCommerceAPIConnector(url, key, secret);
    return wooCommerceAPIConnector.allProducts();
  }

  public static List<Sight> run(String url, String key, String secret) {

    List<Sight> listOfSights = new ArrayList<Sight>();
    List<LinkedHashMap<String, Object>> list = getListOfProductMaps(url, key, secret);

    for (int i = 0; i < list.size(); i++) {

      LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
      map = list.get(i);

      Sight sight = new Sight();
      sight.setExternalId(Long.valueOf(map.get("id").toString()));
      sight.setName((String) map.get("name"));
      sight.setDescription(withOutHTMLCode((String) map.get("description")));
      sight.setMinPrice(Integer.valueOf((String) map.get("price".toString())));

      @SuppressWarnings("rawtypes")
      List listOfImages = (List) map.get("images");
      @SuppressWarnings("rawtypes")
      Map mapOfOneImage = (Map) listOfImages.get(0);
      Image image = new Image();
      image.setImageURL((String) mapOfOneImage.get("src"));
      sight.setMainImage(image);
      sight.setDate(setDateFromSightName((String) map.get("name")));

      listOfSights.add(sight);
    }

    return listOfSights;
  }

  public static String withOutHTMLCode(String text) {
    return text.replaceAll("\\<[^>]*>", "").replaceAll("&nbsp;", " ");
  }

  public static Date setDateFromSightName(String text) {

    String re1 = ".*?";
    String re2 =
        "((?:(?:[0-2]?\\d{1})|(?:[3][01]{1}))[-:\\/.](?:[0]?[1-9]|[1][012])[-:\\/.](?:(?:\\d{1}\\d{1})))(?![\\d])";
    Pattern p = Pattern.compile(re1 + re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    Matcher m = p.matcher(text);
    String ddmmyy = "01.01.01";
    if (m.find()) {
      ddmmyy = m.group(1);
    }

    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy");
    try {
      Date date = formatter.parse(ddmmyy);
      return date;
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

}
