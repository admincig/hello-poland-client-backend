package pl.hellopoland.service;

import pl.hellopoland.bo.*;
import pl.hellopoland.bo.Order.Status;
import pl.hellopoland.bo.UserRole.Role;
import pl.hellopoland.dto.EmailSendingReportDTO;
import pl.hellopoland.dto.TicketDefinitionDTO;
import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.exception.email.EmailSendingException;
import pl.hellopoland.exception.notfound.ResourceNotFoundException;
import pl.hellopoland.rest.dto.OrderIRO;
import pl.hellopoland.rest.dto.OrderIRO.OrderEntryIRO;
import pl.hellopoland.soap.p24.enums.Country;
import pl.hellopoland.util.HelloTicket;
import pl.hellopoland.util.PaymentUtils;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.MediaType;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@LocalBean
@Stateless
public class OrderService extends ServiceSuperclass {

  @Inject
  SightEventService seService;

  public PassageCart create(OrderIRO iro) {
    throwIfExpiredTickets(iro);
    throwIfTicketPricesDontMatch(iro);
    writeSomeLogs(iro);
    Order o = new Order();
    o.setUser(getLoggedUser());
    iro.details.setUserLogged(o.getUser() != null);

    Country country = Country.PL;
    try {
      country = Country.valueOf(iro.details.getCountry());
    } catch (IllegalArgumentException e) {
      logger.log(Level.WARNING, "Setting country to PL: " + e.getMessage());
    }
    iro.details.setCountry(country.name());
    iro.details.setLanguage(country.getP24Language());

    o.setDetails(iro.details);
    em.persist(o);
    Set<Long> atnaIds =
        iro.entries.stream().filter(oe -> oe.quantity != null && oe.quantity.compareTo(0) > 0)
            .collect(groupingBy(oeIRO -> oeIRO.id)).keySet();
    HelloTicket hpt = new HelloTicket(getPortal("Hello Ticket Cloud").getUrl());
    List<TicketDefinitionDTO> tds = hpt.getTicketDefinitions(atnaIds);
    Map<Long, List<TicketDefinitionDTO>> ticketsGroupedBySight =
        tds.stream().collect(Collectors.groupingBy(td -> td.sightEventId));
    for (Map.Entry<Long, List<TicketDefinitionDTO>> entry : ticketsGroupedBySight.entrySet()) {
      OrderSightEntry ose = new OrderSightEntry();
      ose.setOrder(o);
      SightEvent sightEvent = seService.getByHptId(entry.getKey());
      if (!sightEvent.isAccessible()) {
        throw new ConflictingException("Sight Event is not accessible.");
      }
      ose.setSightEvent(sightEvent);
      em.persist(ose);
      ose.setAgreements(new ArrayList<>(sightEvent.getAgreements()));
      em.flush();
      Map<Long, TicketDefinitionDTO> ticketIdToObject =
          entry.getValue().stream()
              .collect(Collectors.toMap(td -> td.atnaId, td -> td));
      Set<Long> ticketIdsOfSight = ticketIdToObject.keySet();
      Map<Date, List<OrderEntryIRO>> inSightGroupedByDate =
          iro.entries.stream().filter(oeIRO -> ticketIdsOfSight.contains(oeIRO.id))
              .collect(groupingBy(oeIRO -> oeIRO.date));
      for (Map.Entry<Date, List<OrderEntryIRO>> inSightOnDate : inSightGroupedByDate.entrySet()) {
        if (!inSightOnDate.getValue().isEmpty()) {
          OrderDateEntry dateEntry = new OrderDateEntry();
          dateEntry.setDate(inSightOnDate.getKey());
          dateEntry.setSightEntry(ose);
          em.persist(dateEntry);
          for (OrderEntryIRO oeIRO : inSightOnDate.getValue()) {
            TicketDefinitionDTO ticket = ticketIdToObject.get(oeIRO.id);
            OrderEntry oe = new OrderEntry();
            oe.setName(ticket.name);
            oe.setQuantity(oeIRO.quantity);
            oe.setUnitPrice(ticket.originalPrice);
            if (ticket.discount != null) {
              oe.setDiscount(new Discount());
              oe.getDiscount().setPrice(ticket.discount.price);
              oe.getDiscount().setHplPart(ticket.discount.hplPart);
              oe.getDiscount().setPartnerPart(ticket.discount.partnerPart);
            }
            oe.setDateEntry(dateEntry);
            oe.setExternalDefinitionId(ticket.id);
            oe.setPoolId(ticket.poolId);
            if (oeIRO.partnerAffiliateCode != null
                && !oeIRO.partnerAffiliateCode.equals(sightEvent.getPartner().getAffiliateCode())) {
              logger.log(Level.ERROR,
                  "Kod afiliacyjny zamówienia [" + oeIRO.partnerAffiliateCode
                      + "] niezgodny z kodem afiliacyjnym partnera [id="
                      + sightEvent.getPartner().getId() + "]");
              throw new ConflictingException("Niezgodny kod afiliacyjny");
            }
            oe.setPartnerAffiliateCode(oeIRO.partnerAffiliateCode);
            em.persist(oe);
          }
        }
      }
    }
    try {
      placeInExternalAPI(o);
    } catch (Exception e) {
      throw new ConflictingException("Nie udało się złożyć zamówienia w zewnętrznym systemie", e);
    }
    var cart = getP24PassageCart(o);
    logger.log(Level.INFO, "Returned order id=" + o.getId() + "; p24cart id=" + cart.getId());
    logger.log(Level.INFO, "-------End creating order --------");
    return cart;
  }

