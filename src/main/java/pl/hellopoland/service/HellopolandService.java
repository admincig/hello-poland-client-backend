package pl.hellopoland.service;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import pl.hellopoland.bo.*;
import pl.hellopoland.bo.UserRole.Role;
import pl.hellopoland.dto.PartnerDTO;
import pl.hellopoland.dto.RoleDTO;
import pl.hellopoland.dto.UserAuthDTO;
import pl.hellopoland.dto.UserDTO;
import pl.hellopoland.enums.BusinessType;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.exception.email.EmailSendingRollbackException;
import pl.hellopoland.soap.p24.object.MerchantRegisterRequest;
import pl.hellopoland.util.HelloTicket;
import pl.hellopoland.util.soap.p24.MerchantRegisterValidator;

import java.lang.System.Logger.Level;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@LocalBean
@Stateless
public class HellopolandService extends ServiceSuperclass {
  @Inject
  private UserService userService;
  @Inject
  private EmailService emailService;
  @Inject
  private TranslationService translationService;

  final Set<UserRole.Role> excludedRoles = Set.of(UserRole.Role.ROOT, UserRole.Role.ADMIN,
      UserRole.Role.PARTNER, UserRole.Role.SALESMAN);

  public void resetPartnerCredentials(Long id, String email) {
    Partner partner = em.find(Partner.class, id);
    partner.setEmail(email);
    Portal hpt = getPortal("Hello Ticket Cloud");
    HelloTicket ht = new HelloTicket(hpt.getUrl());

    String newPassword = RandomStringUtils.randomAlphanumeric(10);
    UserAuthDTO form = new UserAuthDTO();
    form.login = email;
    form.password = newPassword;
    form.passwordConfirmation = newPassword;
    ht.changePartnerCredentials(form, partner.getHptToken());

    User user = userService.findByPartnerAndRole(partner, Role.PARTNER);
    user.changePassword(newPassword);
    user.setEmail(email);

    var emailPassword = new HashMap<String, String>();
    emailPassword.put(email, newPassword);
    try {
      emailService.sendEmail(new Email(email, "Reset konta w Hello Poland.", "Twój login to " + email + ", hasło to " + newPassword));
    } catch (Exception e) {
      logger.log(System.Logger.Level.ERROR, e.getLocalizedMessage());
    }
  }

  public Partner addPartner(PartnerDTO partner) {
    if (StringUtils.isBlank(partner.email)) {
      throw new ConflictingException("The email cannot be blank.");
    }
    if (StringUtils.isBlank(partner.name)) {
      throw new ConflictingException("The partner name cannot be blank.");
    }
//    if (partner.commission == null) {
//      throw new ConflictingException("The partner commission cannot be blank.");
//    }
    if (partner.commission != null && (partner.commission.compareTo(BigDecimal.ZERO) < 0
        || partner.commission.compareTo(new BigDecimal("100")) > 0)) {
      throw new ConflictingException("The partner commission is out of range: 0 - 100.");
    }

    var merchant = new MerchantRegisterRequest(partner);
    MerchantRegisterValidator.validate(merchant);
    Partner partnerBO = getPartnerFromMerchantRegisterRequest(merchant);

    // 2. creating a partner and the user in hpl:
    partnerBO.setCreated(LocalDateTime.now());
    partnerBO.setCommission(partner.commission);
    partnerBO.setHptToken("temporaryToken");
    if (BooleanUtils.isTrue(partner.affiliation)) {
      partnerBO.setAffiliateCode(RandomStringUtils.randomAlphanumeric(8));
    }
    em.persist(partnerBO.getContactPerson());
    em.persist(partnerBO.getTechnicalContact());
    em.persist(partnerBO);
    String password = RandomStringUtils.randomAlphanumeric(10);
    try {
      Optional<User> user = userService.findUndeletedByEmailWithNullPartner(partner.email);
      user.ifPresentOrElse(us -> {
        us.changePassword(password);
        userService.attachToPartner(us, partnerBO);
      }, () -> userService.create(partner.email, password, null, null, partnerBO,
          UserRole.Role.PARTNER, UserRole.Role.USHER));
      em.flush();
    } catch (Exception e) {
      var exc = e.getCause();
      if (exc instanceof jakarta.validation.ConstraintViolationException) {
        var errMsg = new StringBuilder();
        ((jakarta.validation.ConstraintViolationException) exc).getConstraintViolations().forEach(
            cv -> errMsg.append(cv.getPropertyPath() + " ").append(cv.getMessage() + ", "));
        logger.log(Level.ERROR, "Błąd podczas dodawania partnera; " + errMsg.toString());
        throw new ConflictingException("Błąd podczas dodawania partnera; " + errMsg.toString());
      }

      var exc2 = e.getCause().getCause();
      if (exc2 instanceof ConstraintViolationException) {
        String errMsg = ((ConstraintViolationException) exc2).getSQLException().getMessage();
        logger.log(Level.ERROR, "Błąd podczas dodawania partnera; " + errMsg);
        throw new ConflictingException(
            "Błąd podczas dodawania partnera; " + errMsg.substring(errMsg.lastIndexOf(": ") + 1));
      }
      throw new ConflictingException("Błąd podczas dodawania partnera");
    }

    partner.password = password;
    var emailPassword = new HashMap<String, String>();
    emailPassword.put(partner.email, password);

    // 3. creating users (excluded ushers) of the partner in hpl:
    var usersDTOs = partner.users;
    if (usersDTOs != null && !usersDTOs.isEmpty()) {
      for (UserDTO userDTO : usersDTOs) {
        if (StringUtils.isBlank(userDTO.email)) {
          throw new ConflictingException("The email cannot be blank.");
        }
        if (userDTO.roles == null || !areRolesSupported(userDTO.roles)) {
          throw new ConflictingException("Roles are blank or some role is unsupported.");
        }
        Role[] userRoles = getFilteredRolesFromDTO(userDTO.roles);
        if (userRoles.length > 0) {
          String pass = RandomStringUtils.randomAlphanumeric(10);
          User userBO = userService.create(userDTO.email, pass, userDTO.name, null, partnerBO,
              userRoles);
          emailPassword.put(userDTO.email, pass);
          partnerBO.addUser(userBO);
        }
      }
    }

    // 4. creating a partner in hpt:
    Portal hpt = getPortal("Hello Ticket Cloud");
    var ht = new HelloTicket(hpt.getUrl());
    var hptToken = getLoggedUser().getHptToken();
    try {
      var hptPartner = ht.addPartner(partner, hptToken);
      partnerBO.setHptToken(hptPartner.token);
      partnerBO.setHptId(hptPartner.id);
    } catch (Exception e) {
      throw new ConflictingException("Nie udało się stworzyć partnera w zewnętrznym systemie", e);
    }

    // 5. sending emails to users (with theirs login and password):
    emailPassword.forEach((key, value) -> {
      try {
        emailService.sendEmail(new Email(key, "Nowe konto w Hello Poland.", "Twój login to " + key + ", hasło to " + value));
      } catch (Exception e) {
        logger.log(System.Logger.Level.ERROR, e.getLocalizedMessage());
        ht.removePartner(partner.email, hptToken);
        throw new EmailSendingRollbackException("Błąd podczas wysyłania maila do: " + key);
      }
    });


    // 6. create first translation
    translationService.createEntityLanguageVersion(partnerBO, partner, LanguageVersion.PL_PL);
    translationService.createEntityLanguageVersion(partnerBO.getAddress(), partner.location,
        LanguageVersion.PL_PL);

    return partnerBO;
  }

