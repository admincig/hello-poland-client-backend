package pl.hellopoland.service.api.hp;

import java.io.File;
import java.util.Date;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.dto.PartnerDTO;
import pl.hellopoland.service.AnalyticsService;
import pl.hellopoland.service.HellopolandService;
import pl.hellopoland.util.DtoMapper;

@Stateless
public class HellopolandServiceAPI {
  @Inject
  private HellopolandService service;
  @Inject
  AnalyticsService analyticsService;

  @RolesAllowed({"admin", "salesman"})
  public PartnerDTO addPartner(PartnerDTO partner) {
    return DtoMapper.getFullDTO(service.addPartner(partner));
  }

  @RolesAllowed("admin")
  public File getOrdersCsvFile(Date fromDate, Date toDate) {
    return analyticsService.getOrdersCsvFile(fromDate, toDate);
  }

}
