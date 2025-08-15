package com.integracaoOmie.integracaoOmie.dto.Frete;

import org.springframework.boot.rsocket.server.RSocketServer.Transport;

import com.integracaoOmie.integracaoOmie.model.Transportadora.Transportadora;

public class FreteResumoDTO {
    private String cidade;
    private String transportadora;

    // Construtor público exigido pelo Hibernate para JPQL/HQL
    public FreteResumoDTO(String cidade, Transportadora transportadora) {
        this.cidade = cidade;
        this.transportadora = transportadora.getNome();
    }

    public String getCidade() {
        return cidade;
    }

    public String getTransportadora() {
        return transportadora;
    }
}