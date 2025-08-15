package com.integracaoOmie.integracaoOmie.controller;

import com.integracaoOmie.integracaoOmie.dto.Cidade.OmieCidadeResponseDTO;
import com.integracaoOmie.integracaoOmie.service.OmieCidadeService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")

@RequestMapping("/omie/cidades")
@RequiredArgsConstructor
public class OmieCidadeController {
    private final OmieCidadeService omieCidadeService;

    @GetMapping
    public List<OmieCidadeResponseDTO> listarCidades(
            @RequestParam(defaultValue = "1") int pagina,
            @RequestParam(defaultValue = "50") int registrosPorPagina,
            @RequestParam(required = false) String uf) {
        return omieCidadeService.listarCidades(pagina, registrosPorPagina, uf);
    }
}
