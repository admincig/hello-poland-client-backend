package pl.hellopoland.service;

import pl.hellopoland.bo.PromotionCampaign;
import pl.hellopoland.bo.PromotionCampaignSightEvent;
import pl.hellopoland.bo.PromotionCode;
import pl.hellopoland.bo.PromotionCodeRedemption;
import pl.hellopoland.bo.Order;
import pl.hellopoland.bo.OrderEntry;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.Discount;
import pl.hellopoland.dto.TicketDefinitionDTO;
import pl.hellopoland.enums.PromotionCodeStatus;
import pl.hellopoland.enums.PromotionCodeRedemptionStatus;
import pl.hellopoland.enums.PromotionCodeType;
import pl.hellopoland.enums.PromotionScopeType;
import pl.hellopoland.enums.PromotionStatus;
import pl.hellopoland.enums.PromotionTicketPoolStatus;
import pl.hellopoland.enums.PromotionType;
import pl.hellopoland.rest.dto.PromotionCodeValidationIRO;
import pl.hellopoland.rest.dto.PromotionCodeValidationORO;
import pl.hellopoland.rest.dto.PromotionCodeReservationReleaseIRO;
import pl.hellopoland.rest.dto.PromotionCodeValidationORO.Effect;
import pl.hellopoland.rest.dto.PromotionCodeValidationORO.Item;
import pl.hellopoland.rest.dto.PromotionCodeValidationORO.PoolRef;
import pl.hellopoland.rest.dto.PromotionCodeValidationORO.Promotion;
import pl.hellopoland.rest.dto.PromotionCodeValidationORO.SightEventRef;
import pl.hellopoland.rest.dto.PromotionCodeValidationORO.Target;
import pl.hellopoland.rest.dto.PromotionCodeValidationORO.TicketTypeRef;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.util.HelloTicket;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NoResultException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@LocalBean
@Stateless
public class PromotionCodeService extends ServiceSuperclass {

  private static final String EFFECT_ADD_TICKET = "ADD_TICKET";
  private static final String EFFECT_PERCENT_DISCOUNT = "APPLY_PERCENT_DISCOUNT";
  private static final String EFFECT_AMOUNT_DISCOUNT = "APPLY_AMOUNT_DISCOUNT";
  private static final String TARGET_CART = "CART";
  private static final String TARGET_ITEMS = "ITEMS";
  private static final int RESERVATION_MINUTES = 30;

  public PromotionCodeValidationORO validate(PromotionCodeValidationIRO iro) {
    if (iro == null || iro.code == null || iro.code.isBlank()) {
      return invalid("CODE_REQUIRED", "Podaj kod promocyjny.");
    }

    PromotionCode code = findByCode(iro.code);
    if (code == null) {
      return invalid("CODE_NOT_FOUND", "Kod jest niepoprawny.");
    }

    if (code.getStatus() != PromotionCodeStatus.ACTIVE) {
      return invalidForCodeStatus(code.getStatus());
    }

    PromotionCampaign campaign = code.getPromotionCampaign();
    PromotionCodeValidationORO campaignValidation = validateCampaign(campaign);
    if (campaignValidation.isInvalid()) {
      return campaignValidation;
    }

    PromotionCodeValidationORO limitsValidation = validateLimits(campaign, code);
    if (limitsValidation.isInvalid()) {
      return limitsValidation;
    }

    CartContext cartContext = resolveCart(iro);
    if (cartContext.isEmpty() && campaign.getScopeType() != PromotionScopeType.GLOBAL) {
      return invalid("CART_REQUIRED", "Dodaj bilety do koszyka, aby użyć kodu.");
    }

    PromotionCodeValidationORO scopeValidation = validateScope(campaign, cartContext.hpSightEventIds());
    if (scopeValidation.isInvalid()) {
      return scopeValidation;
    }

    PromotionCodeValidationORO response = PromotionCodeValidationORO.valid();
    response.promotion = promotionDetails(campaign);
    PromotionCodeValidationORO result = null;

    if (campaign.getPromotionType() == PromotionType.TICKET) {
      result = fillTicketEffects(response, campaign, cartContext);
    }
    if (campaign.getPromotionType() == PromotionType.PERCENT) {
      result = fillDiscountEffect(response, campaign, cartContext, EFFECT_PERCENT_DISCOUNT);
    }
    if (campaign.getPromotionType() == PromotionType.AMOUNT) {
      result = fillDiscountEffect(response, campaign, cartContext, EFFECT_AMOUNT_DISCOUNT);
    }

    if (result != null) {
      if (result.isInvalid()) {
        return result;
      }
      reserve(code, campaign, result);
      return result;
    }

    return invalid("INVALID_PROMOTION_CONFIGURATION", "Nieobsługiwany typ promocji.");
  }

