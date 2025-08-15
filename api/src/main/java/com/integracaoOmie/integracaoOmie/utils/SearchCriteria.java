package com.integracaoOmie.integracaoOmie.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchCriteria {
    public SearchCriteria(String key, String operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    private String key;
    private String operation;
    private Object value;
}
