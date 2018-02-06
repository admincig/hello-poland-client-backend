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

  public User getUser(String idToken) throws IllegalStateException, Exception {
    GoogleIdTokenVerifier verifier =
        new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
            .setAudience(
                List.of("876214520825-se50aq1jerrri6m50eieijobbs9ji4ij.apps.googleusercontent.com",
                    "876214520825-8q9p0e30b9t3c3h3i6dl5pp3emjegfr9.apps.googleusercontent.com",
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
