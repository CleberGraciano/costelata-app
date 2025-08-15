package com.integracaoOmie.integracaoOmie.dto.Pedido;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter

public class ItemDTO {
    @NotNull
    private Long produtoId;

    @NotNull
    private Integer quantidade;

    @NotNull
    private Double valorUnitario;

}