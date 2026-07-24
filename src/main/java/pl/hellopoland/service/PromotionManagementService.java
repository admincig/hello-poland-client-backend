package pl.hellopoland.service;

import pl.hellopoland.bo.PromotionCampaign;
import pl.hellopoland.bo.PromotionCampaignSightEvent;
import pl.hellopoland.bo.PromotionCampaignTag;
import pl.hellopoland.bo.PromotionCode;
import pl.hellopoland.bo.PromotionCodeBatch;
import pl.hellopoland.bo.PromotionCodeRedemption;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.Tag;
import pl.hellopoland.dto.FrequencyDataDTO;
import pl.hellopoland.dto.FrequencyTypeDTO;
import pl.hellopoland.dto.TicketDefinitionDTO;
import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.dto.TicketPoolTypeDTO;
import pl.hellopoland.dto.TicketTypeDTO;
import pl.hellopoland.enums.PromotionCampaignSightEventSource;
import pl.hellopoland.enums.PromotionCodeBatchSource;
import pl.hellopoland.enums.PromotionCodeStatus;
import pl.hellopoland.enums.PromotionCodeType;
import pl.hellopoland.enums.PromotionScopeType;
import pl.hellopoland.enums.PromotionStatus;
import pl.hellopoland.enums.PromotionTicketPoolStatus;
import pl.hellopoland.enums.PromotionType;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.exception.notfound.ResourceNotFoundException;
import pl.hellopoland.rest.dto.PromotionCampaignHelpdeskDTO;
import pl.hellopoland.rest.dto.PromotionCampaignSightEventHelpdeskDTO;
import pl.hellopoland.rest.dto.PromotionCodeBatchHelpdeskDTO;
import pl.hellopoland.rest.dto.PromotionCodeHelpdeskDTO;
import pl.hellopoland.rest.dto.PromotionCodeImportIRO;
import pl.hellopoland.rest.dto.PromotionCodeRedemptionHelpdeskDTO;
import pl.hellopoland.rest.dto.PromotionCodeSetupIRO;
import pl.hellopoland.rest.dto.PromotionTargetSetupIRO;
import pl.hellopoland.rest.dto.PromotionTicketPoolGenerationIRO;
import pl.hellopoland.rest.dto.PromotionTicketPoolSetupIRO;
import pl.hellopoland.rest.dto.PromotionTicketPoolTargetPreviewDTO;
import pl.hellopoland.rest.dto.PromotionTicketPoolTargetPreviewORO;
import pl.hellopoland.util.HelloTicket;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@LocalBean
@Stateless
public class PromotionManagementService extends ServiceSuperclass {

  private static final int DEFAULT_GENERATED_CODE_LENGTH = 12;
  private static final int MAX_GENERATED_CODES = 100000;
  private static final String GENERATED_CODE_ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
  private static final String SPECIAL_TICKET_TYPE_CODE = "SPECJALNY";
  private static final String ERR_PROMOTION_CODES_REQUIRED = "PROMOTION_CODES_REQUIRED";
  private static final String ERR_PROMOTION_TICKET_TARGETS_REQUIRED =
      "PROMOTION_TICKET_TARGETS_REQUIRED";
  private static final String ERR_PROMOTION_TICKET_POOLS_REQUIRED =
      "PROMOTION_TICKET_POOLS_REQUIRED";
  private static final SecureRandom RANDOM = new SecureRandom();

  public List<PromotionCampaignHelpdeskDTO> listCampaigns() {
    return em.createQuery("from PromotionCampaign order by createdAt desc", PromotionCampaign.class)
        .getResultStream()
        .map(this::campaignDTO)
        .collect(Collectors.toList());
  }

  public PromotionCampaignHelpdeskDTO getCampaign(Long id) {
    return campaignDetailsDTO(getCampaignEntity(id));
  }

  public PromotionCampaignHelpdeskDTO createCampaign(PromotionCampaignHelpdeskDTO dto) {
    PromotionCampaign campaign = new PromotionCampaign();
    applyCampaign(campaign, dto);
    PromotionStatus requestedStatus = dto.status != null ? dto.status : PromotionStatus.DRAFT;
    campaign.setStatus(requestedStatus == PromotionStatus.ACTIVE ? PromotionStatus.DRAFT : requestedStatus);
    campaign.setCreatedAt(new Date());
    campaign.setCreatedBy(getLoggedUser());
    em.persist(campaign);
    applyCampaignTags(campaign, dto.tagIds);
    applyManualCampaignTargets(campaign, dto.targetSetup);
    if (dto.codeSetup != null) {
      createInitialCodes(campaign, dto.codeSetup);
    }
    if (campaign.getPromotionType() == PromotionType.TICKET && dto.ticketPoolSetup != null) {
      generateTicketPools(campaign, dto.targetSetup, dto.ticketPoolSetup);
    }
    if (requestedStatus == PromotionStatus.ACTIVE) {
      validateCanActivate(campaign);
      campaign.setStatus(PromotionStatus.ACTIVE);
    }
    return campaignDetailsDTO(campaign);
  }

  public PromotionCampaignHelpdeskDTO updateCampaign(Long id, PromotionCampaignHelpdeskDTO dto) {
    PromotionCampaign campaign = getCampaignEntity(id);
    assertCampaignDraft(campaign);
    applyCampaign(campaign, dto);
    if (dto.status != null) {
      if (dto.status == PromotionStatus.ACTIVE) {
        validateCanActivate(campaign);
      }
      campaign.setStatus(dto.status);
    }
    campaign.setUpdatedAt(new Date());
    campaign.setUpdatedBy(getLoggedUser());
    applyCampaignTags(campaign, dto.tagIds);
    return campaignDetailsDTO(campaign);
  }

  public PromotionCampaignHelpdeskDTO changeCampaignStatus(Long id, PromotionCampaignHelpdeskDTO dto) {
    if (dto == null || dto.status == null) {
      throw new ConflictingException("Status promocji jest wymagany.");
    }
    PromotionCampaign campaign = getCampaignEntity(id);
    validateStatusTransition(campaign, dto.status);
    if (dto.status == PromotionStatus.ACTIVE) {
      validateCanActivate(campaign);
    }
    campaign.setStatus(dto.status);
    campaign.setUpdatedAt(new Date());
    campaign.setUpdatedBy(getLoggedUser());
    return campaignDetailsDTO(campaign);
  }

  public PromotionCampaignSightEventHelpdeskDTO addSightEvent(Long campaignId,
      PromotionCampaignSightEventHelpdeskDTO dto) {
    if (dto == null || dto.sightEventId == null) {
      throw new ConflictingException("Oferta jest wymagana.");
    }

    PromotionCampaign campaign = getCampaignEntity(campaignId);
    assertCampaignDraft(campaign);
    SightEvent sightEvent = getSightEvent(dto.sightEventId);
    PromotionCampaignSightEvent relation = findCampaignSightEvent(campaign, sightEvent);
    if (relation == null) {
      relation = new PromotionCampaignSightEvent();
      relation.setPromotionCampaign(campaign);
      relation.setSightEvent(sightEvent);
      relation.setCreatedAt(new Date());
      em.persist(relation);
    }

    relation.setActive(true);
    relation.setSource(dto.source != null ? dto.source : PromotionCampaignSightEventSource.MANUAL);
    relation.setSourceTag(dto.sourceTagId != null ? getTag(dto.sourceTagId) : null);
    prepareTicketPoolStatus(campaign, relation);
    return sightEventDTO(relation);
  }

