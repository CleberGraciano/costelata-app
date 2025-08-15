package com.integracaoOmie.integracaoOmie.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.integracaoOmie.integracaoOmie.dto.Unidade.OmieUnidadeResponseDTO;
import com.integracaoOmie.integracaoOmie.model.Unidade.OmieConsultaUnidadeRequest;
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
public class OmieUnidadeService {
    @Autowired
    private Dotenv dotenv;
    private final RestTemplate restTemplate = new RestTemplate();

    public List<OmieUnidadeResponseDTO> listarUnidades() {
        OmieConsultaUnidadeRequest request = new OmieConsultaUnidadeRequest();
        request.setCall("ListarUnidades");
        request.setParam(new Object[]{new Param()});
        request.setApp_key(dotenv.get("OMIE_APP_KEY"));
        request.setApp_secret(dotenv.get("OMIE_APP_SECRET"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OmieConsultaUnidadeRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://app.omie.com.br/api/v1/geral/unidade/",
                    entity,
                    Map.class);

            Map<String, Object> responseBody = response.getBody();
            if (responseBody == null || !responseBody.containsKey("unidade_cadastro")) {
                return new ArrayList<>();
            }
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> unidades = (List<Map<String, Object>>) responseBody.get("unidade_cadastro");
            List<OmieUnidadeResponseDTO> result = new ArrayList<>();
            for (Map<String, Object> u : unidades) {
                result.add(mapper.convertValue(u, OmieUnidadeResponseDTO.class));
            }
            return result;
        } catch (HttpStatusCodeException e) {
            System.err.println("Erro ao consultar unidades: " + e.getResponseBodyAsString());
            throw new RuntimeException("Erro da Omie: " + e.getResponseBodyAsString());
        }
    }

    private static class Param {
        public String codigo = "";
    }
}
