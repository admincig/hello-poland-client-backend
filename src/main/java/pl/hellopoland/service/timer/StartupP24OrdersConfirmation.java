package pl.hellopoland.service.timer;

import pl.hellopoland.bo.Order;
import pl.hellopoland.bo.Order.Status;
import pl.hellopoland.service.OrderService;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.DependsOn;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Startup
@Singleton
@DependsOn({"Configuration", "DbFiller"})
public class StartupP24OrdersConfirmation {
  private final Logger logger = System.getLogger(this.getClass().getName());

  @Inject
  private OrderService orderService;

  @PostConstruct
  public void processingOrdersConfirmation() {
    logger.log(Logger.Level.INFO, "Start processing");
    try {
      var now = LocalDateTime.now();
      var fromDate = Date.from(now.minusHours(5).atZone(ZoneId.systemDefault()).toInstant());
      var toDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
      orderService.getOrdersInDateRangeAndStatus(fromDate, toDate, Status.CONFIRMED)
          .forEach(o -> confirm(o));
    } catch (Exception e) {
      logger.log(Level.ERROR, e.getLocalizedMessage());
    }
    logger.log(Logger.Level.INFO, "End processing");
  }

  private void confirm(Order o) {
    logger.log(Logger.Level.INFO, "Processing order [id=" + o.getId() + "]");
    orderService.confirmInExternalAPI(o);
  }

}