  private void writeSomeLogs(OrderIRO iro) {
    logger.log(Level.INFO, "-------Start creating order --------");
    logger.log(Level.INFO, "Order details: " + iro.details.getEmail() + " "
        + iro.details.getFirstName() + " " + iro.details.getLastName());
    var orderEntriesLog = new StringBuilder();
    iro.entries.forEach(entry -> orderEntriesLog.append("[").append("date:").append(entry.date)
        .append("; quantity:").append(entry.quantity).append("; partnerAffiliateCode:")
        .append(entry.partnerAffiliateCode).append("; TicketDefinition id:").append(entry.id)
        .append("];\n"));
    logger.log(Level.INFO, "Order entries: " + orderEntriesLog.toString());
  }

  private void throwIfTicketPricesDontMatch(OrderIRO iro) {
    List<OrderEntryIRO> entriesWithPrice = iro.entries.stream()
        .filter(oe -> oe.price != null)
        .collect(Collectors.toList());
    Set<Long> ids = entriesWithPrice.stream()
        .map(oe -> oe.id)
        .collect(Collectors.toSet());

    if (!ids.isEmpty()) {
      HelloTicket hpt = new HelloTicket(getPortal("Hello Ticket Cloud").getUrl());
      List<TicketDefinitionDTO> tds = hpt.getTicketDefinitions(ids);
      for (TicketDefinitionDTO td : tds) {
        for (OrderEntryIRO oe : iro.entries) {
          if (oe.id.equals(td.atnaId)) {
            if (!oe.price.equals(td.price)) {
              throw new ConflictingException(
                  "Cena biletu " + td.atnaId + ": " + td.name + " uległa zmianie.");
            }
            break;
          }
        }
      }
    }
  }

  private void throwIfExpiredTickets(OrderIRO iro) {
    var expiredIds = new HashMap<Long, OrderEntryIRO>();
    iro.entries.forEach(entry -> {
      if (entry.date.before(new Date())) {
        expiredIds.put(entry.id, entry);
      }
    });
    List<TicketDefinitionDTO> expiredTickets = Collections.emptyList();
    if (expiredIds.size() > 0) {
      HelloTicket hpt = new HelloTicket(getPortal("Hello Ticket Cloud").getUrl());
      expiredTickets = hpt.getTicketDefinitions(expiredIds.keySet());
      List<TicketPoolDefinitionDTO> wholeDayPools = hpt.getWholeDay(expiredTickets.stream()
          .map(td -> td.poolId)
          .collect(Collectors.toList()));
      for (var pool : wholeDayPools) {
        for (var iter = expiredTickets.iterator(); iter.hasNext(); ) {
          TicketDefinitionDTO ticket = iter.next();
          if (Objects.equals(pool.id, ticket.poolId)
              && !LocalDate
              .ofInstant(expiredIds.get(ticket.atnaId).date.toInstant(), ZoneId.systemDefault())
              .isBefore(LocalDate.now())) {
            expiredIds.remove(ticket.atnaId);
            iter.remove();
          }
        }
      }
    }
    if (expiredIds.size() > 0) {
      var format = new SimpleDateFormat("YYYY-MM-dd HH:mm");
      var errMsg = new StringBuilder(
          "W swoim koszyku masz bilety na oferty, które już minęły. Przeterminowane bilety:");
      expiredTickets.forEach(t -> errMsg
          .append("\n" + t.name + ", data: " + format.format(expiredIds.get(t.atnaId).date)
              + ", oferta: " + t.sightEventId + ";"));
      System.out.println(errMsg.toString());
      throw new ConflictingException(errMsg.toString());
    }
  }

