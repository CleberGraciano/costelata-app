package com.integracaoOmie.integracaoOmie.dto.Product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {
    @NotBlank
    private String codigo;

    @NotBlank
    private String descricao;

    private String descricaoDetalhada;

    @NotBlank
    private String unidade;

    @DecimalMin("0.0")
    private double valorUnitario;

    @NotBlank
    private String codigoCategoria;

    @NotBlank
    private String nomeCategoria;

    private String degelo;
    private String armazenamento;
    private String rendimentoPorProduto;
    private String argumentosVenda;
    private String exposicaoPDV;
    private String explicacoesEtiquetas;
}
