package com.integracaoOmie.integracaoOmie.dto.Pedido;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class ItemPedidoResponseDTO {
    private Long id;
    private Long produtoId;
    private String produtoDescricao;
    private Integer quantidade;
    private Double valorUnitario;
}
