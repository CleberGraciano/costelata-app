package com.integracaoOmie.integracaoOmie.controller;

import com.integracaoOmie.integracaoOmie.dto.Pedido.OmiePedidoResponseDTO;
import com.integracaoOmie.integracaoOmie.service.OmiePedidoService;
import com.integracaoOmie.integracaoOmie.utils.PaginatedResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/omie/pedidos")
@RequiredArgsConstructor
public class OmiePedidoController {
    private final OmiePedidoService service;

    @GetMapping("/paginado")
    public PaginatedResponse<OmiePedidoResponseDTO> listarPedidosPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.listarPedidosOmiePaginado(page, size);
    }
}