  // em.refreshes are because of strange NPEs
  private void placeInExternalAPI(Order o) {
    em.refresh(o);
    logger.log(Logger.Level.INFO,
        "Checking if any of order sight entries ought to be placed in external API");
    Map<Portal, List<OrderSightEntry>> groupedByPortal = groupByPortal(o);
    for (var entry : groupedByPortal.entrySet()) {
      Portal portal = entry.getKey();
      logger.log(Logger.Level.INFO, "Placing external order in " + portal.getName());
      switch (portal.getType()) {
        case HELLOTICKET_CLOUD_1:
          placeInHpt(o.getDetails(), entry);
          break;
      }
    }
  }

  private Map<Portal, List<OrderSightEntry>> groupByPortal(Order order) {
    return order.getEntries().stream().filter(ose -> ose.getSightEvent().getPortal() != null)
        .collect(groupingBy(ose -> ose.getSightEvent().getPortal()));
  }

  private PassageCart getP24PassageCart(Order o) {
    var passageCart = new PassageCart(o);
    passageCart.setSandbox(Boolean.parseBoolean(properties.getProperty("przelewy24.isSandbox")));
    List<OrderEntry> orderEntries = gatherOrderEntries(o.getEntries());
    var amount = orderEntries.stream()
        .collect(Collectors.summingInt(oe -> oe.getRealPrice() * oe.getQuantity()));
    passageCart.setAmount(amount);
    passageCart.setCountry(o.getDetails().getCountry());
    passageCart.setLanguage(o.getDetails().getLanguage());
    var currency = "PLN";
    passageCart.setCurrency(currency);
    passageCart.setDescription("Hello Poland, " + o.getHash());
    var merchantId = Integer.valueOf(properties.getProperty("przelewy24.merchantId"));
    passageCart.setMerchantId(merchantId);
    passageCart.setSign(getP24Sign(o.getHash(), merchantId, amount, currency));
    passageCart.setUrlStatus(getAckPaymentURL(o));
    var passageCartEntries = new HashSet<PassageCartEntry>();
    orderEntries.forEach(oe -> {
      var cartEntry = new PassageCartEntry(oe);
      cartEntry.setPassageCart(passageCart);
      cartEntry.setDescription("Hello Poland, " + o.getHash());
      if (cartEntry.getTargetAmount() > 0) {
        passageCartEntries.add(cartEntry);
      }
    });
    passageCart.setCartEntries(passageCartEntries);
    var hpCommissionEntry = getHpCommissionEntry(amount, passageCart.getCartEntries(), o.getHash());
    if (hpCommissionEntry.getTargetAmount() > 0) {
      hpCommissionEntry.setPassageCart(passageCart);
      passageCart.setHpCommissionEntry(hpCommissionEntry);
    }
    em.persist(passageCart);
    return passageCart;
  }

  public void sudoAck(String hash) {
    Order order = findByHash(hash);
    confirm(order);
  }

  private static List<OrderEntry> gatherOrderEntries(Collection<OrderSightEntry> collection) {
    List<OrderEntry> orderEntries = new ArrayList<>();
    for (OrderSightEntry se : collection) {
      for (OrderDateEntry de : se.getEntries()) {
        orderEntries.addAll(de.getEntries());
      }
    }
    return orderEntries;
  }

  private String getAckPaymentURL(Order o) {
    var url = properties.getProperty("base.url");
    if (!url.endsWith("/")) {
      url = url.concat("/");
    }
    return url.concat("market/orders/" + o.getHash() + "/ackPayment");
  }

  private String getP24Sign(String orderHash, Integer merchantId, Integer amount, String currency) {
    var delimiter = "|";
    var signBuilder = new StringBuilder();
    signBuilder.append(orderHash).append(delimiter);
    signBuilder.append(merchantId).append(delimiter);
    signBuilder.append(amount).append(delimiter);
    signBuilder.append(currency).append(delimiter);
    signBuilder.append(properties.getProperty("przelewy24.crc"));
    return PaymentUtils.MD5(signBuilder.toString());
  }

