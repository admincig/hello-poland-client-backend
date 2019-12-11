package pl.hellopoland.service.api.market;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.service.MailingListService;

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
