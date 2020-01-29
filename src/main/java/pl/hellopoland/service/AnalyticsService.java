package pl.hellopoland.service;

import java.io.File;
import java.io.IOException;
import java.lang.System.Logger.Level;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.commons.lang3.RandomStringUtils;
import pl.hellopoland.bo.Order;
import pl.hellopoland.bo.OrderDateEntry;
import pl.hellopoland.bo.OrderDetails;
import pl.hellopoland.bo.OrderDetails.Platform;
import pl.hellopoland.bo.OrderEntry;
import pl.hellopoland.bo.OrderSightEntry;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.UserRole;

@LocalBean
@Stateless
public class AnalyticsService extends ServiceSuperclass {
  final static String PATH =
      properties.getProperty("dms.root.path") + File.separator + "analitics" + File.separator;
  final static SimpleDateFormat DATE_FORMATER = new SimpleDateFormat("yyyy-MM-dd HH:mm");

  @Inject
  OrderService orderService;

  @Inject
  FileDescriptorService fileDescriptorService;

  public File getOrdersCsvFile(Date fromDate, Date toDate) {
    final File csvFile = fileDescriptorService.createEmptyFileOnDisc(
        PATH + "orders_" + RandomStringUtils.randomAlphanumeric(10) + ".csv");
    // csv file header:
    writeCsvRow(csvFile.toPath(), "NR ZAMÓWIENIA", "DATA ZAMÓWIENIA", "ID PARTNERA HP",
        "ID PARTNERA P24",
        "NAZWA PARTNERA", "AFILIACJA", "WARTOŚĆ", "PROWIZJA", "WALUTA", "NR TRANSAKCJI P24",
        "TYTUŁ PRZELEWU P24", "NAZWA UŻUTKOWNIKA", "TELEON", "ADRES EMAIL", "PLATFORMA",
        "ZALOGOWANY", "NAZWA OFERTY", "DATA OFERTY", "ILOŚĆ", "NAZWA BILETÓW", "PROMOCJA");

    var orders = orderService.getOrdersInDateRange(fromDate, toDate,
        getLoggedUser().hasRole(UserRole.Role.ADMIN) ? null : getLoggedPartner());
    for (OrderEntry oe : orders) {
      OrderDateEntry dateEntry = oe.getDateEntry();
      OrderSightEntry sightEntry = dateEntry.getSightEntry();
      SightEvent sightEvent = sightEntry.getSightEvent();
      Partner partner = sightEvent.getPartner();
      Order order = sightEntry.getOrder();
      OrderDetails oDetails = order.getDetails();
      BigDecimal commission = partner.getCommission();
      Platform platform = oDetails.getPlatform();

      var hundred = new BigDecimal("100");
      var total = new BigDecimal(oe.getUnitPrice() * oe.getQuantity()).divide(hundred);
      BigDecimal commissionVal =
          total.multiply(commission).divide(hundred).setScale(2, RoundingMode.HALF_EVEN);

      writeCsvRow(csvFile.toPath(),
          order.getHash(),
          DATE_FORMATER.format(order.getDate()),
          String.valueOf(partner.getId()),
          String.valueOf(partner.getP24Id()),
          partner.getName(),
          oe.getPartnerAffiliateCode() != null ? "afiliacja" : "",
          String.valueOf(total).replace(".", ","),
          String.valueOf(commissionVal).replace(".", ","),
          order.getP24Currency(),
          order.getP24OrderId(),
          order.getP24Statement(),
          oDetails.getFirstName() + " " + oDetails.getLastName(),
          oDetails.getPhone(),
          oDetails.getEmail(),
          platform != null ? platform.name() : Platform.UNKNOWN.name(),
          String.valueOf(oDetails.isUserLogged()),
          sightEvent.getName(),
          DATE_FORMATER.format(dateEntry.getDate()),
          String.valueOf(oe.getQuantity()),
          oe.getName(),
          String.valueOf(oe.getDiscount() != null));
    }
    return csvFile;
  }

  private void writeCsvRow(Path path, String... strings) {
    try (var writer =
        Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
      for (String string : strings) {
        writer.append(string).append(";");
      }
      writer.newLine();
    } catch (IOException e) {
      logger.log(Level.ERROR, e.getLocalizedMessage());
    }
  }

}
