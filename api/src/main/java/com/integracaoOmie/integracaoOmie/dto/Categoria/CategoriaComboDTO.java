package com.integracaoOmie.integracaoOmie.dto.Categoria;

public class CategoriaComboDTO {
    private Long id;
    private String descricao;

    public CategoriaComboDTO(Long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
