package pl.hellopoland.service.timer;

import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.service.SightEventService;
import pl.hellopoland.util.HelloTicket;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.*;
import jakarta.inject.Inject;
import java.util.List;

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
    List<Long> ids = service.getAllActiveAndPublishedAndNotBlocked();
    List<Long> available = hptClient.getAvailableSightEvents(ids);
    ids.removeAll(available);
    service.setAvailableByHptId(available, true);
    service.setAvailableByHptId(ids, false);
//    available.forEach(se -> se.setAvailable(true));
//    all.forEach(se -> se.setAvailable(false));
  }

}
