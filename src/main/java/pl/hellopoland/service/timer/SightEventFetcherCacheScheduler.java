package pl.hellopoland.service.timer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.service.PartnerService;
import pl.hellopoland.util.HelloTicket;

@Singleton
@DependsOn("DbFiller")
public class SightEventFetcherCacheScheduler {

  @Inject
  PartnerService partnerService;

  private Map<Long, List<TicketPoolDefinitionDTO>> cache = new HashMap<>();
  private HelloTicket hptClient;

  @PostConstruct
  private void afterConstruct() {
    hptClient = new HelloTicket(partnerService.getPortal("Hello Ticket Cloud").getUrl());
    populateCache();
  }

  // @Schedule(minute = "*/2", hour = "*", persistent = false)
  private void populateCache() {
    partnerService.getAll().forEach(partner -> {
      var tpds = hptClient.getTicketPoolDefinitions(partner.getHptToken());
      cache.put(partner.getId(), tpds);
    });
  }

  @Lock(LockType.READ)
  public List<TicketPoolDefinitionDTO> getHptTPDs(Long partnerId) {
    return cache.get(partnerId);
  }
}
