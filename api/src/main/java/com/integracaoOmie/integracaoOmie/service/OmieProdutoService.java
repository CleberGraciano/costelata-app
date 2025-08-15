package com.integracaoOmie.integracaoOmie.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.integracaoOmie.integracaoOmie.dto.Product.OmieProduto.OmieConsultaProdutoRequest;
import com.integracaoOmie.integracaoOmie.dto.Product.OmieProduto.OmieProdutoResponseDTO;
import com.integracaoOmie.integracaoOmie.utils.PaginatedResponse;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class OmieProdutoService {
    @Autowired
    private Dotenv dotenv;
    private final RestTemplate restTemplate = new RestTemplate();

    public List<OmieProdutoResponseDTO> listarProdutos() {
        OmieConsultaProdutoRequest request = new OmieConsultaProdutoRequest();
        request.setCall("ListarProdutos");
        request.setApp_key(dotenv.get("OMIE_APP_KEY"));
        request.setApp_secret(dotenv.get("OMIE_APP_SECRET"));

        Map<String, Object> param = new HashMap<>();
        param.put("pagina", 1);
        param.put("registros_por_pagina", 50);
        param.put("apenas_importado_api", "N");
        param.put("filtrar_apenas_omiepdv", "N");

        request.setParam(List.of(param));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OmieConsultaProdutoRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://app.omie.com.br/api/v1/geral/produtos/",
                    entity,
                    Map.class);

            Map<String, Object> responseBody = response.getBody();

            if (responseBody == null || !responseBody.containsKey("produto_servico_cadastro")) {
                throw new RuntimeException("Nenhum produto encontrado");
            }

            List<Map<String, Object>> produtos = (List<Map<String, Object>>) responseBody
                    .get("produto_servico_cadastro");

            ObjectMapper mapper = new ObjectMapper();
            return produtos.stream().map(p -> mapper.convertValue(p, OmieProdutoResponseDTO.class)).toList();

        } catch (HttpStatusCodeException e) {
            System.err.println("Erro ao consultar produtos: " + e.getResponseBodyAsString());
            throw new RuntimeException("Erro da Omie: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro inesperado: " + e.getMessage());
        }
    }

    public OmieProdutoResponseDTO listarProdutoPorId(Long codigoProduto) {
        OmieConsultaProdutoRequest request = new OmieConsultaProdutoRequest();
        request.setCall("ConsultarProduto");
        request.setApp_key(dotenv.get("OMIE_APP_KEY"));
        request.setApp_secret(dotenv.get("OMIE_APP_SECRET"));

        Map<String, Object> param = new HashMap<>();
        param.put("codigo_produto", codigoProduto);
        param.put("codigo_produto_integracao", "");
        param.put("codigo", "");

        request.setParam(List.of(param));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OmieConsultaProdutoRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://app.omie.com.br/api/v1/geral/produtos/",
                    entity,
                    Map.class);

            Map<String, Object> responseBody = response.getBody();
            System.out.println("Response Omie: " + responseBody);

            ObjectMapper mapper = new ObjectMapper();

            Object cadastro = responseBody.get("produto_servico_cadastro");
            if (cadastro != null) {
                return mapper.convertValue(cadastro, OmieProdutoResponseDTO.class);
            } else if (responseBody.containsKey("codigo_produto")) {
                return mapper.convertValue(responseBody, OmieProdutoResponseDTO.class);
            } else {
                throw new RuntimeException("Nenhum produto encontrado");
            }

        } catch (HttpStatusCodeException e) {
            System.err.println("Erro ao consultar produtos: " + e.getResponseBodyAsString());
            throw new RuntimeException("Erro da Omie: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro inesperado: " + e.getMessage());
        }
    }

    public PaginatedResponse<OmieProdutoResponseDTO> listarProdutosOmiePaginado(int page, int size) {
        int paginaOmie = page + 1; // Omie começa do 1
        int registrosPorPagina = size;
        OmieConsultaProdutoRequest request = new OmieConsultaProdutoRequest();
        request.setCall("ListarProdutos");
        request.setApp_key(dotenv.get("OMIE_APP_KEY"));
        request.setApp_secret(dotenv.get("OMIE_APP_SECRET"));
        Map<String, Object> param = new HashMap<>();
        param.put("pagina", paginaOmie);
        param.put("registros_por_pagina", registrosPorPagina);
        param.put("apenas_importado_api", "N");
        param.put("filtrar_apenas_omiepdv", "N");
        request.setParam(List.of(param));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OmieConsultaProdutoRequest> entity = new HttpEntity<>(request, headers);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://app.omie.com.br/api/v1/geral/produtos/",
                    entity,
                    Map.class);
            Map<String, Object> responseBody = response.getBody();
            if (responseBody == null || !responseBody.containsKey("produto_servico_cadastro")) {
                return new PaginatedResponse<>(List.of(), page, size, 0, 0);
            }
            List<Map<String, Object>> produtos = (List<Map<String, Object>>) responseBody
                    .get("produto_servico_cadastro");
            ObjectMapper mapper = new ObjectMapper();
            List<OmieProdutoResponseDTO> dtos = produtos.stream()
                    .map(p -> mapper.convertValue(p, OmieProdutoResponseDTO.class)).toList();
            int total = (int) responseBody.getOrDefault("total_de_registros", dtos.size());
            int totalPages = (int) Math.ceil((double) total / size);
            return new PaginatedResponse<>(dtos, page, size, total, totalPages);
        } catch (HttpStatusCodeException e) {
            System.err.println("Erro ao consultar produtos: " + e.getResponseBodyAsString());
            throw new RuntimeException("Erro da Omie: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro inesperado: " + e.getMessage());
        }
    }

}
