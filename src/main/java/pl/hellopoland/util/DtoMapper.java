package pl.hellopoland.util;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import pl.hellopoland.bo.Address;
import pl.hellopoland.bo.Agreement;
import pl.hellopoland.bo.Category;
import pl.hellopoland.bo.ContactPerson;
import pl.hellopoland.bo.FileDescriptor;
import pl.hellopoland.bo.ImageCollector;
import pl.hellopoland.bo.Location;
import pl.hellopoland.bo.OpeningHours;
import pl.hellopoland.bo.OrderDetails;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.bo.PartnerRepresentative;
import pl.hellopoland.bo.PassageCart;
import pl.hellopoland.bo.PassageCartEntry;
import pl.hellopoland.bo.Sight;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.SightEventCategory;
import pl.hellopoland.bo.SightEventTag;
import pl.hellopoland.bo.Tag;
import pl.hellopoland.bo.TicketDefinition;
import pl.hellopoland.bo.User;
import pl.hellopoland.bo.UserRole;
import pl.hellopoland.dto.AgreementDTO;
import pl.hellopoland.dto.CategoryDTO;
import pl.hellopoland.dto.ContactPersonDTO;
import pl.hellopoland.dto.FileDescriptorDTO;
import pl.hellopoland.dto.ImageDTO;
import pl.hellopoland.dto.LocationDTO;
import pl.hellopoland.dto.MarketPartnerDTO;
import pl.hellopoland.dto.OpeningHoursDTO;
import pl.hellopoland.dto.P24PassageCartDTO;
import pl.hellopoland.dto.P24PassageCartEntryDTO;
import pl.hellopoland.dto.P24PassageTransactionParamsDTO;
import pl.hellopoland.dto.PartnerDTO;
import pl.hellopoland.dto.PartnerRepresentativeDTO;
import pl.hellopoland.dto.RoleDTO;
import pl.hellopoland.dto.SightDTO;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.dto.TagDTO;
import pl.hellopoland.dto.TicketDefinitionDTO;
import pl.hellopoland.dto.UserDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.soap.p24.enums.BusinessType;

public class DtoMapper {

  private final static Logger logger = System.getLogger(DtoMapper.class.getName());

  public static void copy(SightDTO source, Sight target) {
    target.setName(source.name);
    target.setLead(source.lead);
    target.setDescription(source.description);
    target.setEmail(source.email);
    target.setPhone(source.phone);
    target.setScore(source.score);
    if (source.defaultLanguage != null) {
      target
          .setDefaultLanguage(LanguageVersion.getForCreateAndUpdateEntity(source.defaultLanguage));
    }
    if (source.availableLanguageVersions != null && !source.availableLanguageVersions.isEmpty()) {
      target.setAvailableLanguageVersions(source.availableLanguageVersions.stream()
          .map(ver -> LanguageVersion.getForCreateAndUpdateEntity(ver))
          .collect(Collectors.toSet()));
    }
    if (source.blocked != null) {
      target.setBlocked(source.blocked);
    }
    if (source.published != null) {
      target.setPublished(source.published);
    }
    copyLocation(source.location, target);
  }

  public static OpeningHours copy(OpeningHoursDTO source, OpeningHours target) {
    target.setCloseTime(source.closeTime);
    target.setDay(source.day);
    target.setOpenTime(source.openTime);
    return target;
  }

  public static Agreement copy(AgreementDTO source, Agreement target) {
    if (source.linkUrl != null && !source.linkUrl.isEmpty()) {
      target.setLinkUrl(source.linkUrl);
    }
    if (source.text != null && !source.text.isEmpty()) {
      target.setText(source.text);
    }
    if (source.obligatory != null) {
      target.setObligatory(source.obligatory);
    }
    return target;
  }

  public static SightDTO getDTO(Sight bo) {
    SightDTO dto = new SightDTO();
    dto.id = bo.getId();
    dto.name = bo.getName();
    dto.lead = bo.getLead();
    dto.description = bo.getDescription();
    dto.mainImage = bo.getMainImage() != null ? getDTO(bo.getMainImage()) : null;
    dto.location = ofNullable(bo.getLocation()).map(DtoMapper::getDTO).orElse(null);
    dto.email = bo.getEmail();
    dto.phone = bo.getPhone();
    dto.score = bo.getScore();
    dto.blocked = bo.isBlocked();
    dto.published = bo.isPublished();
    dto.defaultLanguage = bo.getDefaultLanguage().getLanuage();
    dto.language = bo.getCurrentLanguage() == null ? dto.defaultLanguage
        : bo.getCurrentLanguage().getLanuage();
    return dto;
  }