  private PromotionCodeValidationORO fillTicketEffects(PromotionCodeValidationORO response,
      PromotionCampaign campaign, CartContext cartContext) {
    List<PromotionCampaignSightEvent> campaignSightEvents =
        findActiveCampaignSightEvents(campaign, cartContext.hpSightEventIds());
    if (campaignSightEvents.isEmpty()) {
      return invalid("PROMOTION_NOT_AVAILABLE_FOR_OFFER",
          "Kod jest poprawny, ale wybrana oferta nie jest objęta tą promocją.");
    }

    int requiredQuantity = valueOrDefault(campaign.getRequiredTicketQuantity(), 1);
    int grantedQuantity = valueOrDefault(campaign.getGrantedTicketQuantity(), 1);
    Set<Long> promotionalAtnaIds = campaignSightEvents.stream()
        .map(PromotionCampaignSightEvent::getHptAtnaId)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
    Map<Long, TicketDefinitionDTO> promotionalTickets = fetchTicketDefinitions(promotionalAtnaIds);

    for (PromotionCampaignSightEvent campaignSightEvent : campaignSightEvents) {
      PromotionCodeValidationORO ticketValidation = validateTicketPromotion(campaignSightEvent);
      if (ticketValidation.isInvalid()) {
        return ticketValidation;
      }

      Long hpSightEventId = campaignSightEvent.getSightEvent().getId();
      int cartQuantity = cartContext.quantityForHpSightEventIdWithinTicketValidity(
          hpSightEventId, campaign, this);
      if (cartQuantity < requiredQuantity) {
        continue;
      }

      TicketDefinitionDTO promotionalTicket = promotionalTickets.get(campaignSightEvent.getHptAtnaId());
      if (promotionalTicket == null) {
        return invalid("PROMOTION_TICKET_NOT_FOUND",
            "Nie udało się znaleźć biletu promocyjnego.");
      }

      Effect effect = new Effect();
      effect.type = EFFECT_ADD_TICKET;
      effect.sourceCartItemId = cartContext.firstCartItemIdForHpSightEventIdWithinTicketValidity(
          hpSightEventId, campaign, this);
      effect.sightEvent = sightEventRef(hpSightEventId);
      effect.item = ticketItem(promotionalTicket, grantedQuantity);
      response.effects.add(effect);
    }

    if (response.effects.isEmpty()) {
      Set<Long> matchingSightEventIds = campaignSightEvents.stream()
          .map(item -> item.getSightEvent().getId())
          .collect(Collectors.toSet());
      if (cartContext.hasOnlyItemsAfterTicketValidity(
          matchingSightEventIds, campaign, this)) {
        return invalid("PROMOTION_TICKET_DATE_EXCEEDED",
            ticketValidityExceededMessage(campaign));
      }
      return invalid("PROMOTION_CONDITIONS_NOT_MET",
          "Kod jest poprawny, ale warunki promocji nie są spełnione. "
              + "Dodaj wymaganą liczbę biletów w cenie regularnej dla tej samej oferty.");
    }

    return response;
  }

  private PromotionCodeValidationORO fillDiscountEffect(PromotionCodeValidationORO response,
      PromotionCampaign campaign, CartContext cartContext, String effectType) {
    Effect effect = new Effect();
    effect.type = effectType;
    effect.discountPercent = campaign.getDiscountPercent();
    effect.discountAmountGross = campaign.getDiscountAmountGross();

    if (campaign.getScopeType() == PromotionScopeType.GLOBAL) {
      Target target = new Target();
      target.type = TARGET_CART;
      effect.target = target;
      response.effects.add(effect);
      return response;
    }

    List<PromotionCampaignSightEvent> campaignSightEvents =
        findActiveCampaignSightEvents(campaign, cartContext.hpSightEventIds());
    Set<Long> matchingSightEventIds = campaignSightEvents.stream()
        .map(pcse -> pcse.getSightEvent().getId())
        .collect(Collectors.toSet());
    List<Long> matchingCartItemIds = cartContext.cartItemIdsForHpSightEventIds(matchingSightEventIds);
    if (matchingCartItemIds.isEmpty()) {
      return invalid("PROMOTION_NOT_AVAILABLE_FOR_OFFER",
          "Kod jest poprawny, ale wybrana oferta nie jest objęta tą promocją.");
    }

    Target target = new Target();
    target.type = TARGET_ITEMS;
    target.cartItemIds = matchingCartItemIds;
    target.sightEventIds = new ArrayList<>(matchingSightEventIds);
    effect.target = target;
    response.effects.add(effect);
    return response;
  }

