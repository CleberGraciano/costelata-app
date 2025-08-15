package com.integracaoOmie.integracaoOmie.repository;

import com.integracaoOmie.integracaoOmie.dto.Frete.FreteResumoDTO;
import com.integracaoOmie.integracaoOmie.model.Frete.Frete;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface FreteRepository extends JpaRepository<Frete, Long>, JpaSpecificationExecutor<Frete> {

    @Query("SELECT new com.integracaoOmie.integracaoOmie.dto.Frete.FreteResumoDTO(f.cidade, f.transportadora) " +
            "FROM Frete f " +
            "WHERE (:ids IS NULL OR f.id IN :ids) " +
            "AND (:transportadoraIds IS NULL OR f.transportadora.id IN :transportadoraIds)")
    List<FreteResumoDTO> findResumoByIdsAndTransportadoraIds(
            @Param("ids") List<Long> ids,
            @Param("transportadoraIds") List<Long> transportadoraIds);

}