  public static SightDTO getFullDTO(Sight bo) {
    SightDTO dto = getDTO(bo);
    dto.availableLanguageVersions = bo.getAvailableLanguageVersions().stream()
        .map(lang -> lang.getLanuage()).collect(Collectors.toSet());
    if (bo.getSightEvents() != null) {
      dto.sightEvents = bo.getSightEvents().stream().map(DtoMapper::getFullDTO).collect(toList());
    }
    if (bo.getImages() != null && !bo.getImages().isEmpty()) {
      dto.images = bo.getImages().stream().map(DtoMapper::getDTO).collect(toList());
    }
    if (bo.getOpeningHours() != null && !bo.getOpeningHours().isEmpty()) {
      dto.openingHours = bo.getOpeningHours().stream().map(DtoMapper::getDTO).collect(toList());
    }
    if (bo.getAgreements() != null && !bo.getAgreements().isEmpty()) {
      dto.agreements = bo.getAgreements().stream().map(DtoMapper::getDTO).collect(toList());
    }
    if (bo.getCategories() != null && !bo.getCategories().isEmpty()) {
      dto.categories = bo.getCategories().stream().map(DtoMapper::getDTO).collect(toSet());
    }
    return dto;
  }

  public static SightEventDTO getDTO(SightEvent bo) {
    SightEventDTO dto = new SightEventDTO();
    dto.id = bo.getId();
    dto.name = bo.getName();
    dto.lead = bo.getLead();
    dto.description = bo.getDescription();
    dto.mainImage = bo.getMainImage() != null ? getDTO(bo.getMainImage()) : null;
    dto.email = bo.getEmail();
    dto.phone = bo.getPhone();
    dto.duration = bo.getDuration();
    dto.location = ofNullable(bo.getLocation()).map(DtoMapper::getDTO).orElse(null);
    dto.generalAdmission = bo.getGeneralAdmission();
    dto.score = bo.getScore();
    dto.sightId = bo.getSight() != null ? bo.getSight().getId() : null;
    dto.blocked = bo.isBlocked();
    dto.published = bo.isPublished();
    dto.partnerAffiliateCode = bo.getPartner().getAffiliateCode();
    dto.defaultLanguage = bo.getDefaultLanguage().getLanuage();
    dto.promotion = bo.getPromotion();
    dto.promoted = dto.promotion != null;
    dto.sightId = bo.getSight().getId();
    dto.sightName = bo.getSight().getName();
    dto.partnerId = bo.getPartner().getId();
    dto.partnerName = bo.getPartner().getName();
    dto.minPrice = bo.getMinPrice();
    dto.language = bo.getCurrentLanguage() == null ? dto.defaultLanguage
        : bo.getCurrentLanguage().getLanuage();
    return dto;
  }

  public static SightEventDTO getFullDTO(SightEvent bo) {
    SightEventDTO dto = getDTO(bo);
    dto.availableLanguageVersions = bo.getAvailableLanguageVersions().stream()
        .map(lang -> lang.getLanuage()).collect(Collectors.toSet());
    if (bo.getImages() != null && !bo.getImages().isEmpty()) {
      dto.images = bo.getImages().stream().map(DtoMapper::getDTO).collect(toList());
    }
    // if (bo.getTickets() != null && !bo.getTickets().isEmpty()) {
    // dto.ticketDefinitions = bo.getTickets().stream().map(DtoMapper::getDTO).collect(toList());
    // }
    if (bo.getOpeningHours() != null && !bo.getOpeningHours().isEmpty()) {
      dto.openingHours = bo.getOpeningHours().stream().map(DtoMapper::getDTO).collect(toList());
    }
    if (bo.getAgreements() != null && !bo.getAgreements().isEmpty()) {
      dto.agreements = bo.getAgreements().stream().map(DtoMapper::getDTO).collect(toList());
    }
    if (bo.getCategories() != null && !bo.getCategories().isEmpty()) {
      dto.categories = bo.getCategories().stream().map(SightEventCategory::getCategory)
          .map(DtoMapper::getDTO).collect(toSet());
    }
    if (bo.getTags() != null && !bo.getTags().isEmpty()) {
      dto.tags = bo.getTags().stream().map(SightEventTag::getTag)
          .map(DtoMapper::getDTO).collect(toSet());
    }
    dto.pdfAttachment = bo.getPdfAttachment() != null ? getFullDTO(bo.getPdfAttachment()) : null;
    return dto;
  }

