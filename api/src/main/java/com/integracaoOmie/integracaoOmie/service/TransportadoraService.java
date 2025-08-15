
package com.integracaoOmie.integracaoOmie.service;

import com.integracaoOmie.integracaoOmie.dto.Transportadora.TransportadoraComboDTO;
import java.util.List;
import java.util.stream.Collectors;

import com.integracaoOmie.integracaoOmie.dto.Transportadora.TransportadoraDTO;
import com.integracaoOmie.integracaoOmie.model.Transportadora.Transportadora;
import com.integracaoOmie.integracaoOmie.repository.TransportadoraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integracaoOmie.integracaoOmie.utils.PaginatedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransportadoraService {

    @Autowired
    private TransportadoraRepository transportadoraRepository;

    public org.springframework.data.domain.Page<TransportadoraDTO> listar(String nome, String status,
            org.springframework.data.domain.Pageable pageable) {
        org.springframework.data.jpa.domain.Specification<Transportadora> spec = (root, query, cb) -> {
            java.util.List<jakarta.persistence.criteria.Predicate> predicates = new java.util.ArrayList<>();
            if (nome != null && !nome.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
            }
            if (status != null && !status.isEmpty()) {
                predicates.add(cb.equal(root.get("status"),
                        com.integracaoOmie.integracaoOmie.model.Transportadora.TransportadoraStatus
                                .valueOf(status)));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
        return transportadoraRepository.findAll(spec, pageable).map(TransportadoraDTO::fromEntity);
    }

    public List<TransportadoraComboDTO> listarCombo() {
    return transportadoraRepository.findAll().stream()
        .filter(t -> t.getStatus() == com.integracaoOmie.integracaoOmie.model.Transportadora.TransportadoraStatus.Ativo)
        .map(t -> new TransportadoraComboDTO(t.getCnpj(), t.getNome())) // cnpj vira codigo, nome vira descricao
        .collect(Collectors.toList());
    }

    public PaginatedResponse<TransportadoraDTO> findAll(Pageable pageable) {
        Page<Transportadora> page = transportadoraRepository.findAll(pageable);
        return new PaginatedResponse<>(
                page.getContent().stream().map(TransportadoraDTO::fromEntity).collect(Collectors.toList()),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }

    public Optional<TransportadoraDTO> findById(Long id) {
        return transportadoraRepository.findById(id)
                .map(TransportadoraDTO::fromEntity);
    }

    public TransportadoraDTO save(TransportadoraDTO dto) {
        Transportadora entity = dto.toEntity();
        if (entity.getStatus() == null) {
            entity.setStatus(com.integracaoOmie.integracaoOmie.model.Transportadora.TransportadoraStatus.Ativo);
        }
        Transportadora saved = transportadoraRepository.save(entity);
        return TransportadoraDTO.fromEntity(saved);
    }

    public Optional<TransportadoraDTO> update(Long id, TransportadoraDTO dto) {
        return transportadoraRepository.findById(id).map(existing -> {
            existing.setNome(dto.getNome());
            existing.setCnpj(dto.getCnpj());
            existing.setInscricaoEstadual(dto.getInscricaoEstadual());
            existing.setEndereco(dto.getEndereco());
            existing.setTelefone(dto.getTelefone());
            Transportadora updated = transportadoraRepository.save(existing);
            return TransportadoraDTO.fromEntity(updated);
        });
    }

    public boolean alterarStatus(Long id, String status) {
        if (!"Ativo".equals(status) && !"Bloqueado".equals(status)) {
            return false;
        }
        Optional<Transportadora> opt = transportadoraRepository.findById(id);
        if (opt.isPresent()) {
            Transportadora t = opt.get();
            t.setStatus(com.integracaoOmie.integracaoOmie.model.Transportadora.TransportadoraStatus.valueOf(status));
            transportadoraRepository.save(t);
            return true;
        }
        return false;
    }
}
