package pl.hellopoland.health;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Liveness;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

@Liveness
@ApplicationScoped
public class SystemLoadHealthCheck implements HealthCheck {

  private double max = 0.7;

  @Override
  public HealthCheckResponse call() {
    OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();

    String arch = operatingSystemMXBean.getArch();
    String name = operatingSystemMXBean.getName();
    String version = operatingSystemMXBean.getVersion();
    int availableProcessors = operatingSystemMXBean.getAvailableProcessors();

    double systemLoadAverage = operatingSystemMXBean.getSystemLoadAverage();
    double systemLoadAveragePerProcessors = systemLoadAverage / availableProcessors;

    HealthCheckResponseBuilder responseBuilder =
        HealthCheckResponse.named("system-load").withData("name", name).withData("arch", arch)
            .withData("version", version).withData("processors", availableProcessors)
            .withData("loadAverage", String.valueOf(systemLoadAverage))
            .withData("loadAverage per processor", String.valueOf(systemLoadAveragePerProcessors))
            .withData("loadAverage max", String.valueOf(max));

    if (systemLoadAverage > 0) {
      boolean status = systemLoadAveragePerProcessors < max;
      return responseBuilder.status(status).build();
    } else {
      // Load average not available
      return responseBuilder.up().build();
    }

  }
}
