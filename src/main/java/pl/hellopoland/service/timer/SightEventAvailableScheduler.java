package pl.hellopoland.service.timer;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.service.SightEventService;
import pl.hellopoland.util.HelloTicket;

@Singleton
@DependsOn("DbFiller")
public class SightEventAvailableScheduler {

  @Inject
  SightEventService service;
  private HelloTicket hptClient;

  @PostConstruct
  private void afterConstruct() {
    hptClient = new HelloTicket(service.getPortal("Hello Ticket Cloud").getUrl());
    perform();
  }

  @Schedule(minute = "*/2", hour = "*", persistent = false)
  @Lock(LockType.WRITE)
  private void perform() {
    List<SightEvent> all = service.getAllActiveAndPublishedAndNotBlocked();
    List<SightEvent> available = hptClient.getAvailableSightEvents(all);
    available.forEach(se -> se.setAvailable(true));
    all.removeAll(available);
    all.forEach(se -> se.setAvailable(false));
  }

}
