package com.integracaoOmie.integracaoOmie.model.Transportadora;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "transportadoras")
@Data
public class Transportadora {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nome;

    @Column(unique = true)
    private String cnpj;

    private String inscricaoEstadual;

    private String endereco;

    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column
    private TransportadoraStatus status = TransportadoraStatus.Ativo;
}
