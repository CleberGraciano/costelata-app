
package com.integracaoOmie.integracaoOmie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.integracaoOmie.integracaoOmie.dto.Cliente.ClienteResponseDTO;
import com.integracaoOmie.integracaoOmie.model.Cliente.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>, JpaSpecificationExecutor<Cliente> {
    @Query("SELECT c FROM cliente c WHERE "
            + "(:nomeFantasia IS NULL OR LOWER(c.nomeFantasia) LIKE LOWER(CONCAT('%', :nomeFantasia, '%'))) AND "
            + "(:cidade IS NULL OR LOWER(c.cidade) LIKE LOWER(CONCAT('%', :cidade, '%'))) AND "
            + "(:estado IS NULL OR LOWER(c.estado) LIKE LOWER(CONCAT('%', :estado, '%'))) AND "
            + "(:razaoSocial IS NULL OR LOWER(c.razaoSocial) LIKE LOWER(CONCAT('%', :razaoSocial, '%'))) AND "
            + "(:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND "
            + "(:status IS NULL OR c.status = :status)")
    Page<Cliente> findByFilters(
            @Param("nomeFantasia") String nomeFantasia,
            @Param("cidade") String cidade,
            @Param("estado") String estado,
            @Param("razaoSocial") String razaoSocial,
            @Param("email") String email,
            @Param("status") Cliente.Status status,
            Pageable pageable);

    Optional<Cliente> findByCodigoClienteIntegracao(String codigoClienteIntegracao);

    Optional<Cliente> findByCodigoClienteOmie(String codigoClienteOmie);

    boolean existsByEmail(String email);

    Optional<Cliente> findByEmail(String email);

    List<Cliente> findByEmailNotIn(@Param("emails") Set<String> emails);

}
