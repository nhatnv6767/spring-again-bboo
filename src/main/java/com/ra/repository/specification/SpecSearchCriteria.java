package com.ra.repository.specification;

import lombok.Getter;

@Getter

public class SpecSearchCriteria {
    private String key;
    private SearchOperation operation;
    private Object value;
    private Boolean orPredicate;

    public SpecSearchCriteria(String key, SearchOperation operation, Object value) {
        super();
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public SpecSearchCriteria(String orPredicate, String key, SearchOperation operation, Object value) {
        super();
        this.orPredicate = orPredicate != null && orPredicate.equals(SearchOperation.OR_PREDICATE_FLAG);
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public SpecSearchCriteria(String key, String operation, Object value, String prefix, String suffix) {
        SearchOperation oper = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (oper != null) {
            if (oper == SearchOperation.EQUALITY) {
                boolean startWithAsterisk = prefix != null && prefix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
                boolean endWithAsterisk = suffix != null && suffix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
                if (startWithAsterisk && endWithAsterisk) {
                    this.operation = SearchOperation.CONTAINS;
                } else if (startWithAsterisk) {
                    this.operation = SearchOperation.ENDS_WITH;
                } else if (endWithAsterisk) {
                    this.operation = SearchOperation.STARTS_WITH;
                } else {
                    this.operation = SearchOperation.EQUALITY;
                }
            } else {
                this.operation = oper;
            }
        }
        this.key = key;
        this.value = value;
    }


}
