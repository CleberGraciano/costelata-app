package com.integracaoOmie.integracaoOmie.controller;

import com.integracaoOmie.integracaoOmie.dto.Unidade.OmieUnidadeResponseDTO;
import com.integracaoOmie.integracaoOmie.service.OmieUnidadeService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")

@RequestMapping("/omie/unidades")
@RequiredArgsConstructor
public class OmieUnidadeController {
    private final OmieUnidadeService omieUnidadeService;

    @GetMapping
    public List<OmieUnidadeResponseDTO> listarUnidades() {
        return omieUnidadeService.listarUnidades();
    }
}
