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
    service.createContact(email);
    String contactAddedToList = service.addContactToMailingList(email);
    return contactAddedToList;

    // service.add to contactList();
    // return contact
  }
}
