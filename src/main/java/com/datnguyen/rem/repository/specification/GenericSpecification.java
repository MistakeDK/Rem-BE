package com.datnguyen.rem.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

public class GenericSpecification<T> implements Specification<T> {
    protected SpecSearchCriteria criteria;

    public GenericSpecification(SpecSearchCriteria specSearchCriteria) {
        this.criteria=specSearchCriteria;
    }

    @Override
    public Predicate toPredicate(@NonNull Root<T> root,@NonNull CriteriaQuery<?> query,@NonNull CriteriaBuilder criteriaBuilder) {
        return switch (criteria.getOperation()) {
            case EQUALITY -> criteriaBuilder.equal(root.get(criteria.getKey()), checkAndConvertEnum(root, criteria.getKey(), criteria.getValue()));
            case NEGATION -> criteriaBuilder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case GREATER_THAN -> criteriaBuilder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN -> criteriaBuilder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LIKE -> criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString() + "%");
            case STARTS_WITH -> criteriaBuilder.like(root.get(criteria.getKey()), criteria.getValue().toString() + "%");
            case ENDS_WITH -> criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString());
        };
    }
    private Object checkAndConvertEnum(Root<?> root, String key, Object value) {
        Class<?> javaType = root.get(key).getJavaType();
        if (javaType.isEnum()) {
            return Enum.valueOf((Class<Enum>) javaType, value.toString());
        }
        return value;
    }
}
