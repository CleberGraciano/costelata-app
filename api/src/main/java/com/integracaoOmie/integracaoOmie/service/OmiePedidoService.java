package com.integracaoOmie.integracaoOmie.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.integracaoOmie.integracaoOmie.dto.Pedido.OmiePedidoResponseDTO;
import com.integracaoOmie.integracaoOmie.utils.PaginatedResponse;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class OmiePedidoService {
    @Autowired
    private Dotenv dotenv;
    private final RestTemplate restTemplate = new RestTemplate();

    public PaginatedResponse<OmiePedidoResponseDTO> listarPedidosOmiePaginado(int page, int size) {
        int paginaOmie = page + 1;
        Map<String, Object> param = new HashMap<>();
        param.put("pagina", paginaOmie);
        param.put("registros_por_pagina", size);
        param.put("apenas_importado_api", "N");
        Map<String, Object> request = new HashMap<>();
        request.put("call", "ListarPedidos");
        request.put("app_key", dotenv.get("OMIE_APP_KEY"));
        request.put("app_secret", dotenv.get("OMIE_APP_SECRET"));
        request.put("param", List.of(param));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://app.omie.com.br/api/v1/produtos/pedido/",
                    entity,
                    Map.class);
            Map<String, Object> responseBody = response.getBody();
            List<Map<String, Object>> pedidos = responseBody != null && responseBody.containsKey("pedido_venda_produto")
                    ? (List<Map<String, Object>>) responseBody.get("pedido_venda_produto")
                    : Collections.emptyList();
            ObjectMapper mapper = new ObjectMapper();
            List<OmiePedidoResponseDTO> dtos = pedidos.stream()
                    .map(p -> mapper.convertValue(p, OmiePedidoResponseDTO.class)).toList();
            int total = (int) responseBody.getOrDefault("total_de_registros", dtos.size());
            int totalPages = (int) Math.ceil((double) total / size);
            return new PaginatedResponse<>(dtos, page, size, total, totalPages);
        } catch (HttpStatusCodeException e) {
            System.err.println("Erro ao consultar pedidos: " + e.getResponseBodyAsString());
            throw new RuntimeException("Erro da Omie: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro inesperado: " + e.getMessage());
        }
    }
}
