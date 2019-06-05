package pl.hellopoland.service.timer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.service.PartnerService;
import pl.hellopoland.util.HelloTicket;

@Singleton
@Startup
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

  @Schedule(minute = "*/2", hour = "*", persistent = false)
  private void populateCache() {
    partnerService.getAll().forEach(partner -> {
      var tpds = hptClient.getTicketPoolDefinitions(partner.getHptToken());
      cache.put(partner.getId(), tpds);
    });
  }

  public List<TicketPoolDefinitionDTO> getHptTPDs(Long partnerId) {
    return cache.get(partnerId).stream().map(t -> {
      try {
        return (TicketPoolDefinitionDTO) t.clone();
      } catch (CloneNotSupportedException e) {
        System.out.println("failed to clone");
        return t;
      }
    }).collect(Collectors.toList());
  }
}
