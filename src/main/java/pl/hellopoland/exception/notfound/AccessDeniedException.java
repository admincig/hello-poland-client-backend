package pl.hellopoland.exception.notfound;

import jakarta.ejb.ApplicationException;
import jakarta.ejb.EJBAccessException;

@ApplicationException(rollback = true)
public class AccessDeniedException extends EJBAccessException {
  private static final long serialVersionUID = 6190720064110333792L;

  public AccessDeniedException() {
    super("Access denied to the resource");
  }
}
