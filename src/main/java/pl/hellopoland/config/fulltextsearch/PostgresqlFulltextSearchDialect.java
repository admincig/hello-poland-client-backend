package pl.hellopoland.config.fulltextsearch;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.dialect.PostgreSQLDialect;

/**
 * <h1>Fream Commons Fulltext Search Dialect. Copied from JBossEE.</h1> In order to use this dialect
 * include this class in classloader and set property:<br/>
 * <i>hibernate.dialect=pl.fream.commons.fulltextsearch.PostgresqlFulltextSearchDialect</i> <br/>
 * <br/>
 * Example of use:<br/>
 * <i>em.createQuery("from User e where tsearch(e.searchIndex, :query)=true").setParameter("query",
 * "jan kowalski").getResultList();</i>
 * 
 * @author kret11
 *
 */
public class PostgresqlFulltextSearchDialect extends PostgreSQLDialect {

  @Override public void initializeFunctionRegistry(FunctionContributions functionContributions) {
    super.initializeFunctionRegistry(functionContributions);

    functionContributions.getFunctionRegistry().registerPattern("match", "match (?1) against (?2 in boolean mode)",
        functionContributions.getTypeConfiguration().getBasicTypeRegistry().resolve(StandardBasicTypes.DOUBLE));
  }

  public PostgresqlFulltextSearchDialect() {
    registerFunction("tsearch", new PostgreSQLFullTextSearchStringFunction());
    registerFunction("tsearchrank", new PostgreSQLFullTextSearchRankStringFunction());

    registerFunction("vsearch", new PostgreSQLFullTextSearchVectorFunction());
    registerFunction("vsearchrank", new PostgreSQLFullTextSearchRankVectorFunction());
  }

}
