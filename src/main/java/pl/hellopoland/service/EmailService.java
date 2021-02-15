package pl.hellopoland.service;

import com.sun.mail.smtp.SMTPSendFailedException;
import com.sun.mail.smtp.SMTPTransport;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
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

  public void sendEmail(Email parameterObject)
      throws MessagingException, UnsupportedEncodingException {
    var session = createSessionForEmail(getSessionProperties());
    var message = new MimeMessage(session);
    try {
      message
          .setFrom(new InternetAddress(System.getProperty(MAIL_USERNAME_PROPERTY), MAIL_PERSONAL));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(parameterObject.recipientEmail));
      message.setSubject(parameterObject.subject, "UTF-8");
      var mimeBodyPart = new MimeBodyPart();
      mimeBodyPart.setText(parameterObject.msg, "UTF-8");
      var multipart = new MimeMultipart();
      multipart.addBodyPart(mimeBodyPart);
      message.setContent(multipart);
      SMTPTransport transport = (SMTPTransport) session.getTransport("smtp");
      transport.connect();
      transport.setReportSuccess(true);
      transport.sendMessage(message, message.getAllRecipients());
    } catch (SMTPSendFailedException e) {
      // Message has been sent.
      logger.log(Level.INFO, e.getReturnCode());
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
