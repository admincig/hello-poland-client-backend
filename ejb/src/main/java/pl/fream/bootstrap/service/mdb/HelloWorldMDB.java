package pl.fream.bootstrap.service.mdb;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

@MessageDriven(name = HelloWorldMDB.QUEUE_NAME, activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup",
        propertyValue = HelloWorldMDB.QUEUE),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "maxSession", propertyValue = "1")})
public class HelloWorldMDB implements MessageListener {
  public static final String QUEUE_NAME = "HelloWorldQueue";
  public static final String QUEUE = "java:/jms/queue/" + QUEUE_NAME;

  private Logger log = Logger.getLogger(HelloWorldMDB.class.getName());

  @Override
  @PermitAll
  public void onMessage(Message message) {
    try {
      log.info("received message: " + ((ObjectMessage) message).getObject().toString());
    } catch (JMSException e) {
      log.log(Level.WARNING, "Failed to parse message", e);
    }
  }

}
