package com.integracaoOmie.integracaoOmie.repository;

import com.integracaoOmie.integracaoOmie.model.Transportadora.Transportadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransportadoraRepository extends JpaRepository<Transportadora, Long>, JpaSpecificationExecutor<Transportadora> {
}