  private PromotionCode findByCode(String value) {
    try {
      return em.createQuery(
          "select pc from PromotionCode pc "
              + "join fetch pc.promotionCampaign "
              + "where pc.code = :code",
          PromotionCode.class)
          .setParameter("code", value)
          .setLockMode(LockModeType.PESSIMISTIC_WRITE)
          .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  private PromotionCodeValidationORO validateCampaign(PromotionCampaign campaign) {
    if (campaign.getStatus() != PromotionStatus.ACTIVE) {
      return invalid("PROMOTION_NOT_ACTIVE",
          "Kod jest poprawny, ale promocja nie jest obecnie aktywna.");
    }

    Date now = new Date();
    if (campaign.getValidFrom().after(now)) {
      return invalid("PROMOTION_NOT_STARTED",
          "Kod jest poprawny, ale promocja rozpocznie się "
              + formatPromotionDateTime(campaign.getValidFrom()) + ".");
    }
    if (campaign.getValidTo().before(now)) {
      return invalid("PROMOTION_EXPIRED",
          "Kod jest poprawny, ale promocja zakończyła się "
              + formatPromotionDateTime(campaign.getValidTo()) + ".");
    }

    return PromotionCodeValidationORO.valid();
  }

  private String formatPromotionDateTime(Date date) {
    return new SimpleDateFormat("dd.MM.yyyy 'o' HH:mm").format(date);
  }

  private PromotionCodeValidationORO validateLimits(PromotionCampaign campaign, PromotionCode code) {
    if (isLimitReached(campaign.getGlobalLimit(), campaign.getReservedRedemptionsCount(),
        campaign.getUsedRedemptionsCount())) {
      return invalid("PROMOTION_LIMIT_REACHED", "Skończyła się pula promocji.");
    }

    Integer codeLimit = code.getMaxRedemptions() != null
        ? code.getMaxRedemptions()
        : campaign.getCodeLimit();
    if (isLimitReached(codeLimit, code.getReservedRedemptionsCount(),
        code.getUsedRedemptionsCount())) {
      return invalid("CODE_LIMIT_REACHED", "Kod został już wykorzystany.");
    }

    return PromotionCodeValidationORO.valid();
  }

  private boolean isLimitReached(Integer limit, Integer reservedCount, Integer usedCount) {
    if (limit == null) {
      return false;
    }
    int reserved = reservedCount != null ? reservedCount : 0;
    int used = usedCount != null ? usedCount : 0;
    return reserved + used >= limit;
  }

  private PromotionCodeValidationORO validateScope(PromotionCampaign campaign,
      Set<Long> sightEventIds) {
    if (campaign.getScopeType() == PromotionScopeType.GLOBAL) {
      if (campaign.getPromotionType() == PromotionType.TICKET) {
        return invalid("INVALID_PROMOTION_CONFIGURATION",
            "Promocja biletowa nie może być globalna.");
      }
      return PromotionCodeValidationORO.valid();
    }

    if (sightEventIds.isEmpty()) {
      return invalid("CART_REQUIRED", "Dodaj bilety do koszyka, aby użyć kodu.");
    }

    Long matchingSightEventsCount = em.createQuery(
        "select count(pcse) from PromotionCampaignSightEvent pcse "
            + "where pcse.promotionCampaign = :campaign "
            + "and pcse.active is true "
            + "and pcse.sightEvent.id in :sightEventIds",
        Long.class)
        .setParameter("campaign", campaign)
        .setParameter("sightEventIds", sightEventIds)
        .getSingleResult();

    if (matchingSightEventsCount == 0) {
      return invalid("PROMOTION_NOT_AVAILABLE_FOR_OFFER",
          "Kod jest poprawny, ale wybrana oferta nie jest objęta tą promocją.");
    }

    return PromotionCodeValidationORO.valid();
  }

  private PromotionCodeValidationORO validateTicketPromotion(
      PromotionCampaignSightEvent campaignSightEvent) {
    if (campaignSightEvent.getTicketPoolStatus() != PromotionTicketPoolStatus.CREATED) {
      return invalid("PROMOTION_POOL_NOT_READY",
          "Kod jest poprawny, ale bilety promocyjne dla wybranej oferty nie są obecnie dostępne.");
    }
    if (campaignSightEvent.getHptAtnaId() == null
        || campaignSightEvent.getHptTicketDefinitionId() == null
        || campaignSightEvent.getHptTicketPoolDefinitionId() == null) {
      return invalid("PROMOTION_POOL_NOT_READY",
          "Kod jest poprawny, ale bilety promocyjne dla wybranej oferty nie są obecnie dostępne.");
    }
    return PromotionCodeValidationORO.valid();
  }

  private List<PromotionCampaignSightEvent> findActiveCampaignSightEvents(PromotionCampaign campaign,
      Set<Long> sightEventIds) {
    if (sightEventIds.isEmpty()) {
      return List.of();
    }
    return em.createQuery(
        "select pcse from PromotionCampaignSightEvent pcse "
            + "join fetch pcse.sightEvent "
            + "where pcse.promotionCampaign = :campaign "
            + "and pcse.sightEvent.id in :sightEventIds "
            + "and pcse.active is true",
        PromotionCampaignSightEvent.class)
        .setParameter("campaign", campaign)
        .setParameter("sightEventIds", sightEventIds)
        .getResultList();
  }

  private CartContext resolveCart(PromotionCodeValidationIRO iro) {
    List<PromotionCodeValidationIRO.CartItem> requestedItems = iro.cartItems == null
        ? List.of()
        : iro.cartItems.stream()
            .filter(item -> item != null && item.id != null)
            .filter(item -> valueOrDefault(item.quantity, 0) > 0)
            .collect(Collectors.toList());
    if (requestedItems.isEmpty()) {
      return new CartContext(List.of());
    }

    Set<Long> atnaIds = requestedItems.stream()
        .map(item -> item.id)
        .collect(Collectors.toSet());
    Map<Long, TicketDefinitionDTO> ticketDefinitions = fetchTicketDefinitions(atnaIds);
    Set<Long> hptSightEventIds = ticketDefinitions.values().stream()
        .map(td -> td.sightEventId)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
    Map<Long, SightEvent> hpSightEventsByHptId = findSightEventsByHptIds(hptSightEventIds);

    List<CartItemContext> items = new ArrayList<>();
    for (PromotionCodeValidationIRO.CartItem requestedItem : requestedItems) {
      TicketDefinitionDTO ticketDefinition = ticketDefinitions.get(requestedItem.id);
      if (ticketDefinition == null || ticketDefinition.sightEventId == null) {
        continue;
      }
      SightEvent sightEvent = hpSightEventsByHptId.get(ticketDefinition.sightEventId);
      if (sightEvent == null) {
        continue;
      }
      items.add(new CartItemContext(requestedItem.id, requestedItem.quantity, ticketDefinition,
          sightEvent.getId(), requestedItem.date));
    }
    return new CartContext(items);
  }

  boolean isVisitDateWithinTicketValidity(PromotionCampaign campaign, Date visitDate) {
    if (campaign == null || campaign.getTicketValidTo() == null || visitDate == null) {
      return false;
    }
    ZoneId zone = ZoneId.systemDefault();
    LocalDate visitDay = visitDate.toInstant().atZone(zone).toLocalDate();
    LocalDate lastValidDay = campaign.getTicketValidTo().toInstant().atZone(zone).toLocalDate();
    return !visitDay.isAfter(lastValidDay);
  }

  private String ticketValidityExceededMessage(PromotionCampaign campaign) {
    if (campaign == null || campaign.getTicketValidTo() == null) {
      return "Kod jest poprawny, ale nie skonfigurowano granicznej daty ważności biletu "
          + "promocyjnego. Skontaktuj się z obsługą Hello! Poland.";
    }
    return "Kod jest poprawny, ale wybrany termin przekracza ważność biletu promocyjnego. "
        + "W tej promocji można wybrać termin najpóźniej "
        + new SimpleDateFormat("dd.MM.yyyy").format(campaign.getTicketValidTo()) + ".";
  }

  private Map<Long, TicketDefinitionDTO> fetchTicketDefinitions(Collection<Long> atnaIds) {
    if (atnaIds == null || atnaIds.isEmpty()) {
      return Map.of();
    }
    HelloTicket hpt = new HelloTicket(getPortal("Hello Ticket Cloud").getUrl());
    return hpt.getTicketDefinitionsMarket(new HashSet<>(atnaIds)).stream()
        .filter(td -> td.atnaId != null)
        .collect(Collectors.toMap(td -> td.atnaId, Function.identity(), (left, right) -> left));
  }

  private Map<Long, SightEvent> findSightEventsByHptIds(Set<Long> hptSightEventIds) {
    if (hptSightEventIds.isEmpty()) {
      return Map.of();
    }
    return em.createQuery("from SightEvent where hptId in :hptIds", SightEvent.class)
        .setParameter("hptIds", hptSightEventIds)
        .getResultStream()
        .collect(Collectors.toMap(SightEvent::getHptId, Function.identity(), (left, right) -> left));
  }

  private Promotion promotionDetails(PromotionCampaign campaign) {
    Promotion promotion = new Promotion();
    promotion.id = campaign.getId();
    promotion.name = campaign.getName();
    promotion.type = campaign.getPromotionType();
    promotion.scope = campaign.getScopeType();
    return promotion;
  }

  private SightEventRef sightEventRef(Long id) {
    SightEventRef ref = new SightEventRef();
    ref.id = id;
    return ref;
  }

  private Item ticketItem(TicketDefinitionDTO dto, int quantity) {
    Item item = new Item();
    item.id = dto.atnaId;
    item.name = dto.name;
    item.quantity = quantity;
    item.price = dto.price;
    item.originalPrice = dto.originalPrice;
    item.ticketType = ticketTypeRef(dto);
    item.pool = poolRef(dto);
    return item;
  }

  private TicketTypeRef ticketTypeRef(TicketDefinitionDTO dto) {
    if (dto.ticketType == null) {
      return null;
    }
    TicketTypeRef ref = new TicketTypeRef();
    ref.id = dto.ticketType.id;
    ref.code = dto.ticketType.code;
    ref.label = dto.ticketType.label;
    return ref;
  }

  private PoolRef poolRef(TicketDefinitionDTO dto) {
    PoolRef ref = new PoolRef();
    ref.id = dto.poolId;
    ref.type = "PROMOTIONAL";
    return ref;
  }

  private int valueOrDefault(Integer value, int defaultValue) {
    return value != null ? value : defaultValue;
  }

  private PromotionCodeValidationORO invalidForCodeStatus(PromotionCodeStatus status) {
    if (status == PromotionCodeStatus.USED) {
      return invalid("CODE_ALREADY_USED", "Kod został już wykorzystany.");
    }
    if (status == PromotionCodeStatus.RESERVED) {
      return invalid("CODE_RESERVED", "Kod jest obecnie zarezerwowany.");
    }
    return invalid("CODE_DISABLED", "Kod został wyłączony.");
  }

  private PromotionCodeValidationORO invalid(String errorCode, String message) {
    return PromotionCodeValidationORO.invalid(errorCode, message);
  }

  public void attachReservationToOrder(String reservationToken, Order order) {
    if (reservationToken == null || reservationToken.isBlank()) {
      return;
    }

    Date now = new Date();
    PromotionCodeRedemption redemption = findRedemptionByToken(reservationToken);
    if (redemption == null) {
      throw new ConflictingException("Nie znaleziono rezerwacji kodu promocyjnego.");
    }
    if (redemption.getStatus() != PromotionCodeRedemptionStatus.RESERVED) {
      throw new ConflictingException("Rezerwacja kodu promocyjnego nie jest aktywna.");
    }
    if (redemption.getOrder() != null
        && !Objects.equals(redemption.getOrder().getId(), order.getId())) {
      throw new ConflictingException("Rezerwacja kodu promocyjnego jest przypisana do innego zamówienia.");
    }
    if (redemption.getReservedUntil() != null && redemption.getReservedUntil().before(now)) {
      releaseReservation(redemption, PromotionCodeRedemptionStatus.EXPIRED, now);
      throw new ConflictingException("Rezerwacja kodu promocyjnego wygasła.");
    }

    OrderEntry matchingOrderEntry = findMatchingOrderEntry(redemption, order);
    validateTicketPromotionVisitDate(redemption, matchingOrderEntry);
    redemption.setOrder(order);
    redemption.setOrderEntry(matchingOrderEntry);
    applyDiscountPromotion(redemption, order);
    redemption.setUpdatedAt(now);
  }

  public void markOrderPromotionsAsUsed(Order order) {
    Date now = new Date();
    findReservedRedemptionsForOrder(order).forEach(redemption -> markReservationAsUsed(redemption, now));
  }

  public void cancelOrderPromotions(Order order) {
    Date now = new Date();
    findReservedRedemptionsForOrder(order).forEach(redemption ->
        releaseReservation(redemption, PromotionCodeRedemptionStatus.CANCELLED, now));
  }

  public void releaseOrderPromotions(Order order) {
    Date now = new Date();
    findReservedRedemptionsForOrder(order).forEach(redemption ->
        releaseReservation(redemption, PromotionCodeRedemptionStatus.RELEASED, now));
  }

  public void releaseReservation(PromotionCodeReservationReleaseIRO iro) {
    if (iro == null || iro.reservationToken == null || iro.reservationToken.isBlank()) {
      return;
    }

    PromotionCodeRedemption redemption = findRedemptionByToken(iro.reservationToken);
    if (redemption == null || redemption.getOrder() != null) {
      return;
    }

    releaseReservation(redemption, PromotionCodeRedemptionStatus.RELEASED, new Date());
  }

  public void releaseExpiredReservations() {
    Date now = new Date();
    List<PromotionCodeRedemption> expiredReservations = em.createQuery(
        "select redemption from PromotionCodeRedemption redemption "
            + "join fetch redemption.promotionCampaign "
            + "join fetch redemption.promotionCode "
            + "where redemption.status = :status "
            + "and redemption.order is null "
            + "and redemption.reservedUntil is not null "
            + "and redemption.reservedUntil < :now",
        PromotionCodeRedemption.class)
        .setParameter("status", PromotionCodeRedemptionStatus.RESERVED)
        .setParameter("now", now)
        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
        .getResultList();

    expiredReservations.forEach(redemption ->
        releaseReservation(redemption, PromotionCodeRedemptionStatus.EXPIRED, now));
  }

  private PromotionCodeRedemption findRedemptionByToken(String reservationToken) {
    List<PromotionCodeRedemption> result = em.createQuery(
        "select redemption from PromotionCodeRedemption redemption "
            + "join fetch redemption.promotionCampaign "
            + "join fetch redemption.promotionCode "
            + "left join fetch redemption.order "
            + "where redemption.reservationToken = :reservationToken",
        PromotionCodeRedemption.class)
        .setParameter("reservationToken", reservationToken)
        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
        .setMaxResults(1)
        .getResultList();
    return result.isEmpty() ? null : result.get(0);
  }

  private List<PromotionCodeRedemption> findReservedRedemptionsForOrder(Order order) {
    return em.createQuery(
        "select redemption from PromotionCodeRedemption redemption "
            + "join fetch redemption.promotionCampaign "
            + "join fetch redemption.promotionCode "
            + "where redemption.order = :order "
            + "and redemption.status = :status",
        PromotionCodeRedemption.class)
        .setParameter("order", order)
        .setParameter("status", PromotionCodeRedemptionStatus.RESERVED)
        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
        .getResultList();
  }

  private OrderEntry findMatchingOrderEntry(PromotionCodeRedemption redemption, Order order) {
    if (redemption.getHptTicketDefinitionId() == null
        || redemption.getHptTicketPoolDefinitionId() == null) {
      return null;
    }

    List<OrderEntry> entries = em.createQuery(
        "select orderEntry from OrderEntry orderEntry "
            + "join orderEntry.dateEntry dateEntry "
            + "join dateEntry.sightEntry sightEntry "
            + "where sightEntry.order = :order",
        OrderEntry.class)
        .setParameter("order", order)
        .getResultList();

    return entries.stream()
        .filter(entry -> Objects.equals(entry.getExternalDefinitionId(),
            redemption.getHptTicketDefinitionId()))
        .filter(entry -> Objects.equals(entry.getPoolId(),
            redemption.getHptTicketPoolDefinitionId()))
        .findFirst()
        .orElseThrow(() -> new ConflictingException(
            "W zamówieniu brakuje biletu promocyjnego wymaganego przez kod."));
  }

  private void validateTicketPromotionVisitDate(PromotionCodeRedemption redemption,
      OrderEntry matchingOrderEntry) {
    if (redemption.getPromotionTypeSnapshot() != PromotionType.TICKET) {
      return;
    }
    Date visitDate = matchingOrderEntry != null && matchingOrderEntry.getDateEntry() != null
        ? matchingOrderEntry.getDateEntry().getDate()
        : null;
    if (!isVisitDateWithinTicketValidity(redemption.getPromotionCampaign(), visitDate)) {
      throw new ConflictingException(
          ticketValidityExceededMessage(redemption.getPromotionCampaign()));
    }
  }

  private void applyDiscountPromotion(PromotionCodeRedemption redemption, Order order) {
    PromotionType promotionType = redemption.getPromotionTypeSnapshot();
    if (promotionType != PromotionType.PERCENT && promotionType != PromotionType.AMOUNT) {
      return;
    }

    List<OrderEntry> entries = findDiscountableOrderEntries(redemption, order);
    if (entries.isEmpty()) {
      throw new ConflictingException("Kod nie działa dla biletów znajdujących się w zamówieniu.");
    }

    if (promotionType == PromotionType.PERCENT) {
      applyPercentDiscount(redemption, entries);
    }
    if (promotionType == PromotionType.AMOUNT) {
      applyAmountDiscount(redemption, entries);
    }
  }

  private List<OrderEntry> findDiscountableOrderEntries(PromotionCodeRedemption redemption,
      Order order) {
    StringBuilder query = new StringBuilder()
        .append("select orderEntry from OrderEntry orderEntry ")
        .append("join orderEntry.dateEntry dateEntry ")
        .append("join dateEntry.sightEntry sightEntry ")
        .append("where sightEntry.order = :order ");

    if (redemption.getPromotionCampaign().getScopeType() != PromotionScopeType.GLOBAL) {
      query
          .append("and exists (")
          .append("select pcse from PromotionCampaignSightEvent pcse ")
          .append("where pcse.promotionCampaign = :campaign ")
          .append("and pcse.sightEvent = sightEntry.sightEvent ")
          .append("and pcse.active is true")
          .append(") ");
    }

    var typedQuery = em.createQuery(query.toString(), OrderEntry.class)
        .setParameter("order", order);

    if (redemption.getPromotionCampaign().getScopeType() != PromotionScopeType.GLOBAL) {
      typedQuery.setParameter("campaign", redemption.getPromotionCampaign());
    }

    return typedQuery.getResultList().stream()
        .filter(entry -> entry.getQuantity() != null && entry.getQuantity() > 0)
        .collect(Collectors.toList());
  }

  private void applyPercentDiscount(PromotionCodeRedemption redemption, List<OrderEntry> entries) {
    BigDecimal percent = redemption.getDiscountPercent();
    if (percent == null || percent.compareTo(BigDecimal.ZERO) <= 0) {
      throw new ConflictingException("Nieprawidłowa konfiguracja rabatu procentowego.");
    }

    for (OrderEntry entry : entries) {
      int currentPrice = entry.getRealPrice();
      int discountAmount = BigDecimal.valueOf(currentPrice)
          .multiply(percent)
          .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP)
          .intValue();

      setPromotionalPrice(entry, Math.max(0, currentPrice - discountAmount));
    }
  }

  private void applyAmountDiscount(PromotionCodeRedemption redemption, List<OrderEntry> entries) {
    Integer amountGross = redemption.getDiscountAmountGross();
    if (amountGross == null || amountGross <= 0) {
      throw new ConflictingException("Nieprawidłowa konfiguracja rabatu kwotowego.");
    }

    int remainingDiscount = Math.min(amountGross, entries.stream()
        .mapToInt(OrderEntry::getSum)
        .sum());

    for (OrderEntry entry : new ArrayList<>(entries)) {
      if (remainingDiscount <= 0) {
        break;
      }

      int currentPrice = entry.getRealPrice();
      int quantity = entry.getQuantity();
      int entryTotal = currentPrice * quantity;

      if (remainingDiscount >= entryTotal) {
        setPromotionalPrice(entry, 0);
        remainingDiscount -= entryTotal;
        continue;
      }

      int freeUnits = currentPrice > 0 ? remainingDiscount / currentPrice : 0;
      int unitRemainder = currentPrice > 0 ? remainingDiscount % currentPrice : 0;

      if (freeUnits > 0) {
        OrderEntry freeEntry = splitOrderEntry(entry, freeUnits);
        setPromotionalPrice(freeEntry, 0);
        remainingDiscount -= currentPrice * freeUnits;
      }

      if (unitRemainder > 0 && entry.getQuantity() > 0) {
        OrderEntry partiallyDiscountedEntry = splitOrderEntry(entry, 1);
        setPromotionalPrice(partiallyDiscountedEntry, currentPrice - unitRemainder);
        remainingDiscount = 0;
      }
    }
  }

  private void setPromotionalPrice(OrderEntry entry, int price) {
    Discount discount = entry.getDiscount();
    if (discount == null) {
      discount = new Discount();
      entry.setDiscount(discount);
    }
    discount.setPrice(price);
  }

  private OrderEntry splitOrderEntry(OrderEntry source, int quantity) {
    if (quantity <= 0 || source.getQuantity() == null || quantity > source.getQuantity()) {
      throw new ConflictingException("Nie udało się rozdzielić rabatu promocyjnego na bilety.");
    }

    if (quantity == source.getQuantity()) {
      return source;
    }

    source.setQuantity(source.getQuantity() - quantity);

    OrderEntry copy = new OrderEntry();
    copy.setDateEntry(source.getDateEntry());
    copy.setQuantity(quantity);
    copy.setUnitPrice(source.getUnitPrice());
    copy.setName(source.getName());
    copy.setExternalDefinitionId(source.getExternalDefinitionId());
    copy.setPoolId(source.getPoolId());
    copy.setPartnerAffiliateCode(source.getPartnerAffiliateCode());
    copy.setDiscount(copyDiscount(source.getDiscount()));
    em.persist(copy);

    return copy;
  }

  private Discount copyDiscount(Discount source) {
    if (source == null) {
      return null;
    }

    Discount copy = new Discount();
    copy.setPrice(source.getPrice());
    copy.setHplPart(source.getHplPart());
    copy.setPartnerPart(source.getPartnerPart());
    return copy;
  }

  private void reserve(PromotionCode code, PromotionCampaign campaign,
      PromotionCodeValidationORO response) {
    Date now = new Date();
    Date reservedUntil = reservationExpiration(now);
    PromotionCampaignSightEvent campaignSightEvent =
        findRedemptionCampaignSightEvent(campaign, response);

    PromotionCodeRedemption redemption = new PromotionCodeRedemption();
    redemption.setPromotionCampaign(campaign);
    redemption.setPromotionCode(code);
    redemption.setPromotionCampaignSightEvent(campaignSightEvent);
    redemption.setSightEvent(campaignSightEvent != null ? campaignSightEvent.getSightEvent() : null);
    redemption.setUser(getLoggedUser());
    redemption.setStatus(PromotionCodeRedemptionStatus.RESERVED);
    redemption.setReservationToken(UUID.randomUUID().toString());
    redemption.setReservedAt(now);
    redemption.setReservedUntil(reservedUntil);
    redemption.setCodeSnapshot(code.getCode());
    redemption.setPromotionNameSnapshot(campaign.getName());
    redemption.setPromotionTypeSnapshot(campaign.getPromotionType());
    redemption.setDiscountPercent(campaign.getDiscountPercent());
    redemption.setDiscountAmountGross(campaign.getDiscountAmountGross());
    if (campaignSightEvent != null) {
      redemption.setHptTicketDefinitionId(campaignSightEvent.getHptTicketDefinitionId());
      redemption.setHptTicketPoolDefinitionId(campaignSightEvent.getHptTicketPoolDefinitionId());
    }
    em.persist(redemption);

    campaign.setReservedRedemptionsCount(valueOrDefault(campaign.getReservedRedemptionsCount(), 0) + 1);
    code.setReservedRedemptionsCount(valueOrDefault(code.getReservedRedemptionsCount(), 0) + 1);
    code.setReservedUntil(reservedUntil);
    code.setUpdatedAt(now);
    if (code.getCodeType() == PromotionCodeType.ONE_TIME) {
      code.setStatus(PromotionCodeStatus.RESERVED);
    }

    response.reservationToken = redemption.getReservationToken();
  }

  private void markReservationAsUsed(PromotionCodeRedemption redemption, Date usedAt) {
    if (redemption.getStatus() != PromotionCodeRedemptionStatus.RESERVED) {
      return;
    }

    PromotionCampaign campaign = redemption.getPromotionCampaign();
    PromotionCode code = redemption.getPromotionCode();

    redemption.setStatus(PromotionCodeRedemptionStatus.USED);
    redemption.setUsedAt(usedAt);
    redemption.setUpdatedAt(usedAt);

    campaign.setReservedRedemptionsCount(
        Math.max(0, valueOrDefault(campaign.getReservedRedemptionsCount(), 0) - 1));
    campaign.setUsedRedemptionsCount(
        valueOrDefault(campaign.getUsedRedemptionsCount(), 0) + 1);
    code.setReservedRedemptionsCount(
        Math.max(0, valueOrDefault(code.getReservedRedemptionsCount(), 0) - 1));
    code.setUsedRedemptionsCount(
        valueOrDefault(code.getUsedRedemptionsCount(), 0) + 1);
    code.setReservedUntil(null);
    code.setUpdatedAt(usedAt);
    if (code.getCodeType() == PromotionCodeType.ONE_TIME) {
      code.setStatus(PromotionCodeStatus.USED);
    }
  }

  private void releaseReservation(PromotionCodeRedemption redemption,
      PromotionCodeRedemptionStatus status, Date releasedAt) {
    if (redemption.getStatus() != PromotionCodeRedemptionStatus.RESERVED) {
      return;
    }

    PromotionCampaign campaign = redemption.getPromotionCampaign();
    PromotionCode code = redemption.getPromotionCode();

    redemption.setStatus(status);
    redemption.setReleasedAt(releasedAt);
    redemption.setUpdatedAt(releasedAt);

    campaign.setReservedRedemptionsCount(
        Math.max(0, valueOrDefault(campaign.getReservedRedemptionsCount(), 0) - 1));
    code.setReservedRedemptionsCount(
        Math.max(0, valueOrDefault(code.getReservedRedemptionsCount(), 0) - 1));
    code.setReservedUntil(null);
    code.setUpdatedAt(releasedAt);
    if (code.getCodeType() == PromotionCodeType.ONE_TIME
        && code.getStatus() == PromotionCodeStatus.RESERVED) {
      code.setStatus(PromotionCodeStatus.ACTIVE);
    }
  }

  private Date reservationExpiration(Date from) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(from);
    calendar.add(Calendar.MINUTE, RESERVATION_MINUTES);
    return calendar.getTime();
  }

