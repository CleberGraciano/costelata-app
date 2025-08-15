package com.integracaoOmie.integracaoOmie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.integracaoOmie.integracaoOmie.model.Categoria.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>, JpaSpecificationExecutor<Categoria> {
	java.util.List<Categoria> findByStatus(com.integracaoOmie.integracaoOmie.model.Categoria.CategoriaStatus status);
}
