
package com.integracaoOmie.integracaoOmie.dto.Frete;

import com.integracaoOmie.integracaoOmie.model.Frete.FreteStatus;

public class FreteDTO {
    private Long id;
    private String nome;
    private String estado;
    private String cidade;
    private Long transportadoraId;
    private Double valorFrete;
    private Integer prazoEntregaDias;
    private Double pedidoMinimo;
    private FreteStatus status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public Long getTransportadoraId() { return transportadoraId; }
    public void setTransportadoraId(Long transportadoraId) { this.transportadoraId = transportadoraId; }
    public Double getValorFrete() { return valorFrete; }
    public void setValorFrete(Double valorFrete) { this.valorFrete = valorFrete; }
    public Integer getPrazoEntregaDias() { return prazoEntregaDias; }
    public void setPrazoEntregaDias(Integer prazoEntregaDias) { this.prazoEntregaDias = prazoEntregaDias; }
    public Double getPedidoMinimo() { return pedidoMinimo; }
    public void setPedidoMinimo(Double pedidoMinimo) { this.pedidoMinimo = pedidoMinimo; }
    public FreteStatus getStatus() { return status; }
    public void setStatus(FreteStatus status) { this.status = status; }

    public static FreteDTO fromEntity(com.integracaoOmie.integracaoOmie.model.Frete.Frete entity) {
        if (entity == null) return null;
        FreteDTO dto = new FreteDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setEstado(entity.getEstado());
        dto.setCidade(entity.getCidade());
        dto.setValorFrete(entity.getValorFrete());
        dto.setPrazoEntregaDias(entity.getPrazoEntregaDias());
        dto.setPedidoMinimo(entity.getPedidoMinimo());
        dto.setStatus(entity.getStatus());
        if (entity.getTransportadora() != null) {
            dto.setTransportadoraId(entity.getTransportadora().getId());
        }
        return dto;
    }

    public com.integracaoOmie.integracaoOmie.model.Frete.Frete toEntity() {
        com.integracaoOmie.integracaoOmie.model.Frete.Frete entity = new com.integracaoOmie.integracaoOmie.model.Frete.Frete();
        entity.setId(this.id);
        entity.setNome(this.nome);
        entity.setEstado(this.estado);
        entity.setCidade(this.cidade);
        entity.setValorFrete(this.valorFrete);
        entity.setPrazoEntregaDias(this.prazoEntregaDias);
        entity.setPedidoMinimo(this.pedidoMinimo);
        // status não é enviado pelo front
        // Transportadora será setada no service
        return entity;
    }
}