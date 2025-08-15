package com.integracaoOmie.integracaoOmie.dto.Product.OmieProduto;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class OmieConsultaProdutoRequest {
    private String call;
    private String app_key;
    private String app_secret;
    private List<Map<String, Object>> param;
}