  private PromotionCampaignSightEvent findRedemptionCampaignSightEvent(PromotionCampaign campaign,
      PromotionCodeValidationORO response) {
    Long sightEventId = response.effects.stream()
        .map(effect -> effect.sightEvent != null ? effect.sightEvent.id : null)
        .filter(Objects::nonNull)
        .findFirst()
        .orElseGet(() -> response.effects.stream()
            .filter(effect -> effect.target != null && effect.target.sightEventIds != null
                && effect.target.sightEventIds.size() == 1)
            .map(effect -> effect.target.sightEventIds.get(0))
            .findFirst()
            .orElse(null));
    if (sightEventId == null) {
      return null;
    }
    List<PromotionCampaignSightEvent> result = em.createQuery(
        "select pcse from PromotionCampaignSightEvent pcse "
            + "join fetch pcse.sightEvent "
            + "where pcse.promotionCampaign = :campaign "
            + "and pcse.sightEvent.id = :sightEventId "
            + "and pcse.active is true",
        PromotionCampaignSightEvent.class)
        .setParameter("campaign", campaign)
        .setParameter("sightEventId", sightEventId)
        .setMaxResults(1)
        .getResultList();
    return result.isEmpty() ? null : result.get(0);
  }

  private static class CartItemContext {
    private final Long atnaId;
    private final int quantity;
    private final TicketDefinitionDTO ticketDefinition;
    private final Long hpSightEventId;
    private final Date visitDate;

