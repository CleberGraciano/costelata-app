package com.integracaoOmie.integracaoOmie.model.Frete;

import com.integracaoOmie.integracaoOmie.model.Transportadora.Transportadora;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "fretes")
@Data
public class Frete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nome;
    @Column
    private String estado;
    @Column
    private String cidade;
    @ManyToOne
    @JoinColumn(name = "transportadora_id")
    private Transportadora transportadora;
    @Column
    private Double valorFrete;
    @Column
    private Integer prazoEntregaDias;
    @Column
    private Double pedidoMinimo;

    @Enumerated(EnumType.STRING)
    @Column
    private FreteStatus status = FreteStatus.Ativo;
}
