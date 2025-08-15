package com.integracaoOmie.integracaoOmie.dto.Cliente;

import lombok.Data;

@Data
public class AlterarStatusRequestDTO {
    private Long id;
    // private String codigoClienteOmie;
    // private String codigoClienteIntegracao;
    private String status;
}
