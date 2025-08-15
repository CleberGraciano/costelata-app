package com.integracaoOmie.integracaoOmie.dto.Product.OmieProduto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OmieProdutoResponseDTO {
    @JsonProperty("aliquota_cofins")
    private Double aliquotaCofins;

    @JsonProperty("aliquota_ibpt")
    private Double aliquotaIbpt;

    @JsonProperty("aliquota_icms")
    private Double aliquotaIcms;

    @JsonProperty("aliquota_pis")
    private Double aliquotaPis;

    private Double altura;
    private String bloqueado;

    @JsonProperty("bloquear_exclusao")
    private String bloquearExclusao;

    private String cest;
    private String cfop;

    @JsonProperty("codInt_familia")
    private String codIntFamilia;

    private String codigo;

    @JsonProperty("codigo_beneficio")
    private String codigoBeneficio;

    @JsonProperty("codigo_familia")
    private Long codigoFamilia;

    @JsonProperty("codigo_produto")
    private Long codigoProduto;

    @JsonProperty("codigo_produto_integracao")
    private String codigoProdutoIntegracao;

    @JsonProperty("csosn_icms")
    private String csosnIcms;

    @JsonProperty("cst_cofins")
    private String cstCofins;

    @JsonProperty("cst_icms")
    private String cstIcms;

    @JsonProperty("cst_pis")
    private String cstPis;

    @JsonProperty("descr_detalhada")
    private String descrDetalhada;

    private String descricao;

    @JsonProperty("descricao_familia")
    private String descricaoFamilia;

    @JsonProperty("dias_crossdocking")
    private Integer diasCrossdocking;

    @JsonProperty("dias_garantia")
    private Integer diasGarantia;

    private String ean;

    @JsonProperty("estoque_minimo")
    private Integer estoqueMinimo;

    @JsonProperty("exibir_descricao_nfe")
    private String exibirDescricaoNfe;

    @JsonProperty("exibir_descricao_pedido")
    private String exibirDescricaoPedido;

    @JsonProperty("importado_api")
    private String importadoApi;

    private String inativo;
    private InfoDTO info;
    private Double largura;

    @JsonProperty("lead_time")
    private Integer leadTime;

    private String marca;
    private String modelo;

    @JsonProperty("motivo_deson_icms")
    private String motivoDesonIcms;

    private String ncm;

    @JsonProperty("obs_internas")
    private String obsInternas;

    @JsonProperty("per_icms_fcp")
    private Double perIcmsFcp;

    @JsonProperty("peso_bruto")
    private Double pesoBruto;

    @JsonProperty("peso_liq")
    private Double pesoLiq;

    private Double profundidade;

    @JsonProperty("quantidade_estoque")
    private Integer quantidadeEstoque;

    @JsonProperty("recomendacoes_fiscais")
    private RecomendacoesFiscaisDTO recomendacoesFiscais;

    @JsonProperty("red_base_cofins")
    private Double redBaseCofins;

    @JsonProperty("red_base_icms")
    private Double redBaseIcms;

    @JsonProperty("red_base_pis")
    private Double redBasePis;

    private String tipoItem;
    private String unidade;

    @JsonProperty("valor_unitario")
    private Double valorUnitario;

    // CLASSE ANINHADA InfoDTO
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InfoDTO {
        private String dAlt;
        private String dInc;
        private String hAlt;
        private String hInc;
        private String uAlt;
        private String uInc;
    }

    // CLASSE ANINHADA RecomendacoesFiscaisDTO
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RecomendacoesFiscaisDTO {
        @JsonProperty("cnpj_fabricante")
        private String cnpjFabricante;

        @JsonProperty("cupom_fiscal")
        private String cupomFiscal;

        @JsonProperty("id_cest")
        private String idCest;

        @JsonProperty("id_preco_tabelado")
        private Integer idPrecoTabelado;

        @JsonProperty("indicador_escala")
        private String indicadorEscala;

        @JsonProperty("market_place")
        private String marketPlace;

        @JsonProperty("origem_mercadoria")
        private String origemMercadoria;
    }
}
