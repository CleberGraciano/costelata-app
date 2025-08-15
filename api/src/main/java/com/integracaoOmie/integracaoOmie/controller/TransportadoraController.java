
package com.integracaoOmie.integracaoOmie.controller;
import com.integracaoOmie.integracaoOmie.dto.Transportadora.TransportadoraComboDTO;
import java.util.List;

import com.integracaoOmie.integracaoOmie.dto.Transportadora.TransportadoraDTO;
import com.integracaoOmie.integracaoOmie.service.TransportadoraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integracaoOmie.integracaoOmie.utils.PaginatedResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

@RestController
@RequestMapping("/transportadoras")
public class TransportadoraController {
    @GetMapping("/combo")
    public List<TransportadoraComboDTO> listarCombo() {
        return transportadoraService.listarCombo();
    }
    @Autowired
    private TransportadoraService transportadoraService;

    @GetMapping
    public org.springframework.data.domain.Page<TransportadoraDTO> getAll(
            @org.springframework.web.bind.annotation.RequestParam(required = false) String nome,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String status,
            @org.springframework.data.web.PageableDefault(size = 10, sort = "nome") org.springframework.data.domain.Pageable pageable) {
        return transportadoraService.listar(nome, status, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransportadoraDTO> getById(@PathVariable Long id) {
        return transportadoraService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TransportadoraDTO> create(@RequestBody TransportadoraDTO dto) {
        // Não setar status aqui, o service garante o default
        TransportadoraDTO created = transportadoraService.save(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransportadoraDTO> update(@PathVariable Long id, @RequestBody TransportadoraDTO dto) {
        return transportadoraService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/alterar-status")
    public ResponseEntity<Boolean> alterarStatus(@PathVariable Long id, @RequestParam String status) {
        boolean result = transportadoraService.alterarStatus(id, status);
        return ResponseEntity.ok(result);
    }
}
