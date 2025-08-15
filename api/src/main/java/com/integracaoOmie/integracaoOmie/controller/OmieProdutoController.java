package com.integracaoOmie.integracaoOmie.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.integracaoOmie.integracaoOmie.dto.Product.OmieProduto.OmieProdutoResponseDTO;
import com.integracaoOmie.integracaoOmie.service.OmieClientService;
import com.integracaoOmie.integracaoOmie.service.OmieProdutoService;
import com.integracaoOmie.integracaoOmie.utils.PaginatedResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/omie/produtos")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class OmieProdutoController {

    private final OmieProdutoService service;

    private final OmieClientService omieClientService;

    @GetMapping("/buscar")
    public OmieProdutoResponseDTO buscarProdutoPorCodigo(
            @RequestParam("codigo_produto") String codigo,
            @RequestParam("codigo_produto_integracao") String codigoIntegracao) {
        return omieClientService.listarProdutoPorId(codigo, codigoIntegracao);
    }

    @GetMapping
    public List<OmieProdutoResponseDTO> listar() {
        return service.listarProdutos();
    }

    @GetMapping("/{id}")
    public OmieProdutoResponseDTO listarProdutosPorId(@PathVariable Long id) {
        return service.listarProdutoPorId(id);
    }

    @GetMapping("/paginado")
    public PaginatedResponse<OmieProdutoResponseDTO> listarProdutosPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return service.listarProdutosOmiePaginado(page, size);
    }
}