  public PromotionCampaignSightEventHelpdeskDTO updateSightEvent(Long campaignId, Long relationId,
      PromotionCampaignSightEventHelpdeskDTO dto) {
    PromotionCampaign campaign = getCampaignEntity(campaignId);
    assertCampaignDraft(campaign);
    PromotionCampaignSightEvent relation = getCampaignSightEventEntity(relationId);
    if (!Objects.equals(relation.getPromotionCampaign().getId(), campaign.getId())) {
      throw new ResourceNotFoundException();
    }
    if (dto != null) {
      if (dto.active != null) {
        relation.setActive(dto.active);
      }
      prepareTicketPoolStatus(campaign, relation);
    }
    return sightEventDTO(relation);
  }

  public void removeSightEvent(Long campaignId, Long relationId) {
    PromotionCampaign campaign = getCampaignEntity(campaignId);
    assertCampaignDraft(campaign);
    PromotionCampaignSightEvent relation = getCampaignSightEventEntity(relationId);
    if (!Objects.equals(relation.getPromotionCampaign().getId(), campaign.getId())) {
      throw new ResourceNotFoundException();
    }
    relation.setActive(false);
    relation.setUpdatedAt(new Date());
  }

  public List<PromotionCodeHelpdeskDTO> listCodes(Long campaignId) {
    PromotionCampaign campaign = getCampaignEntity(campaignId);
    return em.createQuery(
        "from PromotionCode where promotionCampaign = :campaign order by createdAt desc",
        PromotionCode.class)
        .setParameter("campaign", campaign)
        .getResultStream()
        .map(this::codeDTO)
        .collect(Collectors.toList());
  }

  public List<PromotionCodeHelpdeskDTO> createCodes(Long campaignId, PromotionCodeSetupIRO iro) {
    PromotionCampaign campaign = getCampaignEntity(campaignId);
    assertCampaignDraft(campaign);
    return createInitialCodes(campaign, iro);
  }

  public List<PromotionCodeHelpdeskDTO> replaceCodes(Long campaignId, PromotionCodeSetupIRO iro) {
    PromotionCampaign campaign = getCampaignEntity(campaignId);
    assertCampaignDraft(campaign);
    assertNoCodeRedemptions(campaign);
    removeExistingCodes(campaign);
    return createInitialCodes(campaign, iro);
  }

  public String exportCodesCsv(Long campaignId) {
    PromotionCampaign campaign = getCampaignEntity(campaignId);
    List<PromotionCode> codes = em.createQuery(
        "from PromotionCode where promotionCampaign = :campaign order by createdAt asc, id asc",
        PromotionCode.class)
        .setParameter("campaign", campaign)
        .getResultList();

    StringBuilder csv = new StringBuilder();
    csv.append("code,status,code_type,max_redemptions,max_redemptions_per_customer,")
        .append("max_redemptions_per_day,reserved_redemptions_count,used_redemptions_count,")
        .append("reserved_until,created_at,updated_at,disabled_at\n");
    for (PromotionCode code : codes) {
      appendCsvRow(csv,
          code.getCode(),
          code.getStatus(),
          code.getCodeType(),
          code.getMaxRedemptions(),
          code.getMaxRedemptionsPerCustomer(),
          code.getMaxRedemptionsPerDay(),
          code.getReservedRedemptionsCount(),
          code.getUsedRedemptionsCount(),
          code.getReservedUntil(),
          code.getCreatedAt(),
          code.getUpdatedAt(),
          code.getDisabledAt());
    }
    return csv.toString();
  }

  public List<PromotionCodeBatchHelpdeskDTO> listBatches(Long campaignId) {
    PromotionCampaign campaign = getCampaignEntity(campaignId);
    return em.createQuery(
        "from PromotionCodeBatch where promotionCampaign = :campaign order by createdAt desc",
        PromotionCodeBatch.class)
        .setParameter("campaign", campaign)
        .getResultStream()
        .map(this::batchDTO)
        .collect(Collectors.toList());
  }

  public List<PromotionCodeHelpdeskDTO> importCodes(Long campaignId, PromotionCodeImportIRO iro) {
    PromotionCampaign campaign = getCampaignEntity(campaignId);
    assertCampaignDraft(campaign);
    List<String> codes = normalizedCodes(iro);
    assertCodesDoNotExist(codes);

    PromotionCodeType codeType = iro.codeType != null ? iro.codeType : PromotionCodeType.ONE_TIME;
    PromotionCodeBatch batch =
        createBatch(campaign, PromotionCodeBatchSource.IMPORT, iro.fileName, codes.size());
    return createCodes(campaign, batch, codes, codeType, iro.maxRedemptions,
        iro.maxRedemptionsPerCustomer, iro.maxRedemptionsPerDay);
  }

  public PromotionCodeHelpdeskDTO updateCode(Long campaignId, Long codeId,
      PromotionCodeHelpdeskDTO dto) {
    if (dto == null) {
      throw new ConflictingException("Dane kodu są wymagane.");
    }
    PromotionCampaign campaign = getCampaignEntity(campaignId);
    PromotionCode code = getCodeEntity(codeId);
    if (!Objects.equals(code.getPromotionCampaign().getId(), campaign.getId())) {
      throw new ResourceNotFoundException();
    }
    validateCodeUpdate(campaign, code, dto);
    if (dto.status != null) {
      code.setStatus(dto.status);
      code.setDisabledAt(dto.status == PromotionCodeStatus.DISABLED ? new Date() : null);
    }
    if (campaign.getStatus() == PromotionStatus.DRAFT && code.getCodeType() == PromotionCodeType.FIXED) {
      code.setMaxRedemptions(dto.maxRedemptions);
      code.setMaxRedemptionsPerCustomer(dto.maxRedemptionsPerCustomer);
      code.setMaxRedemptionsPerDay(dto.maxRedemptionsPerDay);
    }
    code.setUpdatedAt(new Date());
    return codeDTO(code);
  }

  public List<PromotionCodeRedemptionHelpdeskDTO> listRedemptions(Long campaignId) {
    PromotionCampaign campaign = getCampaignEntity(campaignId);
    return em.createQuery(
        "from PromotionCodeRedemption where promotionCampaign = :campaign order by createdAt desc",
        PromotionCodeRedemption.class)
        .setParameter("campaign", campaign)
        .getResultStream()
        .map(this::redemptionDTO)
        .collect(Collectors.toList());
  }

  public PromotionCampaignHelpdeskDTO generateTicketPools(Long campaignId,
      PromotionTicketPoolGenerationIRO iro) {
    PromotionCampaign campaign = getCampaignEntity(campaignId);
    assertCampaignDraft(campaign);
    if (iro == null) {
      throw new ConflictingException("Podaj zakres i parametry puli promocyjnej.");
    }
    generateTicketPools(campaign, iro.targetSetup, iro.ticketPoolSetup);
    return campaignDetailsDTO(campaign);
  }

