package pl.hellopoland.service;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.eclipse.angus.mail.smtp.SMTPSendFailedException;
import org.eclipse.angus.mail.smtp.SMTPTransport;

import java.io.UnsupportedEncodingException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Properties;

@LocalBean
@Stateless
public class EmailService extends ServiceSuperclass {
  private final Logger logger = System.getLogger(this.getClass().getName());
  private static final String MAIL_PERSONAL = "Bilety Hello Poland";
  private static final String MAIL_USERNAME_PROPERTY = "mail.username";
  private static final String MAIL_PASSWORD_PROPERTY = "mail.password";
  private static final String MAIL_SMTP_HOST_PROPERTY = "mail.smtp.host";
  private static final String MAIL_SMTP_PORT_PROPERTY = "mail.smtp.port";
  private static final String MAIL_SMTP_AUTH_PROPERTY = "mail.smtp.auth";
  private static final String MAIL_SMTP_SOCKET_FACTORY_CLASS_PROPERTY =
      "mail.smtp.socketFactory.class";
  private static final String MAIL_SMTP_STARTTLS_ENABLE_PROPERTY = "mail.smtp.starttls.enable";
  private static final String MAIL_SMTP_ENABLED_PROPERTY = "mail.smtp.enabled";

  public void sendEmail(Email parameterObject)
      throws MessagingException, UnsupportedEncodingException {
    sendEmail(parameterObject, MAIL_PERSONAL, false);
  }

  public void sendEmail(Email parameterObject, String senderName)
      throws MessagingException, UnsupportedEncodingException {
    sendEmail(parameterObject, senderName, false);
  }

  public void sendHtmlEmail(Email parameterObject, String senderName)
      throws MessagingException, UnsupportedEncodingException {
    sendEmail(parameterObject, senderName, true);
  }

  private void sendEmail(Email parameterObject, String senderName, boolean html)
      throws MessagingException, UnsupportedEncodingException {
    if (!Boolean.TRUE.toString().equals(System.getProperty(MAIL_SMTP_ENABLED_PROPERTY))) {
      logger.log(Level.WARNING, "SMTP Integration is disabled");
      logger.log(Level.INFO, "Email:\n"
          + "To: " + parameterObject.recipientEmail + "\n"
          + "Subject: " + parameterObject.subject + "\n"
          + "Body: " + parameterObject.msg
      );
      return;
    }

    var session = createSessionForEmail(getSessionProperties());
    var message = new MimeMessage(session);
    try {
      message
          .setFrom(new InternetAddress(System.getProperty(MAIL_USERNAME_PROPERTY), senderName));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(parameterObject.recipientEmail));
      message.setSubject(parameterObject.subject, "UTF-8");
      var mimeBodyPart = new MimeBodyPart();
      if (html) {
        mimeBodyPart.setContent(parameterObject.msg, "text/html; charset=UTF-8");
      } else {
        mimeBodyPart.setText(parameterObject.msg, "UTF-8");
      }
      var multipart = new MimeMultipart();
      multipart.addBodyPart(mimeBodyPart);
      message.setContent(multipart);
      SMTPTransport transport = (SMTPTransport) session.getTransport("smtp");
      transport.connect();
      transport.setReportSuccess(true);
      transport.sendMessage(message, message.getAllRecipients());
    } catch (SMTPSendFailedException e) {
      // Message has been sent.
      logger.log(Level.INFO, e.getLocalizedMessage());
      for (Address addr : e.getValidSentAddresses()) {
        logger.log(Level.INFO, "Email has been sent to " + addr);
      }
      for (Address addr : e.getValidUnsentAddresses()) {
        logger.log(Level.INFO, "Email has not been sent to" + addr);
      }
      for (Address addr : e.getInvalidAddresses()) {
        logger.log(Level.INFO, "Email has not been sent to  " + addr);
      }
    } catch (MessagingException | UnsupportedEncodingException e) {
      logger.log(System.Logger.Level.ERROR, "Sending an email failed: " + parameterObject.recipientEmail);
      logger.log(System.Logger.Level.ERROR, e.getLocalizedMessage());
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
