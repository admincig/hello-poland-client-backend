package pl.hellopoland.service;

import java.lang.System.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import sendinblue.ApiClient;
import sendinblue.ApiException;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.ContactsApi;
import sibModel.AddContactToList;
import sibModel.CreateContact;
import sibModel.CreateModel;
import sibModel.PostContactInfo;

@LocalBean
@Stateless
public class MailingListService extends ServiceSuperclass {

  private static String testServerKey =
      "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";

  // runtime.properties
  String hplMail = properties.getProperty("mail.hellopoland.biuro");

  public void lol() {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
    apiKey.setApiKey(testServerKey);
  }

  public Long createContact(String email) {


    ContactsApi apiInstance = new ContactsApi();
    CreateContact createContact = new CreateContact();

    createContact.email(email);

    try {
      CreateModel result = apiInstance.createContact(createContact);


      return result.getId(); // jeżeli jest id to znaczy, że stworzono
    } catch (ApiException e) {
      logger.log(Logger.Level.WARNING, "elo");
      e.printStackTrace();

      return null; // throw exception
    }

  }


  private static Long mailingListId = 4L;

  public String addContactToMailingList(String email) {
    ContactsApi apiInstance = new ContactsApi();

    AddContactToList contactEmails = new AddContactToList();

    contactEmails.addEmailsItem(email);

    try {
      PostContactInfo result = apiInstance.addContactToList(mailingListId, contactEmails);


      return result.getContacts().getSuccess().get(0); // jeżeli wysłano jeden email
    } catch (ApiException e) {
      logger.log(Logger.Level.WARNING, "elo");
      e.printStackTrace();

      return null; // throw exception
    }
  }
}