  private Partner getPartnerFromMerchantRegisterRequest(MerchantRegisterRequest merchant) {
    var partnerBO = new Partner();
    partnerBO.setBankAccount(merchant.bank_account);
    partnerBO.setName(merchant.name);
    partnerBO.setEmail(merchant.email);
    partnerBO.setInvoiceEmail(merchant.invoice_email);
    partnerBO.setKrs(merchant.krs);
    partnerBO.setTaxNumber(merchant.nip);
    partnerBO.setSocialNumber(
        StringUtils.isNotBlank(merchant.pesel) ? Long.valueOf(merchant.pesel) : null);
    partnerBO.setPhone(merchant.phone_number);
    partnerBO.setRegon(merchant.regon);
    partnerBO.setServicesDescription(merchant.services_description);
    partnerBO.setShopUrl(merchant.shop_url);
    partnerBO.setBusinessType(BusinessType.getBusinessType(merchant.business_type));
    var address = new Address();
    address.setCountry(merchant.address.country);
    address.setCity(merchant.address.city);
    address.setPostCode(merchant.address.post_code);
    address.setStreet(merchant.address.street);
    partnerBO.setAddress(address);
    var correspondenceAddress = new Address();
    correspondenceAddress.setCountry(merchant.correspondence_address.country);
    correspondenceAddress.setCity(merchant.correspondence_address.city);
    correspondenceAddress.setPostCode(merchant.correspondence_address.post_code);
    correspondenceAddress.setStreet(merchant.correspondence_address.street);
    partnerBO.setCorrespondenceAddress(correspondenceAddress);
    var contactPerson = new ContactPerson();
    contactPerson.setEmail(merchant.contact_person.email);
    contactPerson.setName(merchant.contact_person.name);
    contactPerson.setPhone(merchant.contact_person.phone_number);
    partnerBO.setContactPerson(contactPerson);
    var technicalContact = new ContactPerson();
    technicalContact.setEmail(merchant.technical_contact.email);
    technicalContact.setName(merchant.technical_contact.name);
    technicalContact.setPhone(merchant.technical_contact.phone_number);
    partnerBO.setTechnicalContact(technicalContact);
    if (merchant.representatives != null) {
      List<PartnerRepresentative> representatives =
          Arrays.asList(merchant.representatives).stream().map(r -> {
            var rep = new PartnerRepresentative();
            rep.setName(r.name);
            rep.setSocialNumber(StringUtils.isNotBlank(r.pesel) ? Long.valueOf(r.pesel) : null);
            return rep;
          }).collect(Collectors.toList());
      partnerBO.setRepresentatives(representatives);
    }
    return partnerBO;
  }

  private boolean areRolesSupported(Set<RoleDTO> roles) {
    var supported = new HashSet<Role>(Arrays.asList(UserRole.Role.values()));
    supported.removeAll(excludedRoles);
    try {
      return supported.containsAll(
          roles.stream().map(r -> UserRole.Role.valueOf(r.name())).collect(Collectors.toSet()));
    } catch (Exception e) {
      return false;
    }
  }

  private Role[] getFilteredRolesFromDTO(Set<RoleDTO> roles) {
    Stream<UserRole.Role> stream = roles.stream().map(r -> UserRole.Role.valueOf(r.name()))
        .filter(r -> !excludedRoles.contains(r) && !r.equals(UserRole.Role.USHER));
    return stream.toArray(UserRole.Role[]::new);
  }

}
