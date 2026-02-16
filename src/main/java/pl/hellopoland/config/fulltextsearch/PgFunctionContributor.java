package pl.hellopoland.config.fulltextsearch;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.type.BasicType;
import org.hibernate.type.StandardBasicTypes;

public class PgFunctionContributor implements FunctionContributor {
	@Override
	public void contributeFunctions(FunctionContributions functionContributions) {
		BasicType<Boolean>
				resolveType = functionContributions.getTypeConfiguration().getBasicTypeRegistry().resolve(
				StandardBasicTypes.BOOLEAN);
		//functionContributions.getFunctionRegistry().registerPattern("tsearch","to_tsvector(?1, ?2) @@ plainto_tsquery(?1, ?3)",resolveType);
        functionContributions.getFunctionRegistry().registerPattern(
                "tsearch",
                "CASE WHEN to_tsvector(?1, ?2) @@ to_tsquery(?1, ?3) THEN true ELSE false END",
                resolveType
        );



	}
}
