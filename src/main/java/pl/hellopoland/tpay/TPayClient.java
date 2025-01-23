package pl.hellopoland.tpay;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ejb.Stateless;
import org.bouncycastle.util.encoders.Base64;
import pl.hellopoland.tpay.dto.*;
import pl.hellopoland.service.ServiceSuperclass;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Stateless
public class TPayClient extends ServiceSuperclass {

  private HttpClient httpClient = HttpClient.newHttpClient();
  private ObjectMapper om = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  private String clientId = ServiceSuperclass.properties.getProperty("tpay.clientId");
  private String clientSecret = ServiceSuperclass.properties.getProperty("tpay.clientSecret");
  private String apiUrl = ServiceSuperclass.properties.getProperty("tpay.apiUrl");
  private String notificationEmail = ServiceSuperclass.properties.getProperty("tpay.notification.email");
  private String redirectUrl = ServiceSuperclass.properties.getProperty("tpay.redirectUrl");

  public TransactionCreated createTransaction(String description, String hash, String ackUrl, BigDecimal totalPrice, String email, String name) {
    try {
      return createTransactionInternal(description, hash, ackUrl, totalPrice, email, name);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private TransactionCreated createTransactionInternal(String description, String hash, String ackUrl, BigDecimal totalPrice, String email, String name) throws IOException, InterruptedException {
    String accessToken = login();
    String transactionUrl = apiUrl + "/transactions";
    CreateTransaction requestJson = prepareRequestJson(description, hash, ackUrl, totalPrice, email, name);
    String requestJsonString = om.writeValueAsString(requestJson);
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(transactionUrl))
        .POST(HttpRequest.BodyPublishers.ofString(requestJsonString))
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + accessToken)
        .build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    return om.readValue(response.body(), TransactionCreated.class);
  }

  private CreateTransaction prepareRequestJson(String description, String hash, String ackUrl, BigDecimal totalPrice, String email, String name) {
    CreateTransaction createTransaction = new CreateTransaction();
    createTransaction.description = description;
    createTransaction.hiddenDescription = hash;
    createTransaction.amount = totalPrice;
    createTransaction.payer = new Payer();
    createTransaction.payer.email = email;
    createTransaction.payer.name = name;
    createTransaction.callbacks = new Callbacks();
    createTransaction.callbacks.notification = new Notification();
    createTransaction.callbacks.notification.email = notificationEmail;
    createTransaction.callbacks.notification.url = ackUrl;
    createTransaction.callbacks.payerUrls = new PayerUrls();
    createTransaction.callbacks.payerUrls.success = redirectUrl;
    createTransaction.callbacks.payerUrls.error = redirectUrl;
    return createTransaction;
  }

  private String login() throws IOException, InterruptedException {
    String tokenEndpoint = apiUrl + "/oauth/auth";
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(tokenEndpoint))
        .POST(HttpRequest.BodyPublishers.noBody())
        .header("Authorization", "Basic " + Base64.toBase64String((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8)))
        .build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    JsonNode responseJson = om.readTree(response.body());
    return responseJson.get("access_token").textValue();
  }
}
