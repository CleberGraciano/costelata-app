package com.integracaoOmie.integracaoOmie.dto.Categoria;

import com.integracaoOmie.integracaoOmie.model.Categoria.CategoriaStatus;
import jakarta.validation.constraints.NotBlank;

public class CategoriaDTO {
    private Long id;
    @NotBlank
    private String descricao;
    private CategoriaStatus status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public CategoriaStatus getStatus() { return status; }
    public void setStatus(CategoriaStatus status) { this.status = status; }

    public static CategoriaDTO fromEntity(com.integracaoOmie.integracaoOmie.model.Categoria.Categoria entity) {
        if (entity == null) return null;
        CategoriaDTO dto = new CategoriaDTO();
        dto.setId(entity.getId());
        dto.setDescricao(entity.getDescricao());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}
