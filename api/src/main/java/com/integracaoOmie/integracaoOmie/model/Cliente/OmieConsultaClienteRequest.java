package com.integracaoOmie.integracaoOmie.model.Cliente;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class OmieConsultaClienteRequest {
    private String call;
    private List<Map<String, Object>> param;
    private String app_key;
    private String app_secret;
}