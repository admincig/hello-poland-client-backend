package pl.hellopoland.exception;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import pl.hellopoland.exception.conflict.SightHasAssignedSightEventsException;

@ApplicationScoped
public class ExceptionFactory {

  @Inject
  private ExceptionMessagesService exceptionMessagesService;

  public SightHasAssignedSightEventsException sightHasAssignedSightEventsException() {
    return new SightHasAssignedSightEventsException(
        exceptionMessagesService
            .getMessage(SightHasAssignedSightEventsException.class.getSimpleName()));
  }
}