  public static FileDescriptorDTO getDTO(FileDescriptor bo) {
    var dto = new FileDescriptorDTO();
    dto.id = bo.getId();
    dto.created = bo.getCreated();
    dto.type = bo.getMimeType().toString();
    dto.name = bo.getFileName();
    return dto;
  }

  public static FileDescriptorDTO getFullDTO(FileDescriptor bo) {
    var dto = getDTO(bo);
    dto.path = bo.getPath();
    dto.downloadUrl = bo.getDownloadUrl();
    return dto;
  }

  private static OpeningHoursDTO getDTO(OpeningHours bo) {
    var dto = new OpeningHoursDTO();
    dto.sight = ofNullable(bo.getSight()).map(DtoMapper::getDTO).orElse(null);
    dto.sightEvent = ofNullable(bo.getSightEvent()).map(DtoMapper::getDTO).orElse(null);
    dto.day = bo.getDay();
    dto.openTime = bo.getOpenTime();
    dto.closeTime = bo.getCloseTime();
    return dto;
  }

  public static LocationDTO getDTO(Location bo) {
    LocationDTO dto = new LocationDTO();
    dto.latitude = bo.getLatitude();
    dto.longitude = bo.getLongitude();
    dto.street = bo.getStreet();
    dto.zipCode = bo.getZipCode();
    dto.city = bo.getCity();
    dto.country = bo.getCountry();
    dto.directions = bo.getDirections();
    return dto;
  }

  private static LocationDTO getDTO(Address address) {
    if (address != null) {
      LocationDTO dto = new LocationDTO();
      dto.street = address.getStreet();
      dto.zipCode = address.getPostCode();
      dto.city = address.getCity();
      dto.country = address.getCountry();
      dto.directions = address.getDirections();
      return dto;
    }
    return null;
  }

  public static TicketDefinitionDTO getDTO(TicketDefinition bo) {
    TicketDefinitionDTO dto = new TicketDefinitionDTO();
    dto.id = bo.getId();
    dto.name = bo.getName();
    dto.price = bo.getPrice();
    // dto.sightEventId = bo.getSightEvent().getId();
    dto.availableTicketsNumber = bo.getAvailableTicketsNumber();
    return dto;
  }

  public static ImageDTO getDTO(ImageCollector bo) {
    if (bo == null) {
      return null;
    }
    ImageDTO dto = new ImageDTO();
    dto.original = bo.getOrginal().getDownloadUrl();
    if (bo.getOrginalWebp() != null) {
      dto.originalWebp = bo.getOrginalWebp().getDownloadUrl();
    }
    dto.fhd = bo.getFhd().getDownloadUrl();
    if (bo.getFhdWebp() != null) {
      dto.fhdWebp = bo.getFhdWebp().getDownloadUrl();
    }
    dto.fourK = bo.getFourK().getDownloadUrl();
    if (bo.getFourKWebp() != null) {
      dto.fourKWebp = bo.getFourKWebp().getDownloadUrl();
    }
    dto.hd = bo.getHd().getDownloadUrl();
    if (bo.getHdWebp() != null) {
      dto.hdWebp = bo.getHdWebp().getDownloadUrl();
    }
    dto.qvg = bo.getQvga().getDownloadUrl();
    if (bo.getQvgaWebp() != null) {
      dto.qvgWebp = bo.getQvgaWebp().getDownloadUrl();
    }
    dto.sxga = bo.getSxga().getDownloadUrl();
    if (bo.getSxgaWebp() != null) {
      dto.sxgaWebp = bo.getSxgaWebp().getDownloadUrl();
    }
    dto.vga = bo.getVga().getDownloadUrl();
    if (bo.getVgaWebp() != null) {
      dto.vgaWebp = bo.getVgaWebp().getDownloadUrl();
    }
    dto.xga = bo.getXga().getDownloadUrl();
    if (bo.getXgaWebp() != null) {
      dto.xgaWebp = bo.getXgaWebp().getDownloadUrl();
    }
    return dto;
  }

