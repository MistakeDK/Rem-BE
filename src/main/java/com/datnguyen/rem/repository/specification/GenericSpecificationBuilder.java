package com.datnguyen.rem.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class GenericSpecificationBuilder<T> {
    private final List<SpecSearchCriteria> params = new ArrayList<>();

    public GenericSpecificationBuilder<T> with(String key, String operation, Object value, String prefix, String suffix) {
        return with(null, key, operation, value, prefix, suffix);
    }

    public GenericSpecificationBuilder<T> with(String orPredicate, String key, String operation, Object value, String prefix, String suffix) {
        SearchOperation oper = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (oper == SearchOperation.EQUALITY) {
            boolean startWithAsterisk = prefix != null && prefix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
            boolean endWithAsterisk = suffix != null && suffix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
            if (startWithAsterisk && endWithAsterisk) {
                oper = SearchOperation.LIKE;
            } else if (startWithAsterisk) {
                oper = SearchOperation.ENDS_WITH;
            } else if (endWithAsterisk) {
                oper = SearchOperation.STARTS_WITH;
            }
        }
        params.add(new SpecSearchCriteria(orPredicate, key, oper, value));
        return this;
    }

    public Specification<T> build(Class<T> entityClass) {
        if (params.isEmpty()) {
            return null;
        }
        Specification<T> specification = new GenericSpecification<>(params.get(0)) {};
        for (int i = 1; i < params.size(); i++) {
            specification = params.get(i).getOrPredicate()
                    ? Specification.where(specification).or(new GenericSpecification<>(params.get(i)) {})
                    : Specification.where(specification).and(new GenericSpecification<>(params.get(i)) {});
        }
        return specification;
    }
}
