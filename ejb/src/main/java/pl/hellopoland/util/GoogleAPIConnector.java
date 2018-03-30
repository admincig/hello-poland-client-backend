package pl.hellopoland.util;

import java.util.List;
import java.util.logging.Logger;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import pl.hellopoland.user.User;

public class GoogleAPIConnector {

  private Logger logger = Logger.getLogger(GoogleAPIConnector.class.getName());

  public static void main(String[] args) throws IllegalStateException, Exception {
    GoogleAPIConnector conn = new GoogleAPIConnector();
    conn.getUser(
        "eyJhbGciOiJSUzI1NiIsImtpZCI6IjM3NmVhMWUyZjRjOTM3YzMzM2QxZTI0YjU2NDczOGZjMDRjOTkwMDkifQ.eyJhenAiOiI4NzYyMTQ1MjA4MjUtc2U1MGFxMWplcnJyaTZtNTBlaWVpam9iYnM5amk0aWouYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI4NzYyMTQ1MjA4MjUtMXVtMG83cHBsZ3R2aWZycmp1NWcyMWwzYWtubmNra2guYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTY3NzIxMjE5NDUyNDQwNzk2MjEiLCJlbWFpbCI6ImphbnVzei5wcnp5Ynlsc2tpQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJleHAiOjE1MjIzOTQ2NjksImlzcyI6Imh0dHBzOi8vYWNjb3VudHMuZ29vZ2xlLmNvbSIsImlhdCI6MTUyMjM5MTA2OSwibmFtZSI6IkphbnVzeiBQcnp5Ynlsc2tpIiwicGljdHVyZSI6Imh0dHBzOi8vbGg1Lmdvb2dsZXVzZXJjb250ZW50LmNvbS8tRkY3WEhQcklwR28vQUFBQUFBQUFBQUkvQUFBQUFBQUFBQUEvQUNMR3lXQ2ZublRXXzVQdExtcVczal9DWW00cS1mNXJnUS9zOTYtYy9waG90by5qcGciLCJnaXZlbl9uYW1lIjoiSmFudXN6IiwiZmFtaWx5X25hbWUiOiJQcnp5Ynlsc2tpIn0.do9k520QdgR9DuQoEhhE3laeY0F4jzZBJM_EHpJSSSvTcii23h93jCPapqtd0YFPAVbuolbudqbd1FAttQ7WijQuZ95vWil4PJNWPwOmuWAn5Qqwj-CudEllQTH1MXtvgeZ4qcBZW2ulfutgOtv0mLNj2uLUgUT2aPM78-7_ytquTpjvHYh1fXpO-CZnJFi4pzN2czFVbb7lMViyODi7ZOVIw0_j87Rq_-IMJn7-4Qz_vHwcwM2YMOFdzqrgbEf2lMiNXWAA5YvKaZek1LgYphO8xOJEnAelfOuiW5xCcj57n-hm0sjWb9FAmzAmGsdZ9P8xAgI9LwanKxTCrjDs2g");
  }

  public User getUser(String idToken) throws IllegalStateException, Exception {
    GoogleIdTokenVerifier verifier =
        new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
            .setAudience(
                List.of("876214520825-se50aq1jerrri6m50eieijobbs9ji4ij.apps.googleusercontent.com",
                    "876214520825-8q9p0e30b9t3c3h3i6dl5pp3emjegfr9.apps.googleusercontent.com",
                    "876214520825-jsnb7u85kle6rnafonvitlq9n0ill06p.apps.googleusercontent.com",
                    "876214520825-1um0o7pplgtvifrrju5g21l3aknnckkh.apps.googleusercontent.com",
                    "876214520825-eodmqsked29157pqispetrmabrv6dd8m.apps.googleusercontent.com",
                    "876214520825-jsnb7u85kle6rnafonvitlq9n0ill06p.apps.googleusercontent.com",
                    "876214520825-9vun30k1bo423jcsfu38ddjcs5jmkfq6.apps.googleusercontent.com"))
            .setIssuer("https://accounts.google.com").build();
    logger.info("verifying idToken=" + idToken);
    GoogleIdToken token = verifier.verify(idToken);
    GoogleIdToken.Payload payload = token.getPayload();
    if (payload != null) {
      logger.info(payload.toPrettyString());
      User user = new User();
      user.setEmail(payload.getEmail());
      user.setName((String) payload.get("name"));
      user.setPicture((String) payload.get("picture"));
      user.setPassword("");
      return user;
    }
    return null;
  }

}
