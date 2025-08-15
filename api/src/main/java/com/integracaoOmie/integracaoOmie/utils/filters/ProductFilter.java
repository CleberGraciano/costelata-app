package com.integracaoOmie.integracaoOmie.utils.filters;

import lombok.Data;

@Data
public class ProductFilter {
    private String descricao;
    private Double valorMin;
    private Double valorMax;
    private String nomeCategoria;
}
