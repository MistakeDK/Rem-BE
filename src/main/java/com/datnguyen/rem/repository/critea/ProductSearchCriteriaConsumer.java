package com.datnguyen.rem.repository.critea;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.function.Consumer;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSearchCriteriaConsumer implements Consumer<SearchCriteria> {
    CriteriaBuilder builder;
    Root root;
    Predicate predicate;
    @Override
    public void accept(SearchCriteria param) {
        if(root.get(param.getKey()).getJavaType()== Double.class){
            predicate= builder.and(predicate,builder.
                    lessThanOrEqualTo(root.get(param.getKey()),param.getValue().toString()));
        } else if (root.get(param.getKey()).getJavaType()== Boolean.class) {
            predicate=builder.and(predicate,builder
                    .equal(root.get(param.getKey()),Boolean.valueOf(param.getValue().toString()) ));
        } else {
            if(root.get(param.getKey()).getJavaType()==String.class){
                predicate= builder.and(predicate,builder.
                        like(root.get(param.getKey()),"%"+param.getValue().toString()+"%"));
            }else {
                predicate= builder.and(predicate,builder.
                        equal(root.get(param.getKey()),param.getValue().toString()));
            }
        }
    }
}
