package com.integracaoOmie.integracaoOmie.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.springframework.data.jpa.domain.Specification;

import com.integracaoOmie.integracaoOmie.utils.SearchCriteria;

public class SpecificationBuilder<T> {
    private final List<SearchCriteria> params = new ArrayList<>();

    public SpecificationBuilder<T> with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<T> build(Function<SearchCriteria, Specification<T>> specFactory) {
        if (params.isEmpty())
            return Specification.where(null);

        Specification<T> result = specFactory.apply(params.get(0));
        for (int i = 1; i < params.size(); i++) {
            result = result.and(specFactory.apply(params.get(i)));
        }
        return result;
    }
}
