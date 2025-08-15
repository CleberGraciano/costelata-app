package com.integracaoOmie.integracaoOmie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.integracaoOmie.integracaoOmie.dto.Pedido.PedidoDTO;
import com.integracaoOmie.integracaoOmie.dto.Pedido.PedidoResponseDTO;
import com.integracaoOmie.integracaoOmie.service.PedidoService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("pedidos")
public class PedidoController {
    @Autowired
    private PedidoService service;

    @GetMapping
    public List<PedidoResponseDTO> listar() {
        return service.listar();
    }

    @PostMapping
    public PedidoResponseDTO criar(@RequestBody @Valid PedidoDTO dto) {
        return service.criar(dto);
    }

    @GetMapping("/{id}")
    public PedidoResponseDTO buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }

    @PutMapping("/{id}")
    public PedidoResponseDTO atualizar(@PathVariable Long id, @RequestBody PedidoDTO dto) {

        return service.atualizar(id, dto);
    }
}
