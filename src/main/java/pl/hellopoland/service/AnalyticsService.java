package pl.hellopoland.service;

import java.io.File;
import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
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
import pl.hellopoland.bo.OrderEntry;

@LocalBean
@Stateless
public class AnalyticsService extends ServiceSuperclass {
  final Logger logger = System.getLogger(this.getClass().getSimpleName());
  final static String PATH =
      properties.getProperty("dms.root.path") + File.separator + "analitics" + File.separator;
  final static SimpleDateFormat DATE_FORMATER = new SimpleDateFormat("yyyy-MM-dd HH:mm");

  @Inject
  OrderService orderService;

  public File getOrdersCsvFile(Date fromDate, Date toDate) {
    final File csvFile =
        createEmptyFileOnDisc(PATH + "orders_" + RandomStringUtils.randomAlphanumeric(10) + ".csv");
    // csv file header:
    writeCsvRow(csvFile.toPath(), "DATA ZAMÓWIENIA", "PŁATNOŚĆ", "NR TRANSAKCJI P24",
        "NAZWA UŻUTKOWNIKA", "TELEON", "ADRES EMAIL", "NAZWA OFERTY", "DATA OFERTY",
        "ILOŚĆ I NAZWA BILETÓW");

    var orders = orderService.getOrdersInDateRange(fromDate, toDate, getLoggedPartner());
    for (OrderEntry oe : orders) {
      OrderDateEntry dateEntry = oe.getDateEntry();
      Order order = dateEntry.getSightEntry().getOrder();
      OrderDetails oDetails = order.getDetails();

      writeCsvRow(csvFile.toPath(), DATE_FORMATER.format(order.getDate()),
          Double.valueOf(oe.getUnitPrice() * oe.getQuantity()) / 100 + " " + order.getP24Currency(),
          order.getP24OrderId(), oDetails.getFirstName() + " " + oDetails.getLastName(),
          oDetails.getPhone(), oDetails.getEmail(),
          dateEntry.getSightEntry().getSightEvent().getName(),
          DATE_FORMATER.format(dateEntry.getDate()), oe.getQuantity() + " x " + oe.getName());
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

  private File createEmptyFileOnDisc(String path) {
    File targetFile = new File(path);
    File parent = targetFile.getParentFile();
    if (!parent.exists() && !parent.mkdirs()) {
      logger.log(Level.ERROR, "Couldn't create dir: " + parent);
      throw new IllegalStateException("Couldn't create dir: " + parent);
    }
    return targetFile;
  }

}
