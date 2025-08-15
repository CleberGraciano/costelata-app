package com.integracaoOmie.integracaoOmie.dto.Cliente;

import com.integracaoOmie.integracaoOmie.model.Cliente.Cliente.Status;
import com.integracaoOmie.integracaoOmie.model.Cliente.Cliente.TipoDeServico;

import lombok.Data;
import com.integracaoOmie.integracaoOmie.model.Cliente.Cliente.CondicoesDePagamento;

@Data

public class ClienteResponseDTO {
    private String idUser;
    private Long id;
    private String codigoClienteIntegracao;
    private String codigoClienteOmie;
    private String razaoSocial;
    private String nomeFantasia;
    private String email;
    private String cidade;
    private String estado;
    private Status status;
    private CondicoesDePagamento condicaoDePagamento;
    private boolean existeAtraso;
    private TipoDeServico tipoDeServico;
    private String senhaProvisoria;
    private java.time.LocalDateTime senhaProvisoriaExpiracao;
}
