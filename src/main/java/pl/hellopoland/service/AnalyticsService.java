package pl.hellopoland.service;

import java.io.File;
import java.util.Date;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

@LocalBean
@Stateless
public class AnalyticsService extends ServiceSuperclass {
  @Inject
  OrderService orderService;

  public File getOrdersCsvFile(Date fromDate, Date toDate) {
    var orders = orderService.getOrdersInDateRange(fromDate, toDate);

    return null;
  }

}
