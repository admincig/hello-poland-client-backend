package pl.hellopoland.service.timer;

import pl.hellopoland.service.FileDescriptorService;
import pl.hellopoland.service.ServiceSuperclass;

import javax.ejb.DependsOn;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.lang.System.Logger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

@Singleton
@DependsOn("DbFiller")
public class AnaliticsFilesDeleteScheduler extends ServiceSuperclass {
  final static Path PATH = Paths
      .get(properties.getProperty("dms.root.path") + File.separator + "analitics" + File.separator);

  @Inject
  FileDescriptorService fileDescriptorService;

  @Schedule(hour = "1", minute = "0", second = "0", year = "*", dayOfMonth = "*", dayOfWeek = "*",
      persistent = false)
  public void run() {
    logger.log(Logger.Level.INFO, this.getClass().getName() + " started.");
    try {
      Files.walk(PATH).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
    } catch (IOException e) {
      logger.log(Logger.Level.ERROR, e.getLocalizedMessage());
    }
  }

}
