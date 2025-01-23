package pl.hellopoland.service;

import org.apache.commons.lang3.RandomStringUtils;
import pl.hellopoland.bo.*;
import pl.hellopoland.bo.OrderDetails.Platform;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
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

@LocalBean
@Stateless
public class AnalyticsService extends ServiceSuperclass {
  final static String PATH =
      properties.getProperty("dms.root.path") + File.separator + "analitics" + File.separator;
  final static SimpleDateFormat DATE_FORMATER = new SimpleDateFormat("yyyy-MM-dd HH:mm");
  final static BigDecimal HUNDRED = new BigDecimal("100");


  @Inject
  OrderService orderService;

  @Inject
  FileDescriptorService fileDescriptorService;

  public File getOrdersCsvFile(Date fromDate, Date toDate) {
    final File csvFile = fileDescriptorService.createEmptyFileOnDisc(
        PATH + "orders_" + RandomStringUtils.randomAlphanumeric(10) + ".csv");
    // csv file header:
    writeCsvRow(csvFile.toPath(), "NR ZAMÓWIENIA", "DATA ZAMÓWIENIA", "ID PARTNERA HP",
        "NAZWA PARTNERA", "AFILIACJA", "WARTOŚĆ", "PROWIZJA", "NAZWA UŻUTKOWNIKA", "TELEON", "ADRES EMAIL", "PLATFORMA",
        "ZALOGOWANY", "NAZWA OFERTY", "DATA OFERTY", "ILOŚĆ", "NAZWA BILETÓW", "PROMOCJA", "FAKTURA");

    var orders = orderService.getOrdersInDateRange(fromDate, toDate,
        getLoggedUser().hasRole(UserRole.Role.ADMIN) ? null : getLoggedPartner());
    for (OrderEntry oe : orders) {
      OrderDateEntry dateEntry = oe.getDateEntry();
      OrderSightEntry sightEntry = dateEntry.getSightEntry();
      SightEvent sightEvent = sightEntry.getSightEvent();
      Partner partner = sightEvent.getPartner();
      Order order = sightEntry.getOrder();
      OrderDetails oDetails = order.getDetails();
      BigDecimal commissionPercent = partner.getCommission();
      Platform platform = oDetails.getPlatform();
      BigDecimal commission = calculateCommission(commissionPercent, oe);
      BigDecimal total = new BigDecimal(oe.getSum()).divide(HUNDRED);

      writeCsvRow(
          csvFile.toPath(),
          order.getHash(),
          DATE_FORMATER.format(order.getDate()),
          String.valueOf(partner.getId()),
          partner.getName(),
          oe.getPartnerAffiliateCode() != null ? "afiliacja" : "",
          String.valueOf(total).replace(".", ","),
          String.valueOf(commission).replace(".", ","),
          oDetails.getFirstName() + " " + oDetails.getLastName(),
          oDetails.getPhone(),
          oDetails.getEmail(),
          platform != null ? platform.name() : Platform.UNKNOWN.name(),
          String.valueOf(oDetails.isUserLogged()),
          sightEvent.getName(),
          DATE_FORMATER.format(dateEntry.getDate()),
          String.valueOf(oe.getQuantity()),
          oe.getName(),
          String.valueOf(oe.getDiscount() != null),
          String.valueOf(Boolean.TRUE.equals(oDetails.getInvoice()))
      );
    }
    return csvFile;
  }

  private BigDecimal calculateCommission(BigDecimal commissionPercent, OrderEntry oe) {
    var originalTotal = new BigDecimal(oe.getUnitPrice() * oe.getQuantity());
    BigDecimal commissionVal =
        originalTotal.multiply(commissionPercent).divide(HUNDRED).setScale(2,
            RoundingMode.HALF_EVEN);
    if (oe.getDiscount() != null) {
      commissionVal = commissionVal.subtract(new BigDecimal(oe.getDiscount().getHplPart()));
    }
    commissionVal = commissionVal.divide(HUNDRED);
    return commissionVal;
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
