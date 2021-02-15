package pl.hellopoland.exception;

import pl.hellopoland.exception.conflict.SightHasAssignedSightEventsException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ExceptionFactory {

  @Inject
  private ExceptionMessagesService exceptionMessagesService;

  public SightHasAssignedSightEventsException sightHasAssignedSightEventsException() {
    return new SightHasAssignedSightEventsException(exceptionMessagesService
        .getMessage(SightHasAssignedSightEventsException.class.getSimpleName()));
  }
}
