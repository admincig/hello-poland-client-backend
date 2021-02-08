package pl.hellopoland.service.api.partner;

import pl.hellopoland.rest.dto.HplInstanceRO;
import pl.hellopoland.service.DiscoveryService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class DiscoveryServicePartnerAPI {

  @Inject
  private DiscoveryService service;

  public List<HplInstanceRO> discovery() {
    return service.discovery().stream()
        .map(instance -> new HplInstanceRO(instance.getName(), instance.getUrl(), instance.getTosUrl(), instance.getPrivacyPolicyUrl()))
        .collect(Collectors.toList());
  }

}
