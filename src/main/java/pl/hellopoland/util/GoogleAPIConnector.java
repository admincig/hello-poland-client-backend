package pl.hellopoland.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.lang.System.Logger;
import java.util.List;
import pl.hellopoland.bo.User;

public class GoogleAPIConnector {

  private Logger logger = System.getLogger(GoogleAPIConnector.class.getName());

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
                    "876214520825-9vun30k1bo423jcsfu38ddjcs5jmkfq6.apps.googleusercontent.com",
                    "319066854009-4r9h35lq6gipp35e4c650ahqcrui3u03.apps.googleusercontent.com",
                    "319066854009-37dc24k6el5cp5iu9on2ng37j0usi8j7.apps.googleusercontent.com",
                    "319066854009-hjmsd13qdunboqv3l52mcmq18b6pftmf.apps.googleusercontent.com",
                    "319066854009-a8v305sfvacpkdqq7t63v9fvcij3r8ts.apps.googleusercontent.com",
                    "319066854009-vd6lt8aj3373igh6tf9qu78n8krmenme.apps.googleusercontent.com",
                    "319066854009-shv7nogo8kuqa602kp62jhfe74jdkrv2.apps.googleusercontent.com"))
            .setIssuer("https://accounts.google.com").build();
    logger.log(Logger.Level.INFO, "verifying idToken=" + idToken);
    GoogleIdToken token = verifier.verify(idToken);
    GoogleIdToken.Payload payload = token.getPayload();
    if (payload != null) {
      logger.log(Logger.Level.INFO, payload.toPrettyString());
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