  private PassageCartEntry getHpCommissionEntry(Integer amount,
      Set<PassageCartEntry> passageCartEntries, String orderHash) {
    var hpCommission = new PassageCartEntry();
    hpCommission.setName("Hello-Poland prowizja");
    hpCommission.setDescription("HP prowizja do zamówienia " + orderHash);
    hpCommission.setNumber(0l);
    hpCommission.setQuantity(1);
    Integer targetAmount = amount
        - passageCartEntries.stream().collect(Collectors.summingInt(f -> f.getTargetAmount()));
    hpCommission.setTargetAmount(targetAmount);
    hpCommission.setPrice(targetAmount);
    hpCommission.setTargetPosId(Integer.parseInt(properties.getProperty("przelewy24.posId")));
    hpCommission.setCommission(BigDecimal.ZERO);
    return hpCommission;
  }

  public void confirmInExternalAPI(Order o) {
    logger.log(Logger.Level.INFO,
        "Checking if any of order sight entries ought to be confirmed in external API");
    Map<Portal, List<OrderSightEntry>> groupedByPortal = groupByPortal(o);
    for (var entry : groupedByPortal.entrySet()) {
      Portal portal = entry.getKey();
      logger.log(Logger.Level.INFO, "Confirming external order in " + portal.getName());

      switch (portal.getType()) {
        case HELLOTICKET_CLOUD_1:
          confirmInHpt(entry);
          break;
      }
    }
  }

  private void confirmInHpt(Map.Entry<Portal, List<OrderSightEntry>> entry) {
    Portal portal = entry.getKey();
    List<OrderEntry> orderEntries = gatherOrderEntries(entry.getValue());
    String serialNumber =
        entry.getValue().stream().map(OrderSightEntry::getSerialNumber).findFirst().get();
    HelloTicket hpt = new HelloTicket(portal.getUrl());
    hpt.confirm(serialNumber, orderEntries);
  }

  private void placeInHpt(OrderDetails details, Entry<Portal, List<OrderSightEntry>> entry) {
    Portal portal = entry.getKey();
    List<OrderEntry> orderEntries = gatherOrderEntries(entry.getValue());
    HelloTicket hpt = new HelloTicket(portal.getUrl());
    JsonObject resp = (JsonObject) hpt.book(details, orderEntries);
    if (resp == null) {
      throw new ConflictingException("Placing order in HPT returned respons null.");
    }
    Integer externalOrderId = resp.getInt("id");
    entry.getValue().forEach(ose -> ose.setExternalId(externalOrderId.longValue()));
    em.flush();
  }

  private List<OrderEntry> gatherOrderEntries(List<OrderSightEntry> list) {
    List<OrderEntry> returnList = new ArrayList<>();
    for (OrderSightEntry se : list) {
      em.refresh(se);
      for (OrderDateEntry de : se.getEntries()) {
        em.refresh(de);
        returnList.addAll(de.getEntries());
      }
    }
    return returnList;
  }

  public OrderDateEntry getOrderDateEntryForLoggedUser(long id) {
    String queryString =
        "from OrderDateEntry where deleted=false and id=:id and sightEntry.order.user=:user";
    OrderDateEntry de = em.createQuery(queryString, OrderDateEntry.class).setParameter("id", id)
        .setParameter("user", getLoggedUser()).getSingleResult();

    de.getEntries().forEach(e -> e.getNumbers().size());
    return de;
  }

  public List<OrderDateEntry> getOrderSightDateEntriesInTheFutureForLoggedUser() {
    Date nowPlusOneDay = new Date(new Date().getTime() - TimeUnit.DAYS.toMillis(1));
    List<OrderDateEntry> osdes = em.createQuery(
        "from OrderDateEntry osde join fetch osde.sightEntry ose join fetch ose.sightEvent s join fetch ose.order o where osde.deleted=false and o.user=:user and o.status=:status and osde.date>=:date order by osde.date asc",
        OrderDateEntry.class)
        .setParameter("user", getLoggedUser())
        .setParameter("status", Order.Status.CONFIRMED)
        .setParameter("date", nowPlusOneDay)
        .getResultList();
    osdes.forEach(osde -> osde.getEntries().size());
    return osdes;
  }

