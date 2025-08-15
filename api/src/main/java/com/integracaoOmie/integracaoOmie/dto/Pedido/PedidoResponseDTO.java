package com.integracaoOmie.integracaoOmie.dto.Pedido;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class PedidoResponseDTO {
    private Long id;
    private String numeroPedido;
    private LocalDate dataEmissao;
    private String cliente;
    private Double valorTotal;
    private String status;
    private List<ItemPedidoResponseDTO> itens;

}
