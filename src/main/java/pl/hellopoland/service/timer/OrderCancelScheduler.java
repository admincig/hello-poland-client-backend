package pl.hellopoland.service.timer;

import pl.hellopoland.bo.Order;
import pl.hellopoland.service.OrderService;
import pl.hellopoland.service.ServiceSuperclass;

import jakarta.ejb.DependsOn;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.inject.Inject;
import java.lang.System.Logger;
import java.util.Calendar;
import java.util.List;

@Singleton
@DependsOn({"StartupP24OrdersConfirmation"})
public class OrderCancelScheduler extends ServiceSuperclass {

  @Inject
  OrderService oService;

  @Schedule(hour = "*", minute = "*/5", second = "0", year = "*", dayOfMonth = "*", dayOfWeek = "*",
      persistent = false)
  public void run() {
    logger.log(Logger.Level.INFO, "Cancelling orders older than 30min");
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MINUTE, -30);
    List<Order> ordersToCancel =
        em.createQuery("from Order where status=:status and date<:date", Order.class)
            .setParameter("status", Order.Status.NEW).setParameter("date", cal.getTime())
            .getResultList();
    logger.log(Logger.Level.INFO, "Found " + ordersToCancel.size() + " orders to cancel");
    ordersToCancel.forEach(o -> {
      logger.log(Logger.Level.INFO, "Cancelling order " + o.getId());
      oService.cancel(o);
    });
  }
}