  public PromotionTicketPoolTargetPreviewORO previewTicketPoolTargets(Long campaignId,
      PromotionTicketPoolGenerationIRO iro) {
    PromotionCampaign campaign = getCampaignEntity(campaignId);
    PromotionTargetSetupIRO targetSetup = iro != null ? iro.targetSetup : null;
    List<SightEvent> sightEvents = resolvePromotionTargets(campaign, targetSetup);

    PromotionTicketPoolTargetPreviewORO oro = new PromotionTicketPoolTargetPreviewORO();
    oro.sightEvents = sightEvents.stream()
        .map(sightEvent -> targetPreviewDTO(campaign, sightEvent))
        .collect(Collectors.toList());
    oro.count = oro.sightEvents.size();
    return oro;
  }

  private void generateTicketPools(PromotionCampaign campaign, PromotionTargetSetupIRO targetSetup,
      PromotionTicketPoolSetupIRO ticketPoolSetup) {
    if (campaign.getPromotionType() != PromotionType.TICKET) {
      throw new ConflictingException("Pule promocyjne można generować tylko dla promocji TICKET.");
    }
    validateTicketPoolSetup(ticketPoolSetup);
    List<SightEvent> sightEvents = resolvePromotionTargets(campaign, targetSetup);
    if (sightEvents.isEmpty()) {
      throw new ConflictingException(
          "Nie znaleziono aktywnych ofert z powiązaniem HT dla wskazanego zakresu promocji.");
    }

    for (SightEvent sightEvent : sightEvents) {
      PromotionCampaignSightEvent relation = ensureCampaignSightEvent(campaign, sightEvent,
          sourceForTarget(campaign, sightEvent), sourceTagForTarget(campaign, sightEvent));
      if (relation.getTicketPoolStatus() == PromotionTicketPoolStatus.CREATED
          && relation.getHptAtnaId() != null) {
        continue;
      }
      createPromotionTicketPool(campaign, relation, ticketPoolSetup);
    }
  }

  private void createPromotionTicketPool(PromotionCampaign campaign,
      PromotionCampaignSightEvent relation, PromotionTicketPoolSetupIRO setup) {
    SightEvent sightEvent = relation.getSightEvent();
    relation.setTicketPoolStatus(PromotionTicketPoolStatus.CONFIG_REQUIRED);
    relation.setHptSightEventId(sightEvent.getHptId());
    relation.setUpdatedAt(new Date());

    try {
      HelloTicket hpt = new HelloTicket(getPortal("Hello Ticket Cloud").getUrl());
      String hptToken = sightEvent.getPartner().getHptToken();
      TicketTypeDTO specialTicketType = findSpecialTicketType(hpt, hptToken);
      TicketDefinitionDTO ticketDefinition =
          createSpecialTicketDefinition(hpt, hptToken, campaign, setup, specialTicketType);
      TicketPoolDefinitionDTO ticketPool =
          createSpecialTicketPool(hpt, hptToken, campaign, relation, setup, ticketDefinition);
      TicketDefinitionDTO poolTicket = resolveCreatedPoolTicket(hpt, hptToken, ticketPool,
          ticketDefinition);

      relation.setHptAtnaId(poolTicket.atnaId);
      relation.setHptTicketDefinitionId(poolTicket.id);
      relation.setHptTicketPoolDefinitionId(ticketPool.id);
      relation.setTicketPoolStatus(PromotionTicketPoolStatus.CREATED);
      relation.setUpdatedAt(new Date());
    } catch (Exception e) {
      relation.setTicketPoolStatus(PromotionTicketPoolStatus.ERROR);
      relation.setUpdatedAt(new Date());
      throw e;
    }
  }

  private TicketTypeDTO findSpecialTicketType(HelloTicket hpt, String hptToken) {
    return hpt.getTicketTypes(hptToken).stream()
        .filter(type -> SPECIAL_TICKET_TYPE_CODE.equals(type.code))
        .findFirst()
        .orElseThrow(() -> new ConflictingException("Brak typu biletu SPECJALNY w HT."));
  }

  private TicketDefinitionDTO createSpecialTicketDefinition(HelloTicket hpt, String hptToken,
      PromotionCampaign campaign, PromotionTicketPoolSetupIRO setup, TicketTypeDTO ticketType) {
    TicketDefinitionDTO dto = new TicketDefinitionDTO();
    dto.name = setup.ticketName != null && !setup.ticketName.isBlank()
        ? setup.ticketName
        : "Bilet promocyjny - " + campaign.getName();
    dto.price = setup.ticketPrice;
    dto.originalPrice = setup.ticketPrice;
    dto.ticketTypeId = ticketType.id;
    return hpt.addTicketDefinition(dto, hptToken);
  }

  private TicketPoolDefinitionDTO createSpecialTicketPool(HelloTicket hpt, String hptToken,
      PromotionCampaign campaign, PromotionCampaignSightEvent relation,
      PromotionTicketPoolSetupIRO setup, TicketDefinitionDTO ticketDefinition) {
    Date startDate = setup.startDate != null ? setup.startDate : campaign.getValidFrom();
    Date endDate = setup.endDate != null ? setup.endDate : campaign.getValidTo();

    TicketDefinitionDTO poolTicket = new TicketDefinitionDTO();
    poolTicket.id = ticketDefinition.id;
    poolTicket.availableTicketsNumber = -1;

    TicketPoolDefinitionDTO pool = new TicketPoolDefinitionDTO();
    pool.name = setup.poolName != null && !setup.poolName.isBlank()
        ? setup.poolName
        : "Promocja - " + campaign.getName();
    pool.availableTicketsNumber = setup.availableTicketsNumber;
    pool.isCyclic = setup.isCyclic == null || setup.isCyclic;
    pool.startDate = startDate;
    pool.endDate = endDate;
    pool.entryStartDate = setup.entryStartDate != null ? setup.entryStartDate : startDate;
    pool.entryEndDate = setup.entryEndDate != null ? setup.entryEndDate : endDate;
    pool.sightEventId = relation.getSightEvent().getHptId();
    pool.ticketDefinitions = List.of(poolTicket);
    pool.wholeDay = setup.wholeDay == null || setup.wholeDay;
    pool.poolType = TicketPoolTypeDTO.PROMOTIONAL;
    pool.visibleForPartner = false;
    pool.visibleOnPortal = false;
    if (pool.isCyclic) {
      pool.frequencyData = new FrequencyDataDTO();
      pool.frequencyData.frequency = 1;
      pool.frequencyData.frequencyType = FrequencyTypeDTO.DAILY;
      pool.frequencyData.startDate = startDate;
      pool.frequencyData.endDate = endDate;
    }
    return hpt.addTicketPoolDefinition(pool, hptToken);
  }

  private TicketDefinitionDTO resolveCreatedPoolTicket(HelloTicket hpt, String hptToken,
      TicketPoolDefinitionDTO ticketPool, TicketDefinitionDTO ticketDefinition) {
    TicketDefinitionDTO fromPool = ticketPool.ticketDefinitions != null
        ? ticketPool.ticketDefinitions.stream()
            .filter(td -> Objects.equals(td.id, ticketDefinition.id))
            .findFirst()
            .orElse(null)
        : null;
    if (fromPool != null && fromPool.atnaId != null) {
      return fromPool;
    }
    return hpt.getTicketDefinitions(hptToken).stream()
        .filter(td -> Objects.equals(td.id, ticketDefinition.id))
        .filter(td -> Objects.equals(td.poolId, ticketPool.id))
        .findFirst()
        .orElseThrow(() -> new ConflictingException(
            "Nie udało się odczytać ATNA biletu promocyjnego z HT."));
  }

