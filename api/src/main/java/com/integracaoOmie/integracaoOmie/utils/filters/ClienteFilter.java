package com.integracaoOmie.integracaoOmie.utils.filters;

import lombok.Data;

@Data
public class ClienteFilter {
    private String nomeFantasia;
    private String razaoSocial;
    private String email;
    private String estado;
    private String cidade;
    private String status;
}