  private static void copyLocation(LocationDTO source, Located target) {
    Location location = target.getLocation();
    if (source != null) {
      if (location == null) {
        location = new Location();
      }
      location.setLatitude(source.latitude);
      location.setLongitude(source.longitude);
      location.setStreet(source.street);
      location.setZipCode(source.zipCode);
      location.setCity(source.city);
      location.setCountry(source.country);
      location.setDirections(source.directions);
      target.setLocation(location);
    } else {
      target.setLocation(null);
    }
  }

  public static SightEventDTO getGAEventDTO(Sight bo) {
    SightEventDTO dto = new SightEventDTO();
    dto.sightId = bo.getId();
    dto.name = bo.getName();
    dto.description = bo.getDescription();
    dto.mainImage = getDTO(bo.getMainImage());
    dto.email = bo.getEmail();
    dto.phone = bo.getPhone();
    dto.lead = bo.getLead();
    dto.location = ofNullable(bo.getLocation()).map(DtoMapper::getDTO).orElse(null);
    dto.generalAdmission = true;
    return dto;
  }

  public static void copy(SightEventDTO source, SightEvent target) {
    target.setName(source.name);
    target.setLead(source.lead);
    target.setDescription(source.description);
    target.setEmail(source.email);
    target.setPhone(source.phone);
    target.setGeneralAdmission(source.generalAdmission);
    target.setHptId(source.id);
    target.setScore(source.score);
    if (source.defaultLanguage != null) {
      target
          .setDefaultLanguage(LanguageVersion.getForCreateAndUpdateEntity(source.defaultLanguage));
    }
    if (source.availableLanguageVersions != null && !source.availableLanguageVersions.isEmpty()) {
      target.setAvailableLanguageVersions(source.availableLanguageVersions.stream()
          .map(ver -> LanguageVersion.getForCreateAndUpdateEntity(ver))
          .collect(Collectors.toSet()));
    }
    if (source.blocked != null) {
      target.setBlocked(source.blocked);
    }
    if (source.published != null) {
      target.setPublished(source.published);
    }
    copyLocation(source.location, target);
  }

  public static void copy(TicketDefinitionDTO source, TicketDefinition target) {
    target.setExternalId(source.id);
    target.setName(source.name);
    target.setPoolId(source.poolId);
    target.setPrice(source.price);
    target.setAvailableTicketsNumber(source.availableTicketsNumber);
  }


  public static P24PassageCartDTO getDTO(PassageCart p24PassageCart) {
    var dto = new P24PassageCartDTO();
    dto.isSandbox = p24PassageCart.isSandbox();
    dto.transactionParams = getParams(p24PassageCart);
    return dto;
  }

  private static P24PassageTransactionParamsDTO getParams(PassageCart p24PassageCart) {
    var o = p24PassageCart.getOrder();
    OrderDetails od = o.getDetails();

    var dto = new P24PassageTransactionParamsDTO();
    dto.encoding = "UTF-8";
    dto.amount = p24PassageCart.getAmount();
    dto.country = p24PassageCart.getCountry();
    dto.currency = p24PassageCart.getCurrency();
    dto.description = p24PassageCart.getDescription();
    dto.language = p24PassageCart.getLanguage();
    dto.merchantId = p24PassageCart.getMerchantId();
    dto.sign = p24PassageCart.getSign();
    dto.urlStatus = p24PassageCart.getUrlStatus();
    dto.address = od.getStreet() != null ? od.getStreet() : "";
    dto.city = od.getCity() != null ? od.getCity() : "";
    dto.client = (od.getFirstName() == null && od.getLastName() == null) ? ""
        : od.getFirstName() + " " + od.getLastName();
    dto.email = od.getEmail() != null ? od.getEmail() : "";
    dto.phone = od.getPhone() != null ? od.getPhone() : "";
    dto.sessionId = o.getHash();
    dto.zip = od.getZipCode() != null ? od.getZipCode() : "";

    var passageCartEntries = new ArrayList<P24PassageCartEntryDTO>();
    p24PassageCart.getCartEntries().forEach(ce -> {
      passageCartEntries.add(getP24PassageCartEntryDTO(ce));
    });
    passageCartEntries.add(getP24PassageCartEntryDTO(p24PassageCart.getHpCommissionEntry()));

    dto.passageCart = passageCartEntries;
    return dto;
  }

