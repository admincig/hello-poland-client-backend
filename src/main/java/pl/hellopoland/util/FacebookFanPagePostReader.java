package pl.hellopoland.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.ejb.Singleton;


@Singleton
public class FacebookFanPagePostReader {
  static final int LIMIT = 100;
  static final String FANPAGE_ID = "316534255768958";

  public static void main(String[] args) {
    try {
      var pageAccessToken =
          "EAADojZAMZB604BAPO42aYwS4k1kZBmZC0cCcz43cnzM9TZA6TQmuRqyDw5j0wFhSzk9QpJM9ZC3UkM7ZAu4z0EWMAt1ysrUflNX1Jr2aYoy46PKJvGwSe8B7rS5ioUf9g1eDUKaJgtufWIch5q1dCHMZBUlKW2oxKUShgUeMper3iOMYrO3wpFHq";

      var graph = "https://graph.facebook.com/" + FANPAGE_ID + "/published_posts?access_token="
          + pageAccessToken + "&limit=" + LIMIT;
      var graphURL = new URL(graph);
      HttpURLConnection myWebClient = (HttpURLConnection) graphURL.openConnection();
      String responseMessage = myWebClient.getResponseMessage();
      if (myWebClient.getResponseCode() != HttpURLConnection.HTTP_OK) {
        System.out.println(myWebClient.getResponseCode() + " " + responseMessage);
      } else {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = myWebClient.getInputStream();
        int r;
        while ((r = is.read()) != -1) {
          baos.write(r);
        }
        var response = new String(baos.toByteArray());
        is.close();
        baos.close();
        System.out.println(response);
      }
      myWebClient.disconnect();
    } catch (Exception e) {
      System.err.println(e);
    }
  }
}
