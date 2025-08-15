package com.integracaoOmie.integracaoOmie.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integracaoOmie.integracaoOmie.dto.Cliente.OmieClienteResponseDTO;
import com.integracaoOmie.integracaoOmie.service.OmieClientService;
import com.integracaoOmie.integracaoOmie.utils.PaginatedResponse;
import com.integracaoOmie.integracaoOmie.utils.filters.ClienteFilter;
import com.integracaoOmie.integracaoOmie.utils.filters.ClienteOmieFilter;

import org.springframework.data.domain.Sort;

import io.swagger.v3.oas.models.responses.ApiResponse;

@RestController
@CrossOrigin(origins = "http://localhost:4200")

@RequestMapping("/omie/clientes")
@RequiredArgsConstructor
public class OmieClientController {

    private final OmieClientService omieClientService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<OmieClienteResponseDTO>> listarClientes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        PaginatedResponse<OmieClienteResponseDTO> clientes = omieClientService.listarClientesOmiePaginado(page, size);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/buscar")
    public ResponseEntity<OmieClienteResponseDTO> listarClientePorId(
            @RequestParam("codigo_cliente_omie") String codigo,
            @RequestParam("codigo_cliente_integracao") String codigoIntegracao) {
        OmieClienteResponseDTO cliente = omieClientService.listarClientePorId(codigo, codigoIntegracao);
        return ResponseEntity.ok(cliente);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/paginado")
    public PaginatedResponse<OmieClienteResponseDTO> listarPaginado(
            @ModelAttribute ClienteOmieFilter filter,
            Pageable pageable,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String status) {
        System.out.println("Page: " + pageable.getPageNumber());
        System.out.println("PageSize: " + pageable.getPageSize());
        return omieClientService.consultarOmiePaginado(filter, pageable, cidade, estado, status);
    }

}
