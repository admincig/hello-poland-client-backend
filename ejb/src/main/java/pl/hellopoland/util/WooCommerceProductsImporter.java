package pl.hellopoland.util;

import java.util.List;
import pl.hellopoland.sight.Sight;

public class WooCommerceProductsImporter {

  public static List<Sight> run(String url, String key, String secret) {
    // TODO ściągnij z woo produkty, stwórz z nich Sighty
    // TODO wymyśl co z obrazkami


    // XXX wynik działania tej metody przekażemy do odpowiedniego serwisu celem perzystencji
    return null;
  }


  public static void main(String[] args) {
    String url = "http://woo.hello-poland.pl";
    String key = "ck_5233b79180ff8b7bef81b28fe7222b2eb2b37ebe";
    String secret = "cs_2c96f574d729e8bde7b71d96007c172bc12244d9";
    System.out.println(run(url, key, secret));
  }
}
