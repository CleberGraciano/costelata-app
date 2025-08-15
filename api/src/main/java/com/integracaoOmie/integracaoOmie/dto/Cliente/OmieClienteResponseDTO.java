package com.integracaoOmie.integracaoOmie.dto.Cliente;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.integracaoOmie.integracaoOmie.model.Cliente.Cliente;
import com.integracaoOmie.integracaoOmie.model.Cliente.Cliente.TipoDeServico;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OmieClienteResponseDTO {
    @JsonProperty("codigo_cliente_omie")
    private String codigoClienteOmie;

    @JsonProperty("codigo_cliente_integracao")
    private String codigoClienteIntegracao;

    @JsonProperty("razao_social")
    private String razaoSocial;

    @JsonProperty("nome_fantasia")
    private String nomeFantasia;

    @JsonProperty("cnpj_cpf")
    private String cnpjCpf;

    @JsonProperty("inscricao_estadual")
    private String inscricaoEstadual;

    @JsonProperty("inscricao_municipal")
    private String inscricaoMunicipal;

    @JsonProperty("pessoa_fisica")
    private String pessoaFisica;

    @JsonProperty("endereco")
    private String endereco;
    @JsonProperty("email")
    private String email;

    @JsonProperty("endereco_numero")
    private String enderecoNumero;

    @JsonProperty("complemento")
    private String complemento;

    @JsonProperty("bairro")
    private String bairro;

    @JsonProperty("cidade")
    private String cidade;

    @JsonProperty("cidade_ibge")
    private String cidadeIbge;

    @JsonProperty("estado")
    private String estado;

    @JsonProperty("cep")
    private String cep;

    @JsonProperty("codigo_pais")
    private String codigoPais;

    @JsonProperty("exterior")
    private String exterior;

    @JsonProperty("bloquear_faturamento")
    private String bloquearFaturamento;

    @JsonProperty("enviar_anexos")
    private String enviarAnexos;

    @JsonProperty("inativo")
    private String inativo;

    @JsonProperty("dados_bancarios")
    private DadosBancariosDTO dadosBancarios;

    @JsonProperty("info")
    private InfoDTO info;

    @JsonProperty("recomendacoes")
    private RecomendacoesDTO recomendacoes;

    @JsonProperty("endereco_entrega")
    private EnderecoEntregaDTO enderecoEntrega;

    private List<TagDTO> tags;

    private String status;
    private String condicaoDePagamento;
    private boolean existeAtraso;
    private TipoDeServico tipoDeServico;

    @Data
    public static class DadosBancariosDTO {
        private String agencia;

        @JsonProperty("cChavePix")
        private String cchavePix;

        @JsonProperty("codigo_banco")
        private String codigoBanco;

        @JsonProperty("conta_corrente")
        private String contaCorrente;

        @JsonProperty("doc_titular")
        private String docTitular;

        @JsonProperty("nome_titular")
        private String nomeTitular;

        @JsonProperty("transf_padrao")
        private String transfPadrao;
    }

    @Data
    public static class InfoDTO {
        @JsonProperty("dAlt")
        private String dAlt;

        @JsonProperty("dInc")
        private String dInc;

        @JsonProperty("hAlt")
        private String hAlt;

        @JsonProperty("hInc")
        private String hInc;

        @JsonProperty("uAlt")
        private String uAlt;

        @JsonProperty("uInc")
        private String uInc;

        @JsonProperty("cImpAPI")
        private String cImpAPI;
    }

    @Data
    public static class RecomendacoesDTO {

        @JsonProperty("gerar_boletos")
        private String gerarBoletos;

        @JsonProperty("tipo_assinante")
        private String tipoAssinante;
        @JsonProperty("codigo_vendedor")
        private String codigoVendedor;
    }

    @Data
    public static class EnderecoEntregaDTO {
        // Preencher quando houver dados reais.
        private Map<String, Object> rawData; // Caso queira só armazenar tudo sem mapear agora
    }

    @Data
    public static class TagDTO {
        private String tag;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
