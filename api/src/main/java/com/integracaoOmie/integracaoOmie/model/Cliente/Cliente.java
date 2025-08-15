package com.integracaoOmie.integracaoOmie.model.Cliente;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "clientes_integracao")
@Entity(name = "cliente")
public class Cliente {
    @Column(name = "id_user")
    private String idUser;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "razao_social", nullable = false)
    private String razaoSocial;
    @Column(name = "nome_fantasia", nullable = false)
    private String nomeFantasia;

    @Column(name = "codigo_cliente_integracao", nullable = true, unique = true)
    private String codigoClienteIntegracao;

    @Column(name = "codigo_cliente_omie")
    private String codigoClienteOmie;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "estado")
    private String estado;

    public enum Status {
        Ativo,
        Bloqueado;
    }

    public enum CondicoesDePagamento {
        AVISTA("À Vista"),
        DIAS7("7 dias"),
        DIAS14("14 dias"),
        DIAS28("28 dias");

        private final String descricao;

        CondicoesDePagamento(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }

        @Override
        public String toString() {
            return descricao;
        }
    }

    public enum TipoDeServico {
        Varejo,
        FoodService;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_de_servico")
    private TipoDeServico tipoDeServico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "condicao_de_pagamento", nullable = false)
    private CondicoesDePagamento condicaoDePagamento = CondicoesDePagamento.AVISTA;

    @Column(name = "existe_atraso", nullable = false)
    private boolean existeAtraso = true;

    public boolean isExisteAtraso() {
        return condicaoDePagamento == CondicoesDePagamento.AVISTA;
    }

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
