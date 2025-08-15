package com.integracaoOmie.integracaoOmie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.integracaoOmie.integracaoOmie.dto.Categoria.CategoriaDTO;
import com.integracaoOmie.integracaoOmie.model.Categoria.Categoria;
import com.integracaoOmie.integracaoOmie.service.CategoriaService;
import com.integracaoOmie.integracaoOmie.dto.Categoria.CategoriaComboDTO;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:4200")

@RequestMapping("categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService service;

    @GetMapping
    public org.springframework.data.domain.Page<Categoria> all(
            @org.springframework.web.bind.annotation.RequestParam(required = false) String nome,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String status,
            @org.springframework.data.web.PageableDefault(size = 20, sort = "id") org.springframework.data.domain.Pageable pageable) {
        return service.listar(nome, status, pageable);
    }

    @GetMapping("/combo")
    public List<CategoriaComboDTO> comboAtivos() {
        return service.listarCombosAtivos();
    }

    @PostMapping()
    public Categoria criar(@RequestBody @Valid CategoriaDTO categoriaDTO) {
        Categoria categoriaNova = new Categoria();
        categoriaNova.setDescricao(categoriaDTO.getDescricao());
        // Não setar status aqui, o service garante o default
        return service.criar(categoriaNova);
    }

    @GetMapping("/{id}")
    public Categoria findById(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PutMapping("/{id}")
    public Categoria update(@PathVariable Long id, @Valid @RequestBody CategoriaDTO dto) {
        return service.atualizar(id, dto);
    }

    @org.springframework.web.bind.annotation.PatchMapping("/{id}/alterar-status")
    public void alterarStatus(@PathVariable Long id, @org.springframework.web.bind.annotation.RequestParam String status) {
        service.alterarStatus(id, status);
    }

}
