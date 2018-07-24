package pl.hellopoland.exception.notfound;

import javax.ejb.ApplicationException;
import javax.mail.Quota.Resource;

@ApplicationException(rollback = true)
public class ResourceNotFoundException extends NotFoundBaseException {

  public ResourceNotFoundException() {
    super("Resource not found");
  }
}
