package pl.hellopoland.exception.conflict;

public class SightHasAssignedSightEventsException extends ConflictBaseException {

  private static final long serialVersionUID = -2724010649763113902L;

  public SightHasAssignedSightEventsException() {

  }

  public SightHasAssignedSightEventsException(String message) {
    super(message);
  }

}
