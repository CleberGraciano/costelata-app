package com.integracaoOmie.integracaoOmie.dto.Cliente;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Data;

@Data
public class SolicitacaoDeCadastroDTO {
    private String email;
    private String razaoSocial;
    private String nomeFantasia;
    private String codigoClienteIntegracao;

}
