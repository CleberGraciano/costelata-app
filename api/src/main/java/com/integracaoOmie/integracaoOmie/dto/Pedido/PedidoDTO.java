package com.integracaoOmie.integracaoOmie.dto.Pedido;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoDTO {
    @NotBlank
    private String numeroPedido;

    @NotBlank
    private String dataEmissao;

    @NotBlank
    private String cliente;

    @NotNull
    private Double valorTotal;

    @NotBlank
    private String status;

    @NotNull
    private List<ItemDTO> itens;

}
