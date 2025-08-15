package com.integracaoOmie.integracaoOmie.dto.Transportadora;

import com.integracaoOmie.integracaoOmie.model.Transportadora.Transportadora;
import com.integracaoOmie.integracaoOmie.model.Transportadora.TransportadoraStatus;
public class TransportadoraDTO {
    private Long id;
    private String nome;
    private String cnpj;
    private String inscricaoEstadual;
    private String endereco;
    private String telefone;
    private TransportadoraStatus status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public String getInscricaoEstadual() { return inscricaoEstadual; }
    public void setInscricaoEstadual(String inscricaoEstadual) { this.inscricaoEstadual = inscricaoEstadual; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public TransportadoraStatus getStatus() { return status; }
    public void setStatus(TransportadoraStatus status) { this.status = status; }

    public static TransportadoraDTO fromEntity(Transportadora entity) {
        if (entity == null) return null;
        TransportadoraDTO dto = new TransportadoraDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setCnpj(entity.getCnpj());
        dto.setInscricaoEstadual(entity.getInscricaoEstadual());
        dto.setEndereco(entity.getEndereco());
        dto.setTelefone(entity.getTelefone());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public Transportadora toEntity() {
        Transportadora entity = new Transportadora();
        entity.setId(this.id);
        entity.setNome(this.nome);
        entity.setCnpj(this.cnpj);
        entity.setInscricaoEstadual(this.inscricaoEstadual);
        entity.setEndereco(this.endereco);
        entity.setTelefone(this.telefone);
        return entity;
    }
}
