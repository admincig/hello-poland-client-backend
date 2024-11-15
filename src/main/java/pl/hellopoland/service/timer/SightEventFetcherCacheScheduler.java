package pl.hellopoland.service.timer;

import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.service.PartnerService;
import pl.hellopoland.util.HelloTicket;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.DependsOn;
import jakarta.ejb.Singleton;
import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
      var tpds = hptClient.getTicketPoolDefinitions(partner.getHptToken(), null);
      cache.put(partner.getId(), tpds);
    });
  }

}
