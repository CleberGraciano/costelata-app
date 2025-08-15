package com.integracaoOmie.integracaoOmie.service;

import com.integracaoOmie.integracaoOmie.dto.Frete.FreteDTO;
import com.integracaoOmie.integracaoOmie.dto.Frete.FreteResumoDTO;
import com.integracaoOmie.integracaoOmie.model.Frete.Frete;
import com.integracaoOmie.integracaoOmie.model.Transportadora.Transportadora;
import com.integracaoOmie.integracaoOmie.repository.FreteRepository;
import com.integracaoOmie.integracaoOmie.repository.TransportadoraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integracaoOmie.integracaoOmie.utils.PaginatedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FreteService {
    @Autowired
    private FreteRepository freteRepository;
    @Autowired
    private TransportadoraRepository transportadoraRepository;

    public PaginatedResponse<FreteDTO> findAll(Pageable pageable) {
        Page<Frete> page = freteRepository.findAll(pageable);
        return new PaginatedResponse<>(
                page.getContent().stream().map(FreteDTO::fromEntity).collect(Collectors.toList()),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }

    public Optional<FreteDTO> findById(Long id) {
        return freteRepository.findById(id)
                .map(FreteDTO::fromEntity);
    }

    public FreteDTO save(FreteDTO dto) {
        Frete entity = dto.toEntity();
        if (entity.getStatus() == null) {
            entity.setStatus(com.integracaoOmie.integracaoOmie.model.Frete.FreteStatus.Ativo);
        }
        if (dto.getTransportadoraId() != null) {
            Optional<Transportadora> t = transportadoraRepository.findById(dto.getTransportadoraId());
            t.ifPresent(entity::setTransportadora);
        }
        Frete saved = freteRepository.save(entity);
        return FreteDTO.fromEntity(saved);
    }

    public Optional<FreteDTO> update(Long id, FreteDTO dto) {
        return freteRepository.findById(id).map(existing -> {
            existing.setNome(dto.getNome());
            existing.setEstado(dto.getEstado());
            existing.setCidade(dto.getCidade());
            existing.setValorFrete(dto.getValorFrete());
            existing.setPrazoEntregaDias(dto.getPrazoEntregaDias());
            existing.setPedidoMinimo(dto.getPedidoMinimo());
            if (dto.getTransportadoraId() != null) {
                Optional<Transportadora> t = transportadoraRepository.findById(dto.getTransportadoraId());
                t.ifPresent(existing::setTransportadora);
            }
            Frete updated = freteRepository.save(existing);
            return FreteDTO.fromEntity(updated);
        });
    }

    public boolean alterarStatus(Long id, String status) {
        if (!"Ativo".equals(status) && !"Bloqueado".equals(status)) {
            return false;
        }
        Optional<Frete> opt = freteRepository.findById(id);
        if (opt.isPresent()) {
            Frete f = opt.get();
            f.setStatus(com.integracaoOmie.integracaoOmie.model.Frete.FreteStatus.valueOf(status));
            freteRepository.save(f);
            return true;
        }
        return false;
    }

    public org.springframework.data.domain.Page<FreteDTO> listar(String nome, String status,
            org.springframework.data.domain.Pageable pageable) {
        org.springframework.data.jpa.domain.Specification<Frete> spec = (root, query, cb) -> {
            java.util.List<jakarta.persistence.criteria.Predicate> predicates = new java.util.ArrayList<>();
            if (nome != null && !nome.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
            }
            if (status != null && !status.isEmpty()) {
                predicates.add(cb.equal(root.get("status"),
                        com.integracaoOmie.integracaoOmie.model.Frete.FreteStatus.valueOf(status.toUpperCase())));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
        return freteRepository.findAll(spec, pageable).map(FreteDTO::fromEntity);
    }

    public List<FreteResumoDTO> listarResumo(List<Long> ids, List<Long> transportadoraIds) {
        if (ids == null || ids.isEmpty() || (ids.size() == 1 && (ids.get(0) == null))) {
            ids = null;
        }
        if (transportadoraIds == null || transportadoraIds.isEmpty() ||
                (transportadoraIds.size() == 1 && transportadoraIds.get(0) == null)) {
            transportadoraIds = null;
        }
        return freteRepository.findResumoByIdsAndTransportadoraIds(ids, transportadoraIds);
    }

}
