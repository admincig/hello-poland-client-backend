package pl.hellopoland.health;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;
import java.lang.System.Logger.Level;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@Health
@ApplicationScoped
public class DatabaseHealthCheck implements HealthCheck {

  @Resource(lookup = "java:module/hellopolandDS")
  private DataSource ds;

  @Override
  public HealthCheckResponse call() {

    HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("membership");
    try {
      Connection connection = ds.getConnection();
      boolean isValid = connection.isValid(1000);
      DatabaseMetaData metaData = connection.getMetaData();
      connection.close();
      responseBuilder =
          responseBuilder.withData("databaseProductName", metaData.getDatabaseProductName())
              .withData("databaseProductVersion", metaData.getDatabaseProductVersion())
              .withData("driverName", metaData.getDriverName())
              .withData("driverVersion", metaData.getDriverVersion()).withData("isValid", isValid);

      return responseBuilder.state(isValid).build();

    } catch (SQLException e) {
      System.getLogger(DatabaseHealthCheck.class.getName()).log(Level.WARNING, e);
      responseBuilder = responseBuilder.withData("exceptionMessage", e.getMessage());
      return responseBuilder.down().build();
    }
  }

}
