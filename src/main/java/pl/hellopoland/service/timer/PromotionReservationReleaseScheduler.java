package pl.hellopoland.service.timer;

import pl.hellopoland.service.PromotionCodeService;
import pl.hellopoland.service.ServiceSuperclass;

import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.inject.Inject;
import java.lang.System.Logger;

@Singleton
public class PromotionReservationReleaseScheduler extends ServiceSuperclass {

  @Inject
  PromotionCodeService promotionCodeService;

  @Schedule(hour = "*", minute = "*/10", second = "0", year = "*", dayOfMonth = "*",
      dayOfWeek = "*", persistent = false)
  public void run() {
    logger.log(Logger.Level.INFO, "Releasing expired promotion code reservations");
    promotionCodeService.releaseExpiredReservations();
  }
}