  private void validateTicketPoolSetup(PromotionTicketPoolSetupIRO setup) {
    if (setup == null) {
      throw new ConflictingException("Podaj parametry puli promocyjnej.");
    }
    if (setup.ticketPrice == null || setup.ticketPrice < 0) {
      throw new ConflictingException("Podaj poprawną cenę biletu promocyjnego.");
    }
    if (setup.availableTicketsNumber == null || setup.availableTicketsNumber <= 0) {
      throw new ConflictingException("Podaj liczbę biletów promocyjnych dla oferty.");
    }
    Date startDate = setup.startDate;
    Date endDate = setup.endDate;
    if (startDate != null && endDate != null && endDate.before(startDate)) {
      throw new ConflictingException("Data końca puli nie może być przed datą startu.");
    }
  }

  private List<SightEvent> resolvePromotionTargets(PromotionCampaign campaign,
      PromotionTargetSetupIRO setup) {
    Set<Long> sightEventIds = new LinkedHashSet<>();
    if (setup != null) {
      addAll(sightEventIds, setup.sightEventIds);
      addSightEventsForSights(sightEventIds, setup.sightIds);
      addSightEventsForPartners(sightEventIds, setup.partnerIds);
      addSightEventsForTags(sightEventIds, setup.tagIds);
    }
    if (campaign.getScopeType() == PromotionScopeType.TAG) {
      addSightEventsForTags(sightEventIds, activeCampaignTagIds(campaign));
    }
    addAll(sightEventIds, activeCampaignSightEventIds(campaign));

    if (sightEventIds.isEmpty()) {
      return List.of();
    }
    return em.createQuery(
        "select distinct se from SightEvent se "
            + "join fetch se.partner "
            + "join fetch se.sight "
            + "where se.id in :ids "
            + "and se.active is true "
            + "and se.blocked is false "
            + "and se.hptId is not null",
        SightEvent.class)
        .setParameter("ids", sightEventIds)
        .getResultList();
  }

  private void addSightEventsForSights(Set<Long> sightEventIds, List<Long> sightIds) {
    if (sightIds == null || sightIds.isEmpty()) {
      return;
    }
    sightEventIds.addAll(em.createQuery(
        "select se.id from SightEvent se where se.sight.id in :sightIds",
        Long.class)
        .setParameter("sightIds", sightIds)
        .getResultList());
  }

  private void addSightEventsForPartners(Set<Long> sightEventIds, List<Long> partnerIds) {
    if (partnerIds == null || partnerIds.isEmpty()) {
      return;
    }
    sightEventIds.addAll(em.createQuery(
        "select se.id from SightEvent se where se.partner.id in :partnerIds",
        Long.class)
        .setParameter("partnerIds", partnerIds)
        .getResultList());
  }

  private void addSightEventsForTags(Set<Long> sightEventIds, List<Long> tagIds) {
    if (tagIds == null || tagIds.isEmpty()) {
      return;
    }
    sightEventIds.addAll(em.createQuery(
        "select distinct set.sightEvent.id from SightEventTag set where set.tag.id in :tagIds",
        Long.class)
        .setParameter("tagIds", tagIds)
        .getResultList());
  }

  private List<Long> activeCampaignTagIds(PromotionCampaign campaign) {
    return em.createQuery(
        "select pct.tag.id from PromotionCampaignTag pct where pct.promotionCampaign = :campaign "
            + "and pct.active is true",
        Long.class)
        .setParameter("campaign", campaign)
        .getResultList();
  }

  private List<Long> activeCampaignSightEventIds(PromotionCampaign campaign) {
    return em.createQuery(
        "select pcse.sightEvent.id from PromotionCampaignSightEvent pcse "
            + "where pcse.promotionCampaign = :campaign and pcse.active is true",
        Long.class)
        .setParameter("campaign", campaign)
        .getResultList();
  }

  private void addAll(Set<Long> target, List<Long> source) {
    if (source != null) {
      source.stream().filter(Objects::nonNull).forEach(target::add);
    }
  }

  private PromotionCampaignSightEvent ensureCampaignSightEvent(PromotionCampaign campaign,
      SightEvent sightEvent, PromotionCampaignSightEventSource source, Tag sourceTag) {
    PromotionCampaignSightEvent relation = findCampaignSightEvent(campaign, sightEvent);
    if (relation == null) {
      relation = new PromotionCampaignSightEvent();
      relation.setPromotionCampaign(campaign);
      relation.setSightEvent(sightEvent);
      relation.setCreatedAt(new Date());
      em.persist(relation);
    }
    relation.setActive(true);
    relation.setSource(source);
    relation.setSourceTag(sourceTag);
    relation.setHptSightEventId(sightEvent.getHptId());
    if (relation.getTicketPoolStatus() == null) {
      relation.setTicketPoolStatus(PromotionTicketPoolStatus.NOT_CREATED);
    }
    relation.setUpdatedAt(new Date());
    return relation;
  }

  private PromotionCampaignSightEventSource sourceForTarget(PromotionCampaign campaign,
      SightEvent sightEvent) {
    return sourceTagForTarget(campaign, sightEvent) != null
        ? PromotionCampaignSightEventSource.TAG
        : PromotionCampaignSightEventSource.MANUAL;
  }

  private Tag sourceTagForTarget(PromotionCampaign campaign, SightEvent sightEvent) {
    List<Long> tagIds = activeCampaignTagIds(campaign);
    if (tagIds.isEmpty()) {
      return null;
    }
    return em.createQuery(
        "select set.tag from SightEventTag set where set.sightEvent = :sightEvent "
            + "and set.tag.id in :tagIds",
        Tag.class)
        .setParameter("sightEvent", sightEvent)
        .setParameter("tagIds", tagIds)
        .setMaxResults(1)
        .getResultStream()
        .findFirst()
        .orElse(null);
  }

  private List<PromotionCodeHelpdeskDTO> createInitialCodes(PromotionCampaign campaign,
      PromotionCodeSetupIRO setup) {
    if (setup == null) {
      throw new ConflictingException("Podaj konfigurację kodów dla promocji.");
    }

    boolean hasImportCodes = hasImportCodes(setup);
    boolean hasFixedCode = setup.fixedCode != null && !setup.fixedCode.trim().isBlank();
    boolean hasGenerateParams = setup.generateCount != null;
    int selectedSources = (hasImportCodes ? 1 : 0) + (hasFixedCode ? 1 : 0)
        + (hasGenerateParams ? 1 : 0);
    if (selectedSources > 1) {
      throw new ConflictingException(
          "Podaj listę kodów, jeden kod stały albo parametry generatora.");
    }

    if (hasImportCodes) {
      return createImportedCodes(campaign, setup);
    }
    if (hasFixedCode) {
      return createFixedCode(campaign, setup);
    }
    return createGeneratedCodes(campaign, setup);
  }

