package pl.hellopoland.service;

import java.lang.System.Logger;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import pl.hellopoland.exception.conflict.ConflictingException;
import sendinblue.ApiClient;
import sendinblue.ApiException;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.ContactsApi;
import sibApi.SmtpApi;
import sibModel.AddContactToList;
import sibModel.CreateContact;
import sibModel.CreateModel;
import sibModel.GetExtendedContactDetails;
import sibModel.PostContactInfo;
import sibModel.SendSmtpEmail;
import sibModel.SendSmtpEmailTo;

@LocalBean
@Stateless
public class MailingListService extends ServiceSuperclass {

  private static String sendInBlueKey = properties.getProperty("sendInBlue.secret.key");
  private Long sendInBlueListId = Long.valueOf(properties.getProperty("sendInBlue.list.id"));
  private Long sendInBlueConfirmationTemplateId =
      Long.valueOf(properties.getProperty("sendInBlue.confirmation.template.id"));

  ContactsApi contactsApi = new ContactsApi();
  SmtpApi smtpApi = new SmtpApi();

  static {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
    apiKey.setApiKey(sendInBlueKey);
  }

  public String getOrCreateContact(String email) {
    GetExtendedContactDetails details = getContact(email);
    if (details == null) {
      Long sendInBlueId = createContact(email);
    }
    return email;
  }

  private GetExtendedContactDetails getContact(String email) {
    GetExtendedContactDetails ecd = null;
    try {
      ecd = contactsApi.getContactInfo(email);
    } catch (ApiException e) {
      logger.log(Logger.Level.WARNING, "SendInBlue Contact:" + e.getResponseBody());
    }
    return ecd;
  }

  private Long createContact(String email) {
    CreateContact createContact = new CreateContact();
    createContact.email(email);
    try {
      CreateModel result = contactsApi.createContact(createContact);
      logger.log(Logger.Level.INFO, "Created new contact for mailing list. [" + email + "].");
      return result.getId();
    } catch (ApiException e) {
      logger.log(Logger.Level.WARNING, e.getResponseBody());
      throw new ConflictingException("Could not create contact for this email. [" + email + "].");
    }
  }

  public String addContactToList(String email) {
    AddContactToList contactEmails = new AddContactToList();

    contactEmails.addEmailsItem(email);
    try {
      PostContactInfo result = contactsApi.addContactToList(sendInBlueListId, contactEmails);
      logger.log(Logger.Level.INFO, "Added contact to mailing list. [" + email + "].");
      return result.getContacts().getSuccess().get(0);
    } catch (ApiException e) {
      logger.log(Logger.Level.WARNING, e.getResponseBody());
      throw new ConflictingException("Contact email already on mailing list. [" + email + "].");
    }
  }

  public void sendConfirmationMail(String email) {
    SendSmtpEmail confirmationMail = new SendSmtpEmail();
    confirmationMail.setTemplateId(sendInBlueConfirmationTemplateId);
    confirmationMail.setTo(List.of(new SendSmtpEmailTo().email(email)));
    try {
      smtpApi.sendTransacEmail(confirmationMail);
    } catch (ApiException e) {
      logger.log(Logger.Level.WARNING, e.getResponseBody());
      throw new ConflictingException(
          "Failed to send confirmation mail. [" + email + "]. " + e.getMessage());
    }
  }
}
