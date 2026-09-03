package pl.hellopoland.service;

import pl.hellopoland.rest.dto.TechnicalLogsORO;
import pl.hellopoland.rest.dto.TechnicalRecoveryAgentIRO;
import pl.hellopoland.rest.dto.TechnicalRecoveryAgentORO;
import pl.hellopoland.rest.dto.TechnicalStatusORO;

import jakarta.ejb.Stateless;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Stateless
public class TechnicalOpsAgentClient extends ServiceSuperclass {

  private static final String BASE_URL_PROPERTY = "ops.agent.base.url";
  private static final String SECRET_PROPERTY = "ops.agent.shared.secret";
  private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(5);
  private static final Duration READ_TIMEOUT = Duration.ofSeconds(20);
  private static final Duration RECOVERY_TIMEOUT = Duration.ofMinutes(3);

  private final HttpClient httpClient = HttpClient.newBuilder()
      .connectTimeout(CONNECT_TIMEOUT)
      .build();

  public boolean isConfigured() {
    return value(BASE_URL_PROPERTY) != null && value(SECRET_PROPERTY) != null;
  }

  public TechnicalStatusORO getStatus() {
    return request("GET", "/v1/status", "", TechnicalStatusORO.class, READ_TIMEOUT);
  }

  public TechnicalLogsORO getLogs(String source) {
    return request("GET", "/v1/logs/" + source, "", TechnicalLogsORO.class, READ_TIMEOUT);
  }

  public TechnicalRecoveryAgentORO recoverWordpress(String requestId, String actor, String reason) {
    try (Jsonb jsonb = JsonbBuilder.create()) {
      String body = jsonb.toJson(new TechnicalRecoveryAgentIRO(requestId, actor, reason));
      return request("POST", "/v1/recovery/wordpress", body,
          TechnicalRecoveryAgentORO.class, RECOVERY_TIMEOUT);
    } catch (Exception e) {
      throw new IllegalStateException("Nie można przygotować żądania operacyjnego.", e);
    }
  }

  private <T> T request(String method, String path, String body, Class<T> responseType,
      Duration timeout) {
    String baseUrl = value(BASE_URL_PROPERTY);
    String secret = value(SECRET_PROPERTY);
    if (baseUrl == null || secret == null) {
      throw new IllegalStateException("Agent operacyjny nie jest skonfigurowany.");
    }

    URI uri = URI.create(baseUrl.replaceAll("/+$", "") + path);
    String timestamp = Long.toString(Instant.now().getEpochSecond());
    String nonce = UUID.randomUUID().toString();
    String bodyHash = sha256(body);
    String canonical = method + "\n" + path + "\n" + bodyHash + "\n" + timestamp + "\n" + nonce;
    String signature = hmacSha256(secret, canonical);

    HttpRequest.Builder builder = HttpRequest.newBuilder(uri)
        .timeout(timeout)
        .header("Accept", "application/json")
        .header("X-HP-Ops-Timestamp", timestamp)
        .header("X-HP-Ops-Nonce", nonce)
        .header("X-HP-Ops-Signature", signature);

    if ("POST".equals(method)) {
      builder.header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8));
    } else {
      builder.GET();
    }

    try {
      HttpResponse<String> response = httpClient.send(builder.build(),
          HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
      if (response.statusCode() < 200 || response.statusCode() >= 300) {
        throw new IllegalStateException("Agent operacyjny zwrócił HTTP " + response.statusCode());
      }
      try (Jsonb jsonb = JsonbBuilder.create()) {
        return jsonb.fromJson(response.body(), responseType);
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("Przerwano połączenie z agentem operacyjnym.", e);
    } catch (Exception e) {
      throw new IllegalStateException("Agent operacyjny jest niedostępny: " + e.getMessage(), e);
    }
  }

  private String value(String key) {
    String value = properties.getProperty(key);
    return value == null || value.isBlank() ? null : value.trim();
  }

  private String sha256(String value) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      return HexFormat.of().formatHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
    } catch (Exception e) {
      throw new IllegalStateException("Nie można obliczyć SHA-256.", e);
    }
  }

  private String hmacSha256(String secret, String value) {
    try {
      Mac mac = Mac.getInstance("HmacSHA256");
      mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
      return HexFormat.of().formatHex(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
    } catch (Exception e) {
      throw new IllegalStateException("Nie można podpisać żądania operacyjnego.", e);
    }
  }
}