  private List<PromotionCodeHelpdeskDTO> createImportedCodes(PromotionCampaign campaign,
      PromotionCodeSetupIRO setup) {
    List<String> codes = normalizedCodes(setup.codes);
    assertCodesDoNotExist(codes);
    PromotionCodeType codeType = setup.codeType != null ? setup.codeType : PromotionCodeType.ONE_TIME;
    PromotionCodeBatch batch =
        createBatch(campaign, PromotionCodeBatchSource.IMPORT, setup.fileName, codes.size());
    return createCodes(campaign, batch, codes, codeType, setup.maxRedemptions,
        setup.maxRedemptionsPerCustomer, setup.maxRedemptionsPerDay);
  }

  private List<PromotionCodeHelpdeskDTO> createFixedCode(PromotionCampaign campaign,
      PromotionCodeSetupIRO setup) {
    List<String> codes = List.of(setup.fixedCode.trim());
    assertCodesDoNotExist(codes);
    PromotionCodeBatch batch =
        createBatch(campaign, PromotionCodeBatchSource.IMPORT,
            setup.fileName != null ? setup.fileName : "manual-fixed-code", codes.size());
    return createCodes(campaign, batch, codes, PromotionCodeType.FIXED, setup.maxRedemptions,
        setup.maxRedemptionsPerCustomer, setup.maxRedemptionsPerDay);
  }

  private List<PromotionCodeHelpdeskDTO> createGeneratedCodes(PromotionCampaign campaign,
      PromotionCodeSetupIRO setup) {
    if (setup.generateCount == null || setup.generateCount <= 0
        || setup.generateCount > MAX_GENERATED_CODES) {
      throw new ConflictingException("Podaj poprawną liczbę kodów do wygenerowania.");
    }

    List<String> codes = generateUniqueCodes(setup);
    PromotionCodeType codeType = setup.codeType != null ? setup.codeType : PromotionCodeType.ONE_TIME;
    PromotionCodeBatch batch =
        createBatch(campaign, PromotionCodeBatchSource.GENERATED, setup.fileName, codes.size());
    return createCodes(campaign, batch, codes, codeType, setup.maxRedemptions,
        setup.maxRedemptionsPerCustomer, setup.maxRedemptionsPerDay);
  }

  private PromotionCodeBatch createBatch(PromotionCampaign campaign, PromotionCodeBatchSource source,
      String fileName, int codesCount) {
    PromotionCodeBatch batch = new PromotionCodeBatch();
    batch.setPromotionCampaign(campaign);
    batch.setSource(source);
    batch.setFileName(fileName);
    batch.setCodesCount(codesCount);
    batch.setCreatedAt(new Date());
    batch.setCreatedBy(getLoggedUser());
    em.persist(batch);
    return batch;
  }

  private List<PromotionCodeHelpdeskDTO> createCodes(PromotionCampaign campaign,
      PromotionCodeBatch batch, List<String> codeValues, PromotionCodeType codeType,
      Integer maxRedemptions, Integer maxRedemptionsPerCustomer, Integer maxRedemptionsPerDay) {
    List<PromotionCodeHelpdeskDTO> result = new ArrayList<>();
    for (String codeValue : codeValues) {
      PromotionCode code = new PromotionCode();
      code.setPromotionCampaign(campaign);
      code.setPromotionCodeBatch(batch);
      code.setCode(codeValue);
      code.setCodeType(codeType);
      code.setStatus(PromotionCodeStatus.ACTIVE);
      code.setMaxRedemptions(codeType == PromotionCodeType.ONE_TIME ? Integer.valueOf(1) : maxRedemptions);
      code.setMaxRedemptionsPerCustomer(maxRedemptionsPerCustomer);
      code.setMaxRedemptionsPerDay(maxRedemptionsPerDay);
      code.setCreatedAt(new Date());
      em.persist(code);
      result.add(codeDTO(code));
    }
    return result;
  }

  private List<String> generateUniqueCodes(PromotionCodeSetupIRO setup) {
    int length = setup.generatedCodeLength != null
        ? setup.generatedCodeLength
        : DEFAULT_GENERATED_CODE_LENGTH;
    if (length <= 0 || length > 64) {
      throw new ConflictingException("Podaj poprawną długość kodu.");
    }

    Set<String> codes = new LinkedHashSet<>();
    int attempts = 0;
    while (codes.size() < setup.generateCount) {
      if (attempts++ > 20) {
        throw new ConflictingException("Nie udało się wygenerować unikalnych kodów.");
      }
      while (codes.size() < setup.generateCount) {
        codes.add(generatedCode(setup, length));
      }
      findExistingCodeValues(new ArrayList<>(codes)).forEach(codes::remove);
    }
    return new ArrayList<>(codes);
  }

  private String generatedCode(PromotionCodeSetupIRO setup, int length) {
    StringBuilder randomPart = new StringBuilder();
    for (int i = 0; i < length; i++) {
      randomPart.append(GENERATED_CODE_ALPHABET.charAt(
          RANDOM.nextInt(GENERATED_CODE_ALPHABET.length())));
    }
    String prefix = setup.generatedCodePrefix != null ? setup.generatedCodePrefix.trim() : "";
    if (prefix.isBlank()) {
      return randomPart.toString();
    }
    String separator = setup.generatedCodeSeparator != null ? setup.generatedCodeSeparator : "-";
    return prefix + separator + randomPart;
  }

  private void appendCsvRow(StringBuilder csv, Object... values) {
    for (int i = 0; i < values.length; i++) {
      if (i > 0) {
        csv.append(',');
      }
      csv.append(csvValue(values[i]));
    }
    csv.append('\n');
  }

  private String csvValue(Object value) {
    if (value == null) {
      return "";
    }
    String text = value instanceof Date
        ? new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format((Date) value)
        : String.valueOf(value);
    return "\"" + text.replace("\"", "\"\"") + "\"";
  }

  private void validateCanActivate(PromotionCampaign campaign) {
    Long codesCount = em.createQuery(
        "select count(code) from PromotionCode code where code.promotionCampaign = :campaign "
            + "and code.status <> :disabledStatus",
        Long.class)
        .setParameter("campaign", campaign)
        .setParameter("disabledStatus", PromotionCodeStatus.DISABLED)
        .getSingleResult();
    if (codesCount == 0) {
      throw new ConflictingException("Nie można uruchomić promocji bez kodów.",
          ERR_PROMOTION_CODES_REQUIRED);
    }

    if (campaign.getPromotionType() == PromotionType.TICKET) {
      validateTicketPromotionReady(campaign);
    }
  }

  private void validateTicketPromotionReady(PromotionCampaign campaign) {
    List<SightEvent> sightEvents = resolvePromotionTargets(campaign, null);
    if (sightEvents.isEmpty()) {
      throw new ConflictingException(
          "Nie można uruchomić promocji TICKET bez ofert objętych promocją.",
          ERR_PROMOTION_TICKET_TARGETS_REQUIRED);
    }

    List<String> missingPools = sightEvents.stream()
        .filter(sightEvent -> !hasReadyTicketPool(campaign, sightEvent))
        .map(SightEvent::getName)
        .collect(Collectors.toList());
    if (!missingPools.isEmpty()) {
      String examples = missingPools.stream().limit(5).collect(Collectors.joining(", "));
      String suffix = missingPools.size() > 5 ? "..." : "";
      throw new ConflictingException(
          "Nie można uruchomić promocji TICKET. Brak gotowych pul promocyjnych dla "
              + missingPools.size() + " ofert: " + examples + suffix,
          ERR_PROMOTION_TICKET_POOLS_REQUIRED);
    }
  }

