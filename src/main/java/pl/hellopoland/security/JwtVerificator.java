package pl.hellopoland.security;

import jakarta.enterprise.context.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.hellopoland.service.ServiceSuperclass;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import java.io.StringReader;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;


@RequestScoped
public class JwtVerificator extends ServiceSuperclass {
    private static final Logger logger = LoggerFactory.getLogger(JwtVerificator.class);

    private final String tPayIssuer;
    private final String tPayCertificateUrl;
    private final String tPayRootCertificateUrl;

    public JwtVerificator() {
        this.tPayIssuer = ServiceSuperclass.properties.getProperty("tpay.issuer");;
        this.tPayCertificateUrl = ServiceSuperclass.properties.getProperty("tpay.certificate.url");;
        this.tPayRootCertificateUrl = ServiceSuperclass.properties.getProperty("tpay.certificate.root.url");;
    }

    public boolean verify(String jws, String bodyContent) {
        if (jws == null || jws.isBlank()) {
            logger.warn("FALSE - Missing JWS (null/blank)");
            return false;
        }
        if (bodyContent == null) {
            logger.warn("FALSE - Missing bodyContent (null)");
            return false;
        }

        String[] jwtsig = jws.split("\\.");
        if (jwtsig.length < 3) {
            logger.warn("FALSE - Invalid JWS format");
            return false;
        }

        String headers = jwtsig[0];     // base64url-encoded header
        String signature = jwtsig[2];   // base64url-encoded signature

        try {

            String decodedHeaders = new String(
                    Base64.getUrlDecoder().decode(headers),
                    StandardCharsets.UTF_8
            );

            /* stara walidacja – już NIEPOTRZEBNA
            if (!decodedHeaders.contains(tPayIssuer)) {
                logger.warn(decodedHeaders);
                logger.warn("FALSE - Wrong x5u url (not from {})", tPayIssuer);
                return false;
            }
            */

            JsonObject hdr = Json.createReader(new StringReader(decodedHeaders)).readObject();
            String x5u = hdr.getString("x5u", "");

            String host = null;
            try {
                host = URI.create(x5u).getHost();
            } catch (Exception e) {
                logger.warn("Cannot parse x5u URI: {}", x5u, e);
            }

            /*Set<String> allowedHosts = Set.of(
                    tPayIssuer,
                    "secure.tpay.com",
                    "secure.sandbox.tpay.com"
            );*/

            Set<String> allowedHosts = new HashSet<>();
            allowedHosts.add("secure.tpay.com");
            allowedHosts.add("secure.sandbox.tpay.com");
            if (tPayIssuer != null && !tPayIssuer.isBlank()) {
                allowedHosts.add(tPayIssuer.trim());
            }


            if (host == null || !allowedHosts.contains(host)) {
                logger.warn(decodedHeaders);
                logger.warn("FALSE - Wrong x5u host: {} (allowed: {})", host, allowedHosts);
                return false;
            }



            X509Certificate signingCert = getCertificateFromUrl(tPayCertificateUrl);
            X509Certificate trustedRootCert = getCertificateFromUrl(tPayRootCertificateUrl);

            signingCert.verify(trustedRootCert.getPublicKey());

            String urlSafeBase64Body = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bodyContent.getBytes(StandardCharsets.UTF_8));


            String signingInput = headers + "." + urlSafeBase64Body;


            byte[] signatureBytes = Base64.getUrlDecoder().decode(signature);

            PublicKey publicKey = signingCert.getPublicKey();
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKey);
            sig.update(signingInput.getBytes(StandardCharsets.UTF_8));

            return sig.verify(signatureBytes);
        } catch (Exception e) {
            logger.error("Failed to verify JWS", e);
            return false;
        }
    }


    private static X509Certificate getCertificateFromUrl(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        try (InputStream inStream = conn.getInputStream()) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            return (X509Certificate) cf.generateCertificate(inStream);
        }
    }
}
