package pl.hellopoland.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class UnauthorizedException extends BaseException {
  private static final long serialVersionUID = 8596479382460024599L;

  public UnauthorizedException() {
    super("");
  }
}