  private boolean hasReadyTicketPool(PromotionCampaign campaign, SightEvent sightEvent) {
    PromotionCampaignSightEvent relation = findCampaignSightEvent(campaign, sightEvent);
    return relation != null
        && relation.isActive()
        && relation.getTicketPoolStatus() == PromotionTicketPoolStatus.CREATED
        && relation.getHptAtnaId() != null;
  }

  private void validateStatusTransition(PromotionCampaign campaign, PromotionStatus targetStatus) {
    PromotionStatus currentStatus = campaign.getStatus();
    if (Objects.equals(currentStatus, targetStatus)) {
      return;
    }
    if (currentStatus == PromotionStatus.ACTIVE && targetStatus == PromotionStatus.DISABLED) {
      return;
    }
    if (currentStatus == PromotionStatus.DRAFT
        && (targetStatus == PromotionStatus.ACTIVE || targetStatus == PromotionStatus.DISABLED)) {
      return;
    }
    throw new ConflictingException(
        "Po uruchomieniu promocji można ją tylko zakończyć przed czasem.");
  }

  private void assertCampaignDraft(PromotionCampaign campaign) {
    if (campaign.getStatus() != PromotionStatus.DRAFT) {
      throw new ConflictingException("Promocję można konfigurować tylko w statusie DRAFT.");
    }
  }

  private void assertNoCodeRedemptions(PromotionCampaign campaign) {
    Long redemptionsCount = em.createQuery(
        "select count(redemption) from PromotionCodeRedemption redemption "
            + "where redemption.promotionCampaign = :campaign",
        Long.class)
        .setParameter("campaign", campaign)
        .getSingleResult();
    if (redemptionsCount > 0) {
      throw new ConflictingException("Nie można zastąpić kodów, bo promocja ma już użycia kodów.");
    }
  }

  private void validateCodeUpdate(PromotionCampaign campaign, PromotionCode code,
      PromotionCodeHelpdeskDTO dto) {
    if (campaign.getStatus() == PromotionStatus.DRAFT) {
      return;
    }
    boolean disableOnly = dto != null
        && dto.status == PromotionCodeStatus.DISABLED
        && code.getStatus() == PromotionCodeStatus.ACTIVE
        && dto.maxRedemptions == null
        && dto.maxRedemptionsPerCustomer == null
        && dto.maxRedemptionsPerDay == null;
    if ((campaign.getStatus() == PromotionStatus.ACTIVE
        || campaign.getStatus() == PromotionStatus.DISABLED) && disableOnly) {
      return;
    }
    throw new ConflictingException(
        "Po uruchomieniu promocji można tylko wyłączyć aktywny kod.");
  }

  private void removeExistingCodes(PromotionCampaign campaign) {
    em.createQuery("delete from PromotionCode code where code.promotionCampaign = :campaign")
        .setParameter("campaign", campaign)
        .executeUpdate();
    em.createQuery("delete from PromotionCodeBatch batch where batch.promotionCampaign = :campaign")
        .setParameter("campaign", campaign)
        .executeUpdate();
    em.flush();
  }

  private void applyCampaign(PromotionCampaign campaign, PromotionCampaignHelpdeskDTO dto) {
    if (dto == null || dto.name == null || dto.name.isBlank()) {
      throw new ConflictingException("Nazwa promocji jest wymagana.");
    }
    if (dto.promotionType == null || dto.scopeType == null) {
      throw new ConflictingException("Typ promocji i zakres są wymagane.");
    }
    validateCampaignTargetSelection(dto);
    if (dto.validFrom == null || dto.validTo == null || dto.validTo.before(dto.validFrom)) {
      throw new ConflictingException("Podaj poprawny okres obowiązywania promocji.");
    }
    if (dto.promotionType == PromotionType.TICKET && dto.scopeType == PromotionScopeType.GLOBAL) {
      throw new ConflictingException("Promocja biletowa nie może być globalna.");
    }
    if (dto.promotionType == PromotionType.PERCENT && dto.discountPercent == null) {
      throw new ConflictingException("Promocja procentowa wymaga wartości procentowej.");
    }
    if (dto.promotionType == PromotionType.AMOUNT && dto.discountAmountGross == null) {
      throw new ConflictingException("Promocja kwotowa wymaga kwoty brutto.");
    }

    campaign.setName(dto.name);
    campaign.setPromotionType(dto.promotionType);
    campaign.setScopeType(dto.scopeType);
    campaign.setValidFrom(dto.validFrom);
    campaign.setValidTo(dto.validTo);
    campaign.setGlobalLimit(dto.globalLimit);
    campaign.setCodeLimit(dto.codeLimit);
    campaign.setCustomerLimit(dto.customerLimit);
    campaign.setDailyLimit(dto.dailyLimit);
    campaign.setRequiredTicketQuantity(dto.requiredTicketQuantity);
    campaign.setGrantedTicketQuantity(dto.grantedTicketQuantity);
    campaign.setDiscountPercent(dto.promotionType == PromotionType.PERCENT ? dto.discountPercent : null);
    campaign.setDiscountAmountGross(dto.promotionType == PromotionType.AMOUNT
        ? dto.discountAmountGross : null);
  }

  private void applyManualCampaignTargets(PromotionCampaign campaign, PromotionTargetSetupIRO setup) {
    if (campaign.getScopeType() != PromotionScopeType.MANUAL || setup == null) {
      return;
    }
    for (Long sightEventId : new LinkedHashSet<>(setup.sightEventIds)) {
      if (sightEventId != null) {
        ensureCampaignSightEvent(campaign, getSightEvent(sightEventId),
            PromotionCampaignSightEventSource.MANUAL, null);
      }
    }
  }

  private void validateCampaignTargetSelection(PromotionCampaignHelpdeskDTO dto) {
    if (dto.scopeType == PromotionScopeType.GLOBAL) {
      return;
    }
    if (dto.scopeType == PromotionScopeType.TAG && !hasItems(dto.tagIds)) {
      throw new ConflictingException("Wybierz tag dla promocji.");
    }
    if (dto.scopeType == PromotionScopeType.MANUAL
        && (dto.targetSetup == null || !hasItems(dto.targetSetup.sightEventIds))) {
      throw new ConflictingException("Wybierz oferty dla promocji.");
    }
  }

  private boolean hasItems(List<Long> items) {
    return items != null && items.stream().anyMatch(Objects::nonNull);
  }

