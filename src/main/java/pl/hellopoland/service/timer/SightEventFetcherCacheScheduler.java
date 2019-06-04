package pl.hellopoland.service.timer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.service.PartnerService;
import pl.hellopoland.service.ServiceSuperclass;
import pl.hellopoland.util.HelloTicket;

@Startup
@Singleton
public class SightEventFetcherCacheScheduler extends ServiceSuperclass {

  @Inject
  private PartnerService partnerService;

  private Map<String, List<TicketPoolDefinitionDTO>> cache = new HashMap<>();
  private HelloTicket hptClient = new HelloTicket(getPortal("Hello Ticket Cloud").getUrl());

  @PostConstruct
  @Schedule(minute = "*/2", hour = "*", persistent = false)
  private void populateCache() {
    partnerService.getAll().forEach(partner -> {
      var tpds = hptClient.getTicketPoolDefinitions(partner.getHptToken());
      cache.put(partner.getHptToken(), tpds);
    });
  }

  @Lock(LockType.READ)
  public List<TicketPoolDefinitionDTO> getHptTPDs(String hptToken) {
    return cache.get(hptToken);
  }
}
