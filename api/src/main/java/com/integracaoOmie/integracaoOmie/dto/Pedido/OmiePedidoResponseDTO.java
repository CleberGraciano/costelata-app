package com.integracaoOmie.integracaoOmie.dto.Pedido;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OmiePedidoResponseDTO {
    private CabecalhoDTO cabecalho;
    private List<ItemDTO> det;
    private FreteDTO frete;
    private TotalPedidoDTO total_pedido;
    // Adicione outros campos conforme necessário

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CabecalhoDTO {
        private String bloqueado;
        private String codigo_cenario_impostos;
        private Long codigo_cliente;
        private Long codigo_empresa;
        private String codigo_parcela;
        private Long codigo_pedido;
        private String codigo_pedido_integracao;
        private String data_previsao;
        private String etapa;
        private String numero_pedido;
        private Integer qtde_parcelas;
        private Integer quantidade_itens;
        // ... outros campos
    }

    @JsonIgnoreProperties(ignoreUnknown = true)

    @Data
    public static class ItemDTO {
        private ProdutoDTO produto;
        // ... outros campos
    }

    @JsonIgnoreProperties(ignoreUnknown = true)

    @Data
    public static class ProdutoDTO {
        private String descricao;
        private String unidade;
        private Double valor_total;
        private Double quantidade;
        // ... outros campos
    }

    @JsonIgnoreProperties(ignoreUnknown = true)

    @Data
    public static class FreteDTO {
        private String especie_volumes;
        private Double peso_bruto;
        private Double peso_liquido;
        private String placa;
        private String placa_estado;
        private Integer quantidade_volumes;
        // ... outros campos
    }

    @JsonIgnoreProperties(ignoreUnknown = true)

    @Data
    public static class TotalPedidoDTO {
        private Double valor_total_pedido;
        private Double valor_mercadorias;
        private Double valor_icms;
        // ... outros campos
    }
}