    private CartItemContext(Long atnaId, int quantity, TicketDefinitionDTO ticketDefinition,
        Long hpSightEventId, Date visitDate) {
      this.atnaId = atnaId;
      this.quantity = quantity;
      this.ticketDefinition = ticketDefinition;
      this.hpSightEventId = hpSightEventId;
      this.visitDate = visitDate;
    }
  }

  private static class CartContext {
    private final List<CartItemContext> items;

    private CartContext(List<CartItemContext> items) {
      this.items = items;
    }

    private boolean isEmpty() {
      return items.isEmpty();
    }

    private Set<Long> hpSightEventIds() {
      return items.stream()
          .map(item -> item.hpSightEventId)
          .collect(Collectors.toSet());
    }

    private int quantityForHpSightEventIdWithinTicketValidity(Long hpSightEventId,
        PromotionCampaign campaign, PromotionCodeService service) {
      return items.stream()
          .filter(item -> Objects.equals(item.hpSightEventId, hpSightEventId))
          .filter(item -> item.visitDate == null
              || service.isVisitDateWithinTicketValidity(campaign, item.visitDate))
          .mapToInt(item -> item.quantity)
          .sum();
    }

    private Long firstCartItemIdForHpSightEventIdWithinTicketValidity(Long hpSightEventId,
        PromotionCampaign campaign, PromotionCodeService service) {
      return items.stream()
          .filter(item -> Objects.equals(item.hpSightEventId, hpSightEventId))
          .filter(item -> item.visitDate == null
              || service.isVisitDateWithinTicketValidity(campaign, item.visitDate))
          .map(item -> item.atnaId)
          .findFirst()
          .orElse(null);
    }

    private boolean hasOnlyItemsAfterTicketValidity(Set<Long> hpSightEventIds,
        PromotionCampaign campaign, PromotionCodeService service) {
      List<CartItemContext> matchingItems = items.stream()
          .filter(item -> hpSightEventIds.contains(item.hpSightEventId))
          .collect(Collectors.toList());
      return !matchingItems.isEmpty()
          && matchingItems.stream().allMatch(item -> item.visitDate != null
              && !service.isVisitDateWithinTicketValidity(campaign, item.visitDate));
    }

    private List<Long> cartItemIdsForHpSightEventIds(Set<Long> hpSightEventIds) {
      return items.stream()
          .filter(item -> hpSightEventIds.contains(item.hpSightEventId))
          .map(item -> item.atnaId)
          .collect(Collectors.toList());
    }
  }
}
