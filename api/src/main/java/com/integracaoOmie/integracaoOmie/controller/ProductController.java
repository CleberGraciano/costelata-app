package com.integracaoOmie.integracaoOmie.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.integracaoOmie.integracaoOmie.dto.Product.ProductDTO;
import com.integracaoOmie.integracaoOmie.model.Product.Product;
import com.integracaoOmie.integracaoOmie.service.ProductService;
import com.integracaoOmie.integracaoOmie.utils.PaginatedResponse;
import com.integracaoOmie.integracaoOmie.utils.filters.ProductFilter;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@CrossOrigin(origins = "http://localhost:4200")

@RequestMapping("produtos")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping
    public List<Product> all() {
        return service.listar();
    }

    @PostMapping()
    public Product criar(@RequestBody @Valid Product productDTO) {
        Product produtoNovo = new Product();
        produtoNovo.setCodigo(productDTO.getCodigo());
        produtoNovo.setCodigoCategoria(productDTO.getCodigoCategoria());
        produtoNovo.setDescricao(productDTO.getDescricao());
        produtoNovo.setDescricaoDetalhada(productDTO.getDescricaoDetalhada());
        produtoNovo.setNomeCategoria(productDTO.getNomeCategoria());
        produtoNovo.setUnidade(productDTO.getUnidade());
        produtoNovo.setValorUnitario(productDTO.getValorUnitario());
        return service.criar(produtoNovo);
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deletar(id);
    }

    @GetMapping("/paginado")
    public ResponseEntity<PaginatedResponse<ProductDTO>> listarPaginado(
            ProductFilter filter,
            @PageableDefault(size = 10, sort = "descricao", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ProductDTO> page = service.listarPaginado(filter, pageable);

        PaginatedResponse<ProductDTO> resposta = new PaginatedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages());

        return ResponseEntity.ok(resposta);
    }

}
