package com.integracaoOmie.integracaoOmie.dto.Cidade;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OmieCidadeResponseDTO {
    @JsonProperty("cCod")
    private String cCod;

    @JsonProperty("cNome")
    private String cNome;

    @JsonProperty("cUF")
    private String cUF;

    @JsonProperty("nCodIBGE")
    private String nCodIBGE;

    @JsonProperty("nCodSIAFI")
    private Integer nCodSIAFI;
}
