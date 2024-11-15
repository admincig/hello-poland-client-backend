package pl.hellopoland.health;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

@Liveness
@ApplicationScoped
public class HeapHealthCheck implements HealthCheck {

  @Override
  public HealthCheckResponse call() {
    MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
    long memUsed = memoryBean.getHeapMemoryUsage().getUsed();
    long memMax = memoryBean.getHeapMemoryUsage().getMax();
    return HealthCheckResponse.named("heap").status(memUsed < memMax * 0.9)
        .withData("memUsed", memUsed).withData("memMax", memMax).build();
  }

}
