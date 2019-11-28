package pl.hellopoland.service.api.market;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.service.MailingListService;

@Stateless
public class MailingListServiceMarketAPI {

  @Inject
  MailingListService service;

  @PermitAll
  public String addToMailingList(String email) {
    service.getOrCreateContact(email);
    String contactAddedToList = service.addContactToList(email);
    return contactAddedToList;
  }
}
