package com.integracaoOmie.integracaoOmie.controller;

import com.integracaoOmie.integracaoOmie.dto.Frete.FreteDTO;
import com.integracaoOmie.integracaoOmie.dto.Frete.FreteResumoDTO;
import com.integracaoOmie.integracaoOmie.service.FreteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integracaoOmie.integracaoOmie.utils.PaginatedResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")

@RequestMapping("/fretes")
public class FreteController {
    @Autowired
    private FreteService freteService;

    @GetMapping
    public org.springframework.data.domain.Page<FreteDTO> getAll(
            @org.springframework.web.bind.annotation.RequestParam(required = false) String nome,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String status,
            @org.springframework.data.web.PageableDefault(size = 10, sort = "nome") org.springframework.data.domain.Pageable pageable) {
        return freteService.listar(nome, status, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FreteDTO> getById(@PathVariable Long id) {
        return freteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FreteDTO> create(@RequestBody FreteDTO dto) {
        // Não setar status aqui, o service garante o default
        FreteDTO created = freteService.save(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FreteDTO> update(@PathVariable Long id, @RequestBody FreteDTO dto) {
        return freteService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/alterar-status")
    public ResponseEntity<Boolean> alterarStatus(@PathVariable Long id, @RequestParam String status) {
        boolean result = freteService.alterarStatus(id, status);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/resumo")
    public ResponseEntity<List<FreteResumoDTO>> getResumo(
            @RequestParam(required = false) List<Long> ids,
            @RequestParam(required = false) List<Long> transportadoras) {
        List<FreteResumoDTO> resumo = freteService.listarResumo(ids, transportadoras);
        return ResponseEntity.ok(resumo);
    }
}