  public List<OrderDateEntry> getOrderSightDateEntriesInThePastForLoggedUser() {
    Date nowPlusOneDay = new Date(new Date().getTime() - TimeUnit.DAYS.toMillis(1));
    List<OrderDateEntry> osdes = em.createQuery(
        "from OrderDateEntry osde join fetch osde.sightEntry ose join fetch ose.sightEvent s join fetch ose.order o where osde.deleted=false and o.user=:user and o.status=:status and osde.date<:date order by osde.date asc",
        OrderDateEntry.class)
        .setParameter("user", getLoggedUser())
        .setParameter("status", Order.Status.CONFIRMED)
        .setParameter("date", nowPlusOneDay)
        .getResultList();
    osdes.forEach(osde -> osde.getEntries().size());
    return osdes;
  }

  public void deleteOrderDateEntryForLoggedUser(long id) {
    getOrderDateEntryForLoggedUser(id).setDeleted(true);
  }

  public void ack(String hash, String ack) throws Exception {
    logger.log(Logger.Level.INFO, "Got ack from P24");
    Map<String, String> ackMap = PaymentUtils.queryToMap(ack);

    // TODO fill statement title or something
    // p24_order_id, p24_statement

    logger.log(Logger.Level.INFO, "confirming payment");
    StringBuilder signBuilder = new StringBuilder();
    signBuilder.append(ackMap.get("p24_session_id")).append("|");
    signBuilder.append(ackMap.get("p24_order_id")).append("|");
    signBuilder.append(ackMap.get("p24_amount")).append("|");
    signBuilder.append(ackMap.get("p24_currency")).append("|");
    signBuilder.append(properties.getProperty("przelewy24.crc"));
    String p24_sign = PaymentUtils.MD5(signBuilder.toString());
    String p24_statement = ackMap.get("p24_statement");
    ackMap.remove("p24_method");
    ackMap.remove("p24_statement");
    ackMap.put("p24_sign", p24_sign);

    URL url = new URL(properties.getProperty("przelewy24.confirmation.endpoint"));
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.addRequestProperty("Accept", MediaType.APPLICATION_FORM_URLENCODED);
    conn.addRequestProperty("Content-Type", MediaType.APPLICATION_FORM_URLENCODED);
    conn.setDoOutput(true);
    conn.getOutputStream().write(PaymentUtils.mapToQuery(ackMap).getBytes());
    logger.log(Logger.Level.INFO, "" + conn.getResponseCode());
    String resp = conn.getResponseMessage();
    logger.log(Logger.Level.INFO, resp);
    Order order = findByHash(hash);
    if (resp.equals("OK")) {
      logger.log(Logger.Level.INFO, "transaction confirmed. successful");
      order.setP24OrderId(ackMap.get("p24_order_id"));
      order.setP24Currency(ackMap.get("p24_currency"));
      order.setP24Statement(p24_statement);
      confirm(order);
    } else {
      logger.log(Logger.Level.WARNING, "transaction problem.");
      problem(order);
    }
  }

  private void cancelInExternalAPI(Order order) {
    logger.log(Logger.Level.INFO,
        "Checking if any of order sight entries ought to be cancelled in external API");
    Map<Portal, List<OrderSightEntry>> groupedByPortal = groupByPortal(order);
    for (Map.Entry<Portal, List<OrderSightEntry>> entry : groupedByPortal.entrySet()) {
      Portal portal = entry.getKey();
      logger.log(Logger.Level.INFO, "Cancelling external order in " + portal.getName());

      // they have same id. should have
      /* Long id = */
      entry.getValue().stream().map(OrderSightEntry::getExternalId).findFirst().get();
      // TODO cancel in HPT
    }
  }

  private Order findByHash(String hash) {
    return em.createQuery("from Order where hash=:hash", Order.class).setParameter("hash", hash)
        .getSingleResult();
  }

  public void cancel(Order o) {
    o.setStatus(Order.Status.CANCELLED);
    cancelInExternalAPI(o);
    // TODO send mail or something
  }

  private void problem(Order order) {
    order.setStatus(Status.PROBLEM);
    // TODO handle failure
  }

  private void confirm(Order order) {
    confirmInExternalAPI(order);
    order.setStatus(Status.CONFIRMED);
  }

  public Status getStatus(String hash) {
    return findByHash(hash).getStatus();
  }

