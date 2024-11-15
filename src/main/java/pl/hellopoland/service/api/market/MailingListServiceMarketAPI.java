package pl.hellopoland.service.api.market;

import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.service.MailingListService;

import jakarta.annotation.security.PermitAll;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

@Stateless
public class MailingListServiceMarketAPI {

  private static final Logger logger =
      System.getLogger(MailingListServiceMarketAPI.class.getName());

  @Inject
  MailingListService service;

  @PermitAll
  public String addToMailingList(String email) {
    service.getOrCreateContact(email);
    try {
      service.addContactToList(email);
      service.sendConfirmationMail(email);
    } catch (ConflictingException e) {
      logger.log(Level.WARNING,
          "email " + email + " has not been added to mailing list: " + e.getMessage());
    }
    return email;
  }
}
