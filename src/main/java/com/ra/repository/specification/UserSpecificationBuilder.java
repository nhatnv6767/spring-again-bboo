package com.ra.repository.specification;

import java.util.List;

public class UserSpecificationBuilder {
    public final List<SpecSearchCriteria> params;


    public UserSpecificationBuilder(List<SpecSearchCriteria> params) {
        this.params = params;
    }

    public UserSpecificationBuilder with(String key, String operation, Object value, String prefix, String suffix) {
        with(null, key, operation, value, prefix, suffix);
        return this;
    }

    public UserSpecificationBuilder with(String orPredicate, String key, String operation, Object value, String prefix, String suffix) {
        SearchOperation oper = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (oper != null) {
            if (oper == SearchOperation.EQUALITY) {
                boolean startWithAsterisk = prefix != null && prefix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
                boolean endWithAsterisk = suffix != null && suffix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
                if (startWithAsterisk && endWithAsterisk) {
                    oper = SearchOperation.CONTAINS;
                } else if (startWithAsterisk) {
                    oper = SearchOperation.ENDS_WITH;
                } else if (endWithAsterisk) {
                    oper = SearchOperation.STARTS_WITH;
                } else {
                    oper = SearchOperation.EQUALITY;
                }
            }
        }
        params.add(new SpecSearchCriteria(orPredicate, key, oper, value));
        return this;
    }
}
