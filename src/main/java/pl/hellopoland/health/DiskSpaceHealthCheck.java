package pl.hellopoland.health;

import java.io.File;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;

@Health
@ApplicationScoped
public class DiskSpaceHealthCheck implements HealthCheck {

  private double maxPercentage = 0.9;

  @Override
  public HealthCheckResponse call() {
    File path = new File(".");
    long freeMegaBytes = path.getFreeSpace() / 1024 / 1024;
    long allMegaBytes = path.getTotalSpace() / 1024 / 1024;
    boolean enoughSpace = freeMegaBytes < allMegaBytes * maxPercentage;

    HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("disk-space")
        .withData("totalMegabytes", allMegaBytes).withData("freeMegabytes", freeMegaBytes)
        .withData("% used", String.valueOf((100 - (100.0 * freeMegaBytes / allMegaBytes))));

    return responseBuilder.state(enoughSpace).build();
  }
}
