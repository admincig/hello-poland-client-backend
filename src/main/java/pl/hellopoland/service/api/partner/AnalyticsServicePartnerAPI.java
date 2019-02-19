package pl.hellopoland.service.api.partner;

import java.io.File;
import java.util.Date;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.service.AnalyticsService;

@Stateless
public class AnalyticsServicePartnerAPI {
  @Inject
  AnalyticsService service;

  @RolesAllowed("partner")
  public File getOrdersCsvFile(Date fromDate, Date toDate) {
    return service.getOrdersCsvFile(fromDate, toDate);
  }

}