  private static P24PassageCartEntryDTO getP24PassageCartEntryDTO(
      PassageCartEntry passageCartEntry) {
    var dto = new P24PassageCartEntryDTO();
    dto.description = passageCartEntry.getDescription();
    dto.name = passageCartEntry.getName();
    dto.number = passageCartEntry.getNumber();
    dto.price = passageCartEntry.getPrice();
    dto.quantity = passageCartEntry.getQuantity();
    dto.targetAmount = passageCartEntry.getTargetAmount();
    dto.targetPosId = passageCartEntry.getTargetPosId();
    return dto;
  }

  public static AgreementDTO getDTO(Agreement bo) {
    var dto = new AgreementDTO();
    dto.id = bo.getId();
    dto.linkUrl = bo.getLinkUrl();
    dto.obligatory = bo.isObligatory();
    dto.text = bo.getText();
    return dto;
  }

  public static RoleDTO getDTO(UserRole bo) {
    return RoleDTO.valueOf(bo.getRole().name());
  }

  public static UserDTO getDTO(User bo) {
    var dto = new UserDTO();
    dto.id = bo.getId();
    dto.name = bo.getName();
    dto.email = bo.getEmail();
    dto.roles = Optional.ofNullable(bo.getRoles()).orElse(Collections.emptyList()).stream()
        .map(DtoMapper::getDTO).collect(Collectors.toSet());
    return dto;
  }

  public static PartnerDTO getDTO(Partner bo) {
    var dto = new PartnerDTO();
    dto.id = bo.getId();
    dto.name = bo.getName();
    dto.p24MerchantId = bo.getP24Id();
    dto.commission = bo.getCommission();
    dto.email = bo.getEmail();
    dto.affiliateCode = bo.getAffiliateCode();
    dto.bankAccount = bo.getBankAccount();
    dto.businessType = bo.getBusinessType() != null ? bo.getBusinessType().getValue() : null;
    dto.invoiceEmail = bo.getInvoiceEmail();
    dto.krs = bo.getKrs();
    dto.taxNumber = bo.getTaxNumber();
    dto.socialNumber = String.valueOf(bo.getSocialNumber());
    dto.phone = bo.getPhone();
    dto.regon = bo.getRegon();
    dto.servicesDescription = bo.getServicesDescription();
    dto.shopUrl = bo.getShopUrl();
    dto.description = bo.getDescription();
    dto.defaultLanguage = bo.getDefaultLanguage().getLanuage();
    dto.language = bo.getCurrentLanguage() == null ? dto.defaultLanguage
        : bo.getCurrentLanguage().getLanuage();
    dto.blocked = bo.isBlocked();
    dto.mainImage = DtoMapper.getDTO(bo.getMainImage());
    return dto;
  }

  public static MarketPartnerDTO getMarketDTO(Partner bo) {
    var dto = new MarketPartnerDTO();
    dto.id = bo.getId();
    dto.name = bo.getName();
    dto.mainImage = getDTO(bo.getMainImage());
    return dto;
  }

  public static MarketPartnerDTO getFullMarketPartnerDTO(Partner bo) {
    var dto = getMarketDTO(bo);
    dto.location = getDTO(bo.getCorrespondenceAddress());
    dto.description = bo.getDescription();
    if (bo.getCategories() != null) {
      dto.categories =
          bo.getCategories().stream().map(DtoMapper::getDTO).collect(Collectors.toList());
    }
    dto.cities = bo.getCities();
    dto.email = bo.getEmail();
    dto.phone = bo.getPhone();
    return dto;
  }

  public static PartnerDTO getFullDTO(Partner bo) {
    var dto = getDTO(bo);
    dto.users = Optional.ofNullable(bo.getUsers()).orElse(Collections.emptyList()).stream()
        .map(DtoMapper::getDTO).collect(Collectors.toList());
    dto.location = getDTO(bo.getAddress());
    dto.correspondenceAddress = getDTO(bo.getCorrespondenceAddress());
    dto.technicalContact =
        bo.getTechnicalContact() != null ? getDTO(bo.getTechnicalContact()) : null;
    dto.contactPerson = bo.getContactPerson() != null ? getDTO(bo.getContactPerson()) : null;
    dto.representatives =
        Optional.ofNullable(bo.getRepresentatives()).orElse(Collections.emptyList()).stream()
            .map(DtoMapper::getDTO).collect(Collectors.toList());
    dto.availableLanguageVersions = bo.getAvailableLanguageVersions().stream()
        .map(lang -> lang.getLanuage()).collect(Collectors.toSet());
    return dto;
  }