  private void applyCampaignTags(PromotionCampaign campaign, List<Long> tagIds) {
    if (tagIds == null) {
      return;
    }
    em.createQuery("from PromotionCampaignTag where promotionCampaign = :campaign",
        PromotionCampaignTag.class)
        .setParameter("campaign", campaign)
        .getResultList()
        .forEach(tag -> tag.setActive(false));

    for (Long tagId : new LinkedHashSet<>(tagIds)) {
      Tag tag = getTag(tagId);
      tag.setPromotional(true);
      PromotionCampaignTag relation = findCampaignTag(campaign, tag);
      if (relation == null) {
        relation = new PromotionCampaignTag();
        relation.setPromotionCampaign(campaign);
        relation.setTag(tag);
        relation.setCreatedAt(new Date());
        em.persist(relation);
      }
      relation.setActive(true);
    }
  }

  private void prepareTicketPoolStatus(PromotionCampaign campaign,
      PromotionCampaignSightEvent relation) {
    relation.setHptSightEventId(relation.getSightEvent().getHptId());
    if (relation.getTicketPoolStatus() == PromotionTicketPoolStatus.CREATED) {
      return;
    }
    if (campaign.getPromotionType() == PromotionType.TICKET) {
      relation.setTicketPoolStatus(PromotionTicketPoolStatus.NOT_CREATED);
    } else {
      relation.setTicketPoolStatus(PromotionTicketPoolStatus.NOT_REQUIRED);
    }
    relation.setUpdatedAt(new Date());
  }

  private List<String> normalizedCodes(List<String> rawCodes) {
    if (rawCodes == null) {
      throw new ConflictingException("Lista kodów jest wymagana.");
    }
    List<String> codes = rawCodes.stream()
        .filter(Objects::nonNull)
        .map(String::trim)
        .filter(code -> !code.isBlank())
        .collect(Collectors.toList());
    if (codes.isEmpty()) {
      throw new ConflictingException("Lista kodów jest pusta.");
    }
    Set<String> unique = new LinkedHashSet<>(codes);
    if (unique.size() != codes.size()) {
      throw new ConflictingException("Lista zawiera powtorzone kody.");
    }
    return new ArrayList<>(unique);
  }

  private List<String> findExistingCodeValues(List<String> codes) {
    if (codes == null || codes.isEmpty()) {
      return List.of();
    }
    return em.createQuery(
        "select code.code from PromotionCode code where code.code in :codes", String.class)
        .setParameter("codes", codes)
        .getResultList();
  }

  private boolean hasImportCodes(PromotionCodeSetupIRO setup) {
    return setup.codes != null && setup.codes.stream()
        .filter(Objects::nonNull)
        .map(String::trim)
        .anyMatch(code -> !code.isBlank());
  }

  private List<String> normalizedCodes(PromotionCodeImportIRO iro) {
    if (iro == null || iro.codes == null) {
      throw new ConflictingException("Lista kodów jest wymagana.");
    }
    List<String> codes = iro.codes.stream()
        .filter(Objects::nonNull)
        .map(String::trim)
        .filter(code -> !code.isBlank())
        .collect(Collectors.toList());
    if (codes.isEmpty()) {
      throw new ConflictingException("Lista kodów jest pusta.");
    }
    Set<String> unique = new LinkedHashSet<>(codes);
    if (unique.size() != codes.size()) {
      throw new ConflictingException("Lista zawiera powtórzone kody.");
    }
    return new ArrayList<>(unique);
  }

  private void assertCodesDoNotExist(List<String> codes) {
    Long existing = em.createQuery(
        "select count(code) from PromotionCode code where code.code in :codes", Long.class)
        .setParameter("codes", codes)
        .getSingleResult();
    if (existing > 0) {
      throw new ConflictingException("Część kodów już istnieje w systemie.");
    }
  }

  private PromotionCampaign getCampaignEntity(Long id) {
    PromotionCampaign campaign = em.find(PromotionCampaign.class, id);
    if (campaign == null) {
      throw new ResourceNotFoundException();
    }
    return campaign;
  }

  private PromotionCampaignSightEvent getCampaignSightEventEntity(Long id) {
    PromotionCampaignSightEvent relation = em.find(PromotionCampaignSightEvent.class, id);
    if (relation == null) {
      throw new ResourceNotFoundException();
    }
    return relation;
  }

  private PromotionCode getCodeEntity(Long id) {
    PromotionCode code = em.find(PromotionCode.class, id);
    if (code == null) {
      throw new ResourceNotFoundException();
    }
    return code;
  }

  private SightEvent getSightEvent(Long id) {
    SightEvent sightEvent = em.find(SightEvent.class, id);
    if (sightEvent == null) {
      throw new ResourceNotFoundException();
    }
    return sightEvent;
  }

  private Tag getTag(Long id) {
    Tag tag = em.find(Tag.class, id);
    if (tag == null) {
      throw new ResourceNotFoundException();
    }
    return tag;
  }

  private PromotionCampaignSightEvent findCampaignSightEvent(PromotionCampaign campaign,
      SightEvent sightEvent) {
    List<PromotionCampaignSightEvent> result = em.createQuery(
        "from PromotionCampaignSightEvent where promotionCampaign = :campaign "
            + "and sightEvent = :sightEvent",
        PromotionCampaignSightEvent.class)
        .setParameter("campaign", campaign)
        .setParameter("sightEvent", sightEvent)
        .setMaxResults(1)
        .getResultList();
    return result.isEmpty() ? null : result.get(0);
  }

  private PromotionCampaignTag findCampaignTag(PromotionCampaign campaign, Tag tag) {
    List<PromotionCampaignTag> result = em.createQuery(
        "from PromotionCampaignTag where promotionCampaign = :campaign and tag = :tag",
        PromotionCampaignTag.class)
        .setParameter("campaign", campaign)
        .setParameter("tag", tag)
        .setMaxResults(1)
        .getResultList();
    return result.isEmpty() ? null : result.get(0);
  }

  private PromotionCampaignHelpdeskDTO campaignDetailsDTO(PromotionCampaign campaign) {
    PromotionCampaignHelpdeskDTO dto = campaignDTO(campaign);
    dto.tagIds = em.createQuery(
        "select pct.tag.id from PromotionCampaignTag pct where pct.promotionCampaign = :campaign "
            + "and pct.active is true",
        Long.class)
        .setParameter("campaign", campaign)
        .getResultList();
    dto.sightEvents = em.createQuery(
        "from PromotionCampaignSightEvent where promotionCampaign = :campaign order by createdAt desc",
        PromotionCampaignSightEvent.class)
        .setParameter("campaign", campaign)
        .getResultStream()
        .map(this::sightEventDTO)
        .collect(Collectors.toList());
    return dto;
  }

  private PromotionCampaignHelpdeskDTO campaignDTO(PromotionCampaign campaign) {
    PromotionCampaignHelpdeskDTO dto = new PromotionCampaignHelpdeskDTO();
    dto.id = campaign.getId();
    dto.name = campaign.getName();
    dto.promotionType = campaign.getPromotionType();
    dto.status = campaign.getStatus();
    dto.scopeType = campaign.getScopeType();
    dto.validFrom = campaign.getValidFrom();
    dto.validTo = campaign.getValidTo();
    dto.globalLimit = campaign.getGlobalLimit();
    dto.codeLimit = campaign.getCodeLimit();
    dto.customerLimit = campaign.getCustomerLimit();
    dto.dailyLimit = campaign.getDailyLimit();
    dto.requiredTicketQuantity = campaign.getRequiredTicketQuantity();
    dto.grantedTicketQuantity = campaign.getGrantedTicketQuantity();
    dto.reservedRedemptionsCount = campaign.getReservedRedemptionsCount();
    dto.usedRedemptionsCount = campaign.getUsedRedemptionsCount();
    dto.discountPercent = campaign.getDiscountPercent();
    dto.discountAmountGross = campaign.getDiscountAmountGross();
    dto.createdAt = campaign.getCreatedAt();
    dto.updatedAt = campaign.getUpdatedAt();
    return dto;
  }

