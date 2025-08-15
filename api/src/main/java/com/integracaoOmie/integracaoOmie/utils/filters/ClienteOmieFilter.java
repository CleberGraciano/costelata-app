package com.integracaoOmie.integracaoOmie.utils.filters;

import lombok.Data;

@Data
public class ClienteOmieFilter {
    private String codigoClienteOmie;
    private String codigoClienteIntegracao;
    private String razaoSocial;
    private String nomeFantasia;
    private String email;
    private String dataCadastro;
    private String dataAlteracao;
    private String importadoApi;
}