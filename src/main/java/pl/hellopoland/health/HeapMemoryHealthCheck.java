package pl.hellopoland.health;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;

import javax.enterprise.context.ApplicationScoped;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

@Health
@ApplicationScoped
public class HeapMemoryHealthCheck implements HealthCheck {

  private double maxPercentage = 0.9;

  @Override
  public HealthCheckResponse call() {
    MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
    long memUsed = memoryBean.getHeapMemoryUsage().getUsed();
    long memMax = memoryBean.getHeapMemoryUsage().getMax();

    HealthCheckResponseBuilder responseBuilder =
        HealthCheckResponse.named("heap-memory").withData("used", memUsed).withData("max", memMax)
            .withData("max %", String.valueOf(maxPercentage));

    if (memMax > 0) {
      boolean status = (memUsed < memMax * maxPercentage);
      return responseBuilder.state(status).build();
    } else {
      // Max not available
      return responseBuilder.up().build();
    }

  }
}
