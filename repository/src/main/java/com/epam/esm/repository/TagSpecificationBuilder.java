package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class TagSpecificationBuilder {
    private static final String NAME = "name";

    private Specification<Tag> composedSpecification;

    public TagSpecificationBuilder() {
        composedSpecification = Specification.where(null);
    }

    public Specification<Tag> build() {
        return composedSpecification;
    }

    public TagSpecificationBuilder tagNames(List<String> tagNames) {
        if (tagNames != null) {
            Specification<Tag> partialSpecification
                    = (tagRoot, criteriaQuery, criteriaBuilder) -> {
                Predicate resultPredicate;
                List<Predicate> tagPredicates = new ArrayList<>();
                for (String tagName: tagNames) {
                    tagPredicates.add(criteriaBuilder.like(tagRoot.get(NAME), "%" + tagName + "%"));
                }
                resultPredicate = tagPredicates.get(0);
                for (Predicate predicate: tagPredicates) {
                    resultPredicate = criteriaBuilder.or(resultPredicate, predicate);
                }
                return resultPredicate;
            };
            composedSpecification = composedSpecification.and(partialSpecification);
        }

        return this;
    }
}
