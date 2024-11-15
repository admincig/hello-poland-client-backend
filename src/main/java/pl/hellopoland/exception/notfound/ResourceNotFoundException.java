package pl.hellopoland.exception.notfound;

import jakarta.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class ResourceNotFoundException extends NotFoundBaseException {
  private static final long serialVersionUID = 6190720064110333792L;

  public ResourceNotFoundException() {
    super("Resource not found");
  }
}
