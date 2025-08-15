package com.integracaoOmie.integracaoOmie.model.Cidade;

import lombok.Data;

@Data
public class OmieConsultaCidadeRequest {
    private String call;
    private Object[] param;
    private String app_key;
    private String app_secret;
}
