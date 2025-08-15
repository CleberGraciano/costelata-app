package com.integracaoOmie.integracaoOmie.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integracaoOmie.integracaoOmie.dto.Categoria.CategoriaDTO;
import com.integracaoOmie.integracaoOmie.model.Categoria.Categoria;
import com.integracaoOmie.integracaoOmie.repository.CategoriaRepository;
import com.integracaoOmie.integracaoOmie.dto.Categoria.CategoriaComboDTO;
import com.integracaoOmie.integracaoOmie.model.Categoria.CategoriaStatus;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository repo;

    public List<CategoriaComboDTO> listarCombosAtivos() {
        List<Categoria> categorias = repo.findByStatus(CategoriaStatus.Ativo);
        return categorias.stream()
                .map(cat -> new CategoriaComboDTO(cat.getId(), cat.getDescricao()))
                .toList();
    }

    public org.springframework.data.domain.Page<Categoria> listar(String nome, String status, org.springframework.data.domain.Pageable pageable) {
        org.springframework.data.jpa.domain.Specification<Categoria> spec = (root, query, cb) -> {
            java.util.List<jakarta.persistence.criteria.Predicate> predicates = new java.util.ArrayList<>();
            if (nome != null && !nome.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("descricao")), "%" + nome.toLowerCase() + "%"));
            }
            if (status != null && !status.isEmpty()) {
                predicates.add(cb.equal(root.get("status"), com.integracaoOmie.integracaoOmie.model.Categoria.CategoriaStatus.valueOf(status)));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
        return repo.findAll(spec, pageable);
    }

    public Categoria buscar(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrado"));
    }

    public Categoria criar(Categoria categoria) {
        if (categoria.getStatus() == null) {
            categoria.setStatus(com.integracaoOmie.integracaoOmie.model.Categoria.CategoriaStatus.Ativo);
        }
        Categoria salvo = repo.save(categoria);
        return salvo;
    }

    public Categoria atualizar(Long id, CategoriaDTO categoriaDTO) {
        Categoria existente = buscar(id);
        existente.setDescricao(categoriaDTO.getDescricao());
        if (categoriaDTO.getStatus() != null) {
            existente.setStatus(categoriaDTO.getStatus());
        }
        return repo.save(existente);
    }
    
    public void alterarStatus(Long id, String status) {
        if (!"Ativo".equalsIgnoreCase(status) && !"Bloqueado".equalsIgnoreCase(status)) {
            throw new IllegalArgumentException("Status deve ser Ativo ou Bloqueado");
        }
        Categoria categoria = buscar(id);
        if ("Ativo".equalsIgnoreCase(status)) {
            categoria.setStatus(com.integracaoOmie.integracaoOmie.model.Categoria.CategoriaStatus.Ativo);
        } else {
            categoria.setStatus(com.integracaoOmie.integracaoOmie.model.Categoria.CategoriaStatus.Bloqueado);
        }
        repo.save(categoria);
    }
}
