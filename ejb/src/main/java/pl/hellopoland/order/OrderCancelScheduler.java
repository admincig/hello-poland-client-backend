package pl.hellopoland.order;

import java.util.Calendar;
import java.util.List;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import pl.hellopoland.ServiceSuperclass;

@Singleton
public class OrderCancelScheduler extends ServiceSuperclass {

  @Inject
  OrderService oService;

  @Schedule(hour = "*", minute = "*/5", second = "0", year = "*", dayOfMonth = "*", dayOfWeek = "*",
      persistent = false)
  public void run() {
    logger.info("Cancelling orders older than 30min");
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MINUTE, -30);
    List<Order> ordersToCancel =
        em.createQuery("from Order where status=:status and date<:date", Order.class)
            .setParameter("status", Order.Status.NEW).setParameter("date", cal.getTime())
            .getResultList();
    logger.info("Found " + ordersToCancel.size() + " orders to cancel");
    ordersToCancel.forEach(o -> {
      logger.info("Cancelling order " + o.getId());
      oService.cancel(o);
    });
  }
}