  private static ContactPersonDTO getDTO(ContactPerson bo) {
    var dto = new ContactPersonDTO();
    dto.name = bo.getName();
    dto.email = bo.getEmail();
    dto.phone = bo.getPhone();
    return dto;
  }

  private static PartnerRepresentativeDTO getDTO(PartnerRepresentative bo) {
    var dto = new PartnerRepresentativeDTO();
    dto.name = bo.getName();
    dto.socialNumber = String.valueOf(bo.getSocialNumber());
    return dto;
  }

  public static CategoryDTO getDTO(Category bo) {
    var dto = new CategoryDTO();
    dto.id = bo.getId();
    dto.label = bo.getLabel();
    dto.iconUrl = bo.getIconUrl();
    dto.restricted = bo.isRestricted();
    dto.assignedItemsCount = bo.getAssignedItemsCount();
    dto.recommended = bo.isRecommended();
    dto.defaultLanguage = bo.getDefaultLanguage().getLanuage();
    dto.language = bo.getCurrentLanguage() == null ? dto.defaultLanguage
        : bo.getCurrentLanguage().getLanuage();
    return dto;
  }

  public static CategoryDTO getFullDTO(Category bo) {
    var dto = getDTO(bo);
    dto.availableLanguageVersions = bo.getAvailableLanguageVersions().stream()
        .map(lv -> lv.getLanuage()).collect(Collectors.toSet());
    return dto;
  }

  public static TagDTO getDTO(Tag bo) {
    var dto = new TagDTO();
    dto.id = bo.getId();
    dto.label = bo.getLabel();
    dto.iconUrl = bo.getIconUrl();
    dto.restricted = bo.isRestricted();
    dto.assignedItemsCount = bo.getAssignedItemsCount();
    dto.recommended = bo.isRecommended();
    dto.defaultLanguage = bo.getDefaultLanguage().getLanuage();
    dto.language = bo.getCurrentLanguage() == null ? dto.defaultLanguage
        : bo.getCurrentLanguage().getLanuage();
    return dto;
  }

  public static TagDTO getFullDTO(Tag bo) {
    var dto = getDTO(bo);
    dto.availableLanguageVersions = bo.getAvailableLanguageVersions().stream()
        .map(lv -> lv.getLanuage()).collect(Collectors.toSet());
    return dto;
  }

  public static void copy(MarketPartnerDTO dto, Partner bo) {
    bo.setDescription(dto.description);
  }

  public static void copy(PartnerDTO dto, Partner bo) {
    // bo.setAddress(address);
    bo.setAffiliateCode(dto.affiliateCode);
    bo.setBankAccount(dto.bankAccount);
    bo.setBlocked(dto.blocked);
    bo.setBusinessType(BusinessType.getBusinessType(dto.businessType));
    bo.setCommission(dto.commission);
    // bo.setContactPerson(contactPerson);
    // bo.setCorrespondenceAddress(correspondenceAddress);
    bo.getAddress().setCity(dto.location.city);
    bo.getAddress().setCountry(dto.location.country);
    bo.getAddress().setPostCode(dto.location.zipCode);
    bo.getAddress().setStreet(dto.location.street);
    bo.getAddress().setDirections(dto.location.directions);
    bo.setDescription(dto.description);
    bo.setEmail(dto.email);
    bo.setInvoiceEmail(dto.invoiceEmail);
    bo.setKrs(dto.krs);
    bo.setName(dto.name);
    bo.setP24Id(dto.p24MerchantId);
    bo.setPhone(dto.phone);
    bo.setRegon(dto.regon);
    bo.setServicesDescription(dto.servicesDescription);
    bo.setShopUrl(dto.shopUrl);
    try {
      bo.setSocialNumber(Long.valueOf(dto.socialNumber));
    } catch (Exception e) {
      logger.log(Level.DEBUG, "social number cant be parsed");
    }
    bo.setTaxNumber(dto.taxNumber);
    // bo.setTechnicalContact(technicalContact);
  }

}