  public List<Order> getOrdersInDateRangeAndStatus(Date fromDate, Date toDate, Status status) {
    return em
        .createQuery("from Order where (:fromDate <= date and :toDate > date) and status = :status",
            Order.class)
        .setParameter("toDate", toDate)
        .setParameter("fromDate", fromDate).setParameter("toDate", new Date())
        .setParameter("status", status).getResultList();
  }

  public List<OrderEntry> getOrdersInDateRange(Date fromDate, Date toDate, Partner partner) {
    if (toDate == null) {
      return getOrderForSightEventInDate(fromDate, partner);
    }
    String query = "from OrderEntry oe join fetch oe.dateEntry.sightEntry.order o "
        + "where (:fromDate <= o.date and :toDate > o.date) "
        + (partner != null ? "and oe.dateEntry.sightEntry.sightEvent.partner =:partner " : "")
        + "order by o.date asc, o.id asc";
    return getOrderEntries(query, fromDate, toDate, partner);
  }

  private List<OrderEntry> getOrderForSightEventInDate(Date fromDate, Partner partner) {
    var toDate = new Date(fromDate.getTime());
    String query = "from OrderEntry oe join fetch oe.dateEntry.sightEntry.order o "
        + "where (:fromDate <= oe.dateEntry.date and :toDate > oe.dateEntry.date) "
        + (partner != null ? "and oe.dateEntry.sightEntry.sightEvent.partner =:partner " : "")
        + "order by o.date asc, o.id asc";
    return getOrderEntries(query, fromDate, toDate, partner);
  }

  @SuppressWarnings("deprecation")
  private List<OrderEntry> getOrderEntries(String query, Date fromDate, Date toDate,
      Partner partner) {
    toDate.setDate(toDate.getDate() + 1);
    TypedQuery<OrderEntry> tQuery = em.createQuery(query, OrderEntry.class)
        .setParameter("fromDate", fromDate).setParameter("toDate", toDate);
    if (partner != null) {
      tQuery.setParameter("partner", partner);
    }
    return tQuery.getResultList();
  }

  public EmailSendingReportDTO sendTicketCopy(String hash) {
    var order = findByHash(hash);
    EmailSendingReportDTO report = sendTicketsCopyByExternalAPI(order);
    String clientEmail = order.getDetails().getEmail();
    if (report.validUnsentAddresses != null && report.validUnsentAddresses.length > 0) {
      Arrays.stream(report.validUnsentAddresses).filter(address -> clientEmail.equals(address))
          .findAny().orElseThrow(() -> new EmailSendingException(
          "Wystąpił błąd podczas wysyłania kopii biletów do " + clientEmail));
    }
    if (report.invalidAddresses != null && report.invalidAddresses.length > 0) {
      Arrays.stream(report.invalidAddresses).filter(address -> clientEmail.equals(address))
          .findAny().orElseThrow(() -> new EmailSendingException(
          "Wystąpił błąd podczas wysyłania kopii biletów do " + clientEmail));
    }
    return report;
  }

  private EmailSendingReportDTO sendTicketsCopyByExternalAPI(Order order) {
    Map<Portal, List<OrderSightEntry>> groupedByPortal = groupByPortal(order);
    for (var entry : groupedByPortal.entrySet()) {
      Portal portal = entry.getKey();
      switch (portal.getType()) {
        case HELLOTICKET_CLOUD_1:
          return sendTicketsCopyByHpt(portal, entry.getValue());
      }
    }
    return null;
  }

  private EmailSendingReportDTO sendTicketsCopyByHpt(Portal portal, List<OrderSightEntry> ose) {
    String serialNumber = ose.stream().map(OrderSightEntry::getSerialNumber).findFirst()
        .orElseThrow(() -> new ResourceNotFoundException());
    HelloTicket hpt = new HelloTicket(portal.getUrl());
    User loggedUser = getLoggedUser();
    if (loggedUser.hasRole(Role.ADMIN)) {
      return hpt.sendTicketsCopyByAdmin(serialNumber, loggedUser.getHptToken());
    }
    return hpt.sendTicketsCopyByPartner(serialNumber, loggedUser.getPartner().getHptToken());
  }

  public void anonymizeOrdersForUser(User user) {
    em.createQuery("from Order where user=:user",
        Order.class)
        .setParameter("user", user).getResultStream()
        .forEach(o -> {
          OrderDetails details = o.getDetails();
          if (details != null) {
            details.setFirstName("anon");
            details.setLastName("anon");
            details.setStreet("anon");
            details.setPhone("anon");
          }
        });
  }

}
