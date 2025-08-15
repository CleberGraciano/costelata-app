package com.integracaoOmie.integracaoOmie.repository;

import org.springframework.data.domain.Pageable;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import com.integracaoOmie.integracaoOmie.model.User.User;

public interface UserRepository extends JpaRepository<User, String> {
    // Consulta por e-mail
    // Optional<User> findByEmail(String email);

    // Consulta por CPF
    // Optional<User> findByCpf(String cpf);

    @Query("""
            SELECT u FROM users u
            WHERE (:email IS NULL OR u.email LIKE %:email%)
            AND (:nomeFantasia IS NULL OR u.nomeFantasia LIKE %:nomeFantasia%)
            AND (:cidade IS NULL OR u.cidade LIKE %:cidade%)
            AND (:estado IS NULL OR u.estado LIKE %:estado%)
            AND (:codigoClienteIntegracao IS NULL OR u.codigoClienteIntegracao LIKE %:codigoClienteIntegracao%)
            AND (:codigoClienteOmie IS NULL OR u.codigoClienteOmie LIKE %:codigoClienteOmie%)
            AND (:userRole IS NULL OR u.role = :userRole)
            AND (:status IS NULL OR u.status = :status)
            """)
    Page<User> findByFilters(
            String email,
            String nomeFantasia,
            String cidade,
            String estado,
            String codigoClienteIntegracao,
            String codigoClienteOmie,
            String userRole,
            String status,
            Pageable pageable);

    // Verifica existência (para impedir duplicatas)
    UserDetails findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByCnpj(String cnpj);
}
