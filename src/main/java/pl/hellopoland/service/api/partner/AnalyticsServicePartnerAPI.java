package pl.hellopoland.service.api.partner;

import pl.hellopoland.service.AnalyticsService;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.File;
import java.util.Date;

@Stateless
public class AnalyticsServicePartnerAPI {
  @Inject
  AnalyticsService service;

  @RolesAllowed("partner")
  public File getOrdersCsvFile(Date fromDate, Date toDate) {
    return service.getOrdersCsvFile(fromDate, toDate);
  }

}
