

package pl.hellopoland.config.fulltextsearch;

import org.hibernate.QueryException;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.DoubleType;
import org.hibernate.type.Type;

import java.util.List;

class PostgreSQLFullTextSearchRankVectorFunction implements SQLFunction {

  @Override
  public String render(Type firstArgumentType, List args, SessionFactoryImplementor factory) {
    String ftsConfig = null;
    String field = null;
    String value = null;
    if (args.size() == 3) {
      ftsConfig = (String) args.get(0);
      field = (String) args.get(1);
      value = (String) args.get(2);
    } else if (args.size() == 2) {
      field = (String) args.get(0);
      value = (String) args.get(1);
    } else {
      throw new IllegalArgumentException("The function must be passed 2 or 3 arguments");
    }
    if (ftsConfig != null) {
      return "ts_rank(" + field + ", plainto_tsquery(" + ftsConfig + ", " + value + "))";
    } else {
      return "ts_rank(" + field + ", plainto_tsquery(" + value + "))";
    }
  }

  @Override
  public Type getReturnType(Type columnType, Mapping mapping) throws QueryException {
    return new DoubleType();
  }

  @Override
  public boolean hasArguments() {
    return true;
  }

  @Override
  public boolean hasParenthesesIfNoArguments() {
    return false;
  }
}

