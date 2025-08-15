package com.integracaoOmie.integracaoOmie.specification;

import org.springframework.data.jpa.domain.Specification;

import com.integracaoOmie.integracaoOmie.model.Product.Product;
import com.integracaoOmie.integracaoOmie.utils.SearchCriteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ProductSpecification implements Specification<Product> {

    private final SearchCriteria criteria;

    public ProductSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        switch (criteria.getOperation()) {
            case ":":
                return cb.like(cb.lower(root.get(criteria.getKey())),
                        "%" + criteria.getValue().toString().toLowerCase() + "%");
            case ">":
                return cb.greaterThanOrEqualTo(root.get(criteria.getKey()), (Comparable) criteria.getValue());
            case "<":
                return cb.lessThanOrEqualTo(root.get(criteria.getKey()), (Comparable) criteria.getValue());
            default:
                return null;
        }
    }
}