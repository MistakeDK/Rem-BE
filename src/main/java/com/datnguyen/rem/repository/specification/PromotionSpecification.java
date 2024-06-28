package com.datnguyen.rem.repository.specification;

import com.datnguyen.rem.entity.Promotion;
import com.datnguyen.rem.enums.PromotionType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

public class PromotionSpecification implements Specification<Promotion> {
    private SpecSearchCriteria criteria;

    public PromotionSpecification(SpecSearchCriteria specSearchCriteria) {
        this.criteria=specSearchCriteria;
    }

    @Override
    public Predicate toPredicate(@NonNull Root<Promotion> root,@NonNull CriteriaQuery<?> query,@NonNull CriteriaBuilder criteriaBuilder) {
        return switch (criteria.getOperation()){
            case EQUALITY -> criteriaBuilder
                    .equal(root.get(criteria.getKey()),checkEnum(criteria.getValue().toString(),root) ?
                            PromotionType.valueOf(criteria.getValue().toString()):
                            criteria.getValue());
            case NEGATION -> criteriaBuilder
                    .notEqual(root.get(criteria.getKey()),criteria.getValue());
            case GREATER_THAN -> criteriaBuilder
                    .greaterThan(root.get(criteria.getKey()),criteria.getValue().toString());
            case LESS_THAN -> criteriaBuilder
                    .lessThan(root.get(criteria.getKey()),criteria.getValue().toString());
            case LIKE -> criteriaBuilder
                    .like(root.get(criteria.getKey()),"%"+criteria.getValue().toString()+"%");
            case STARTS_WITH -> criteriaBuilder
                    .like(root.get(criteria.getKey()),criteria.getValue().toString()+"%");
            case ENDS_WITH -> criteriaBuilder
                    .like(root.get(criteria.getKey()),"%"+criteria.getValue().toString());
        };
    }
    private Boolean checkEnum(String check,Root<Promotion> root){
        return root.get(criteria.getKey()).getJavaType() == Enum.class;
    }
}
