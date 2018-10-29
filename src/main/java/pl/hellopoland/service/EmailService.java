package pl.hellopoland.service;

import java.lang.System.Logger;
import java.util.Properties;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

@LocalBean
@Stateless
public class EmailService extends ServiceSuperclass {
  private static final Logger lOG = System.getLogger("EmailService");

  private static final String MAIL_USERNAME_PROPERTY = "mail.username";
  private static final String MAIL_PASSWORD_PROPERTY = "mail.password";
  private static final String MAIL_SMTP_HOST_PROPERTY = "mail.smtp.host";
  private static final String MAIL_SMTP_PORT_PROPERTY = "mail.smtp.port";
  private static final String MAIL_SMTP_AUTH_PROPERTY = "mail.smtp.auth";
  private static final String MAIL_SMTP_SOCKET_FACTORY_CLASS_PROPERTY =
      "mail.smtp.socketFactory.class";
  private static final String MAIL_SMTP_STARTTLS_ENABLE_PROPERTY = "mail.smtp.starttls.enable";

  public void sendEmail(String recipientEmail, String subject, String msg)
      throws MessagingException {
    var message = new MimeMessage(createSessionForEmail(getSessionProperties()));
    try {
      message.setFrom(new InternetAddress(System.getProperty(MAIL_USERNAME_PROPERTY)));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
      message.setSubject(subject, "UTF-8");
      var mimeBodyPart = new MimeBodyPart();
      mimeBodyPart.setText(msg, "UTF-8");
      var multipart = new MimeMultipart();
      multipart.addBodyPart(mimeBodyPart);
      message.setContent(multipart);
      Transport.send(message);
    } catch (MessagingException e) {
      lOG.log(System.Logger.Level.ERROR, "Sending an email failed: " + recipientEmail);
      lOG.log(System.Logger.Level.ERROR, e.getLocalizedMessage());
      throw e;
    }

  }

  private Properties getSessionProperties() {
    Properties properties = new Properties();
    properties.put(MAIL_SMTP_HOST_PROPERTY, System.getProperty(MAIL_SMTP_HOST_PROPERTY));
    properties.put(MAIL_SMTP_PORT_PROPERTY, System.getProperty(MAIL_SMTP_PORT_PROPERTY));
    properties.put(MAIL_SMTP_AUTH_PROPERTY, System.getProperty(MAIL_SMTP_AUTH_PROPERTY));
    if (System.getProperty(MAIL_SMTP_SOCKET_FACTORY_CLASS_PROPERTY) != null) {
      properties.put(MAIL_SMTP_SOCKET_FACTORY_CLASS_PROPERTY,
          System.getProperty(MAIL_SMTP_SOCKET_FACTORY_CLASS_PROPERTY));
    }
    if (System.getProperty(MAIL_SMTP_STARTTLS_ENABLE_PROPERTY) != null) {
      properties.put(MAIL_SMTP_STARTTLS_ENABLE_PROPERTY,
          System.getProperty(MAIL_SMTP_STARTTLS_ENABLE_PROPERTY));
    }
    return properties;
  }

  private Session createSessionForEmail(Properties properties) {
    return Session.getInstance(properties, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(System.getProperty(MAIL_USERNAME_PROPERTY),
            System.getProperty(MAIL_PASSWORD_PROPERTY));
      }
    });
  }

}
