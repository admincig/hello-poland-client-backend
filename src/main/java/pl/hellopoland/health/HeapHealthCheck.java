package pl.hellopoland.health;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.enterprise.context.ApplicationScoped;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

@Health
@ApplicationScoped
public class HeapHealthCheck implements HealthCheck {

  @Override
  public HealthCheckResponse call() {
    MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
    long memUsed = memoryBean.getHeapMemoryUsage().getUsed();
    long memMax = memoryBean.getHeapMemoryUsage().getMax();
    return HealthCheckResponse.named("heap").state(memUsed < memMax * 0.9)
        .withData("memUsed", memUsed).withData("memMax", memMax).build();
  }

}
