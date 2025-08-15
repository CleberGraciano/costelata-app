package com.integracaoOmie.integracaoOmie.dto.Cliente;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OmieClienteRequestDTO {
    private String codigoClienteIntegracao;
}