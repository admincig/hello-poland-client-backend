package pl.hellopoland.service;

import pl.hellopoland.bo.PromotionCampaign;
import pl.hellopoland.bo.PromotionCampaignSightEvent;
import pl.hellopoland.bo.PromotionCode;
import pl.hellopoland.bo.PromotionCodeRedemption;
import pl.hellopoland.bo.Order;
import pl.hellopoland.bo.OrderEntry;
import pl.hellopoland.bo.SightEvent;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
          "Kod nie działa dla wybranych biletów.");
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
      int cartQuantity = cartContext.quantityForHpSightEventId(hpSightEventId);
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
      effect.sourceCartItemId = cartContext.firstCartItemIdForHpSightEventId(hpSightEventId);
      effect.sightEvent = sightEventRef(hpSightEventId);
      effect.item = ticketItem(promotionalTicket, grantedQuantity);
      response.effects.add(effect);
    }

    if (response.effects.isEmpty()) {
      return invalid("PROMOTION_CONDITIONS_NOT_MET",
          "Koszyk nie spełnia warunków promocji.");
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
          "Kod nie działa dla wybranych biletów.");
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
      return invalid("PROMOTION_NOT_ACTIVE", "Promocja nie jest aktywna.");
    }

    Date now = new Date();
    if (campaign.getValidFrom().after(now)) {
      return invalid("PROMOTION_NOT_STARTED", "Promocja jeszcze się nie rozpoczęła.");
    }
    if (campaign.getValidTo().before(now)) {
      return invalid("PROMOTION_EXPIRED", "Promocja już się zakończyła.");
    }

    return PromotionCodeValidationORO.valid();
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
          "Kod nie działa dla wybranych biletów.");
    }

    return PromotionCodeValidationORO.valid();
  }

  private PromotionCodeValidationORO validateTicketPromotion(
      PromotionCampaignSightEvent campaignSightEvent) {
    if (campaignSightEvent.getTicketPoolStatus() != PromotionTicketPoolStatus.CREATED) {
      return invalid("PROMOTION_POOL_NOT_READY",
          "Pula promocyjna dla tej oferty nie jest jeszcze gotowa.");
    }
    if (campaignSightEvent.getHptAtnaId() == null
        || campaignSightEvent.getHptTicketDefinitionId() == null
        || campaignSightEvent.getHptTicketPoolDefinitionId() == null) {
      return invalid("PROMOTION_POOL_NOT_READY",
          "Pula promocyjna dla tej oferty nie jest jeszcze gotowa.");
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
    Map<Long, Integer> quantitiesByAtnaId = new LinkedHashMap<>();
    if (iro.cartItems != null) {
      iro.cartItems.stream()
          .filter(item -> item != null && item.id != null)
          .forEach(item -> quantitiesByAtnaId.merge(item.id, valueOrDefault(item.quantity, 0), Integer::sum));
    }
    quantitiesByAtnaId.entrySet().removeIf(entry -> entry.getValue() <= 0);
    if (quantitiesByAtnaId.isEmpty()) {
      return new CartContext(List.of());
    }

    Map<Long, TicketDefinitionDTO> ticketDefinitions = fetchTicketDefinitions(quantitiesByAtnaId.keySet());
    Set<Long> hptSightEventIds = ticketDefinitions.values().stream()
        .map(td -> td.sightEventId)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
    Map<Long, SightEvent> hpSightEventsByHptId = findSightEventsByHptIds(hptSightEventIds);

    List<CartItemContext> items = new ArrayList<>();
    for (Map.Entry<Long, Integer> entry : quantitiesByAtnaId.entrySet()) {
      TicketDefinitionDTO ticketDefinition = ticketDefinitions.get(entry.getKey());
      if (ticketDefinition == null || ticketDefinition.sightEventId == null) {
        continue;
      }
      SightEvent sightEvent = hpSightEventsByHptId.get(ticketDefinition.sightEventId);
      if (sightEvent == null) {
        continue;
      }
      items.add(new CartItemContext(entry.getKey(), entry.getValue(), ticketDefinition,
          sightEvent.getId()));
    }
    return new CartContext(items);
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

    redemption.setOrder(order);
    redemption.setOrderEntry(findMatchingOrderEntry(redemption, order));
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

    private CartItemContext(Long atnaId, int quantity, TicketDefinitionDTO ticketDefinition,
        Long hpSightEventId) {
      this.atnaId = atnaId;
      this.quantity = quantity;
      this.ticketDefinition = ticketDefinition;
      this.hpSightEventId = hpSightEventId;
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

    private int quantityForHpSightEventId(Long hpSightEventId) {
      return items.stream()
          .filter(item -> Objects.equals(item.hpSightEventId, hpSightEventId))
          .mapToInt(item -> item.quantity)
          .sum();
    }

    private Long firstCartItemIdForHpSightEventId(Long hpSightEventId) {
      return items.stream()
          .filter(item -> Objects.equals(item.hpSightEventId, hpSightEventId))
          .map(item -> item.atnaId)
          .findFirst()
          .orElse(null);
    }

    private List<Long> cartItemIdsForHpSightEventIds(Set<Long> hpSightEventIds) {
      return items.stream()
          .filter(item -> hpSightEventIds.contains(item.hpSightEventId))
          .map(item -> item.atnaId)
          .collect(Collectors.toList());
    }
  }
}
