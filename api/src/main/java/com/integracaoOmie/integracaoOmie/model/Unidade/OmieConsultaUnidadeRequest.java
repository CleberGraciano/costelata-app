package com.integracaoOmie.integracaoOmie.model.Unidade;

import lombok.Data;

@Data
public class OmieConsultaUnidadeRequest {
    private String call;
    private Object[] param;
    private String app_key;
    private String app_secret;
}
