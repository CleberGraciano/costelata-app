package com.integracaoOmie.integracaoOmie.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.integracaoOmie.integracaoOmie.dto.Cidade.OmieCidadeResponseDTO;
import com.integracaoOmie.integracaoOmie.model.Cidade.OmieConsultaCidadeRequest;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OmieCidadeService {
    @Autowired
    private Dotenv dotenv;
    private final RestTemplate restTemplate = new RestTemplate();

    public List<OmieCidadeResponseDTO> listarCidades(int pagina, int registrosPorPagina, String uf) {
        OmieConsultaCidadeRequest request = new OmieConsultaCidadeRequest();
        request.setCall("PesquisarCidades");
        request.setParam(new Object[]{new Param(pagina, registrosPorPagina, uf)});
        request.setApp_key(dotenv.get("OMIE_APP_KEY"));
        request.setApp_secret(dotenv.get("OMIE_APP_SECRET"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OmieConsultaCidadeRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://app.omie.com.br/api/v1/geral/cidades/",
                    entity,
                    Map.class);

            Map<String, Object> responseBody = response.getBody();
            if (responseBody == null || !responseBody.containsKey("lista_cidades")) {
                return new ArrayList<>();
            }
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> cidades = (List<Map<String, Object>>) responseBody.get("lista_cidades");
            List<OmieCidadeResponseDTO> result = new ArrayList<>();
            for (Map<String, Object> c : cidades) {
                result.add(mapper.convertValue(c, OmieCidadeResponseDTO.class));
            }
            return result;
        } catch (HttpStatusCodeException e) {
            System.err.println("Erro ao consultar cidades: " + e.getResponseBodyAsString());
            throw new RuntimeException("Erro da Omie: " + e.getResponseBodyAsString());
        }
    }

    private static class Param {
        public int pagina;
        public int registros_por_pagina;
        public String filtrar_por_uf;
        public Param(int pagina, int registrosPorPagina, String uf) {
            this.pagina = pagina;
            this.registros_por_pagina = registrosPorPagina;
            this.filtrar_por_uf = (uf != null && !uf.isBlank()) ? uf : null;
        }
    }
}
