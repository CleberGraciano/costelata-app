package com.integracaoOmie.integracaoOmie.model.Product;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "products")
@Entity(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String codigo;
    String descricao;
    String descricaoDetalhada;
    String unidade;
    double valorUnitario;
    String codigoCategoria;
    String nomeCategoria;

    String degelo;
    String armazenamento;
    String rendimentoPorProduto;
    String argumentosVenda;
    String exposicaoPDV;
    String explicacoesEtiquetas;
}
