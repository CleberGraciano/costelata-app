package com.integracaoOmie.integracaoOmie.model.Pedido;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.integracaoOmie.integracaoOmie.model.Product.Product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "itens_pedido")
@Entity(name = "itens_pedido")
@Getter
@Setter

public class ItemPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id")
    @JsonIgnore
    private Product produto;

    private Integer quantidade;
    private Double valorUnitario;

}