  private PromotionCampaignSightEventHelpdeskDTO sightEventDTO(
      PromotionCampaignSightEvent relation) {
    PromotionCampaignSightEventHelpdeskDTO dto = new PromotionCampaignSightEventHelpdeskDTO();
    dto.id = relation.getId();
    dto.promotionCampaignId = relation.getPromotionCampaign().getId();
    dto.sightEventId = relation.getSightEvent().getId();
    dto.sightEventName = relation.getSightEvent().getName();
    dto.sourceTagId = relation.getSourceTag() != null ? relation.getSourceTag().getId() : null;
    dto.active = relation.isActive();
    dto.source = relation.getSource();
    dto.hptSightEventId = relation.getHptSightEventId();
    dto.hptAtnaId = relation.getHptAtnaId();
    dto.hptTicketDefinitionId = relation.getHptTicketDefinitionId();
    dto.hptTicketPoolDefinitionId = relation.getHptTicketPoolDefinitionId();
    dto.ticketPoolStatus = relation.getTicketPoolStatus();
    dto.createdAt = relation.getCreatedAt();
    dto.updatedAt = relation.getUpdatedAt();
    return dto;
  }

  private PromotionTicketPoolTargetPreviewDTO targetPreviewDTO(PromotionCampaign campaign,
      SightEvent sightEvent) {
    PromotionCampaignSightEvent relation = findCampaignSightEvent(campaign, sightEvent);
    PromotionTicketPoolTargetPreviewDTO dto = new PromotionTicketPoolTargetPreviewDTO();
    dto.sightEventId = sightEvent.getId();
    dto.sightEventName = sightEvent.getName();
    dto.hptSightEventId = sightEvent.getHptId();
    dto.sightId = sightEvent.getSight() != null ? sightEvent.getSight().getId() : null;
    dto.sightName = sightEvent.getSight() != null ? sightEvent.getSight().getName() : null;
    dto.partnerId = sightEvent.getPartner() != null ? sightEvent.getPartner().getId() : null;
    dto.partnerName = sightEvent.getPartner() != null ? sightEvent.getPartner().getName() : null;
    dto.alreadyInCampaign = relation != null;
    dto.relationId = relation != null ? relation.getId() : null;
    dto.activeInCampaign = relation != null ? relation.isActive() : null;
    dto.hptAtnaId = relation != null ? relation.getHptAtnaId() : null;
    dto.hptTicketDefinitionId = relation != null ? relation.getHptTicketDefinitionId() : null;
    dto.hptTicketPoolDefinitionId = relation != null ? relation.getHptTicketPoolDefinitionId() : null;
    if (relation != null) {
      dto.ticketPoolStatus = relation.getTicketPoolStatus();
    } else if (campaign.getPromotionType() == PromotionType.TICKET) {
      dto.ticketPoolStatus = PromotionTicketPoolStatus.NOT_CREATED;
    } else {
      dto.ticketPoolStatus = PromotionTicketPoolStatus.NOT_REQUIRED;
    }
    return dto;
  }

  private PromotionCodeHelpdeskDTO codeDTO(PromotionCode code) {
    PromotionCodeHelpdeskDTO dto = new PromotionCodeHelpdeskDTO();
    dto.id = code.getId();
    dto.promotionCampaignId = code.getPromotionCampaign().getId();
    dto.promotionCodeBatchId = code.getPromotionCodeBatch() != null
        ? code.getPromotionCodeBatch().getId() : null;
    dto.code = code.getCode();
    dto.codeType = code.getCodeType();
    dto.status = code.getStatus();
    dto.maxRedemptions = code.getMaxRedemptions();
    dto.maxRedemptionsPerCustomer = code.getMaxRedemptionsPerCustomer();
    dto.maxRedemptionsPerDay = code.getMaxRedemptionsPerDay();
    dto.reservedRedemptionsCount = code.getReservedRedemptionsCount();
    dto.usedRedemptionsCount = code.getUsedRedemptionsCount();
    dto.reservedUntil = code.getReservedUntil();
    dto.createdAt = code.getCreatedAt();
    dto.updatedAt = code.getUpdatedAt();
    dto.disabledAt = code.getDisabledAt();
    return dto;
  }

  private PromotionCodeBatchHelpdeskDTO batchDTO(PromotionCodeBatch batch) {
    PromotionCodeBatchHelpdeskDTO dto = new PromotionCodeBatchHelpdeskDTO();
    dto.id = batch.getId();
    dto.promotionCampaignId = batch.getPromotionCampaign().getId();
    dto.source = batch.getSource();
    dto.fileName = batch.getFileName();
    dto.codesCount = batch.getCodesCount();
    dto.createdAt = batch.getCreatedAt();
    return dto;
  }

  private PromotionCodeRedemptionHelpdeskDTO redemptionDTO(PromotionCodeRedemption redemption) {
    PromotionCodeRedemptionHelpdeskDTO dto = new PromotionCodeRedemptionHelpdeskDTO();
    dto.id = redemption.getId();
    dto.promotionCampaignId = redemption.getPromotionCampaign().getId();
    dto.promotionCodeId = redemption.getPromotionCode().getId();
    dto.promotionCampaignSightEventId = redemption.getPromotionCampaignSightEvent() != null
        ? redemption.getPromotionCampaignSightEvent().getId() : null;
    dto.sightEventId = redemption.getSightEvent() != null ? redemption.getSightEvent().getId() : null;
    dto.orderId = redemption.getOrder() != null ? redemption.getOrder().getId() : null;
    dto.orderEntryId = redemption.getOrderEntry() != null ? redemption.getOrderEntry().getId() : null;
    dto.userId = redemption.getUser() != null ? redemption.getUser().getId() : null;
    dto.status = redemption.getStatus();
    dto.reservationToken = redemption.getReservationToken();
    dto.reservedAt = redemption.getReservedAt();
    dto.reservedUntil = redemption.getReservedUntil();
    dto.usedAt = redemption.getUsedAt();
    dto.releasedAt = redemption.getReleasedAt();
    dto.codeSnapshot = redemption.getCodeSnapshot();
    dto.promotionNameSnapshot = redemption.getPromotionNameSnapshot();
    dto.promotionTypeSnapshot = redemption.getPromotionTypeSnapshot();
    dto.discountPercent = redemption.getDiscountPercent();
    dto.discountAmountGross = redemption.getDiscountAmountGross();
    dto.hptTicketDefinitionId = redemption.getHptTicketDefinitionId();
    dto.hptTicketPoolDefinitionId = redemption.getHptTicketPoolDefinitionId();
    dto.createdAt = redemption.getCreatedAt();
    dto.updatedAt = redemption.getUpdatedAt();
    return dto;
  }
}
