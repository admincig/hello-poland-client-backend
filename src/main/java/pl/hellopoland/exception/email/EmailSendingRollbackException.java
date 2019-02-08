package pl.hellopoland.exception.email;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class EmailSendingRollbackException extends EmailSendingException {
  private static final long serialVersionUID = 6174549224180339688L;
}
