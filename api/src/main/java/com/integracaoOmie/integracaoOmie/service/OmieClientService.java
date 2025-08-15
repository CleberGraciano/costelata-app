package com.integracaoOmie.integracaoOmie.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.integracaoOmie.integracaoOmie.dto.Cliente.ClienteResponseDTO;
import com.integracaoOmie.integracaoOmie.dto.Cliente.OmieClienteRequestDTO;
import com.integracaoOmie.integracaoOmie.dto.Cliente.OmieClienteResponseDTO;
import com.integracaoOmie.integracaoOmie.dto.Cliente.SolicitacaoDeCadastroDTO;
import com.integracaoOmie.integracaoOmie.dto.Product.OmieProduto.OmieConsultaProdutoRequest;
import com.integracaoOmie.integracaoOmie.dto.Product.OmieProduto.OmieProdutoResponseDTO;
import com.integracaoOmie.integracaoOmie.model.Cliente.Cliente;
import com.integracaoOmie.integracaoOmie.model.Cliente.OmieConsultaClienteRequest;
import com.integracaoOmie.integracaoOmie.model.Product.Product;
import com.integracaoOmie.integracaoOmie.repository.ClienteRepository;
import com.integracaoOmie.integracaoOmie.specification.SpecificationBuilder;
import com.integracaoOmie.integracaoOmie.utils.PaginatedResponse;
import com.integracaoOmie.integracaoOmie.utils.filters.ClienteFilter;
import com.integracaoOmie.integracaoOmie.utils.filters.ClienteOmieFilter;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Lazy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
// Removido para evitar ciclo de dependência
public class OmieClientService {
    /**
     * Lista todos os clientes existentes no Omie, sem filtros, paginando até o fim.
     * CUIDADO: pode ser lento se houver muitos clientes!
     */
    /**
     * Lista até 'maxResults' clientes existentes no Omie, paginando até atingir o
     * limite.
     * 
     * @param maxResults quantidade máxima de clientes a retornar
     */
    /**
     * Consulta paginada dos clientes existentes no Omie.
     * 
     * @param page página (começa em 0)
     * @param size quantidade de clientes por página
     * @return PaginatedResponse com os clientes da página
     */
    public PaginatedResponse<OmieClienteResponseDTO> listarClientesOmiePaginado(int page, int size) {
        int paginaOmie = page + 1; // Omie começa do 1
        int registrosPorPagina = size;
        Map<String, Object> param = new HashMap<>();
        param.put("pagina", paginaOmie);
        param.put("registros_por_pagina", registrosPorPagina);
        param.put("apenas_importado_api", "N");
        OmieConsultaClienteRequest request = new OmieConsultaClienteRequest();
        request.setCall("ListarClientes");
        request.setApp_key(dotenv.get("OMIE_APP_KEY"));
        request.setApp_secret(dotenv.get("OMIE_APP_SECRET"));
        request.setParam(List.of(param));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OmieConsultaClienteRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://app.omie.com.br/api/v1/geral/clientes/",
                entity,
                Map.class);
        Map<String, Object> responseBody = response.getBody();
        List<Map<String, Object>> clientes = responseBody != null && responseBody.containsKey("clientes_cadastro")
                ? (List<Map<String, Object>>) responseBody.get("clientes_cadastro")
                : Collections.emptyList();
        List<OmieClienteResponseDTO> dtos = clientes.stream().map(this::mapToDTO).collect(Collectors.toList());
        int total = (int) responseBody.getOrDefault("total_de_registros", dtos.size());
        int totalPages = (int) Math.ceil((double) total / size);
        return new PaginatedResponse<>(dtos, page, size, total, totalPages);
    }

    @Autowired
    private Dotenv dotenv;
    @Autowired
    @Lazy
    private ClienteService clienteService;

    private final RestTemplate restTemplate = new RestTemplate();

    public OmieProdutoResponseDTO listarProdutoPorId(String codigo, String codigoIntegracao) {
        OmieConsultaProdutoRequest request = new OmieConsultaProdutoRequest();
        request.setCall("ConsultarProduto");
        request.setApp_key(dotenv.get("OMIE_APP_KEY"));
        request.setApp_secret(dotenv.get("OMIE_APP_SECRET"));

        Map<String, Object> param = new HashMap<>();
        param.put("codigo_produto", codigo);
        param.put("codigo_produto_integracao", codigoIntegracao);
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
            ObjectMapper mapper = new ObjectMapper();
            Object cadastro = responseBody.get("produto_servico_cadastro");
            if (cadastro != null) {
                if (cadastro instanceof List) {
                    List<?> lista = (List<?>) cadastro;
                    if (!lista.isEmpty()) {
                        return mapper.convertValue(lista.get(0), OmieProdutoResponseDTO.class);
                    }
                } else {
                    return mapper.convertValue(cadastro, OmieProdutoResponseDTO.class);
                }
            } else if (responseBody != null && responseBody.containsKey("codigo_produto")) {
                return mapper.convertValue(responseBody, OmieProdutoResponseDTO.class);
            }
            throw new RuntimeException("Nenhum produto encontrado");
        } catch (HttpStatusCodeException e) {
            System.err.println("Erro ao consultar produtos: " + e.getResponseBodyAsString());
            throw new RuntimeException("Erro da Omie: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro inesperado: " + e.getMessage());
        }
    }

    public List<OmieClienteResponseDTO> listarClientes() {

        // Monta a requisição
        OmieConsultaClienteRequest request = new OmieConsultaClienteRequest();
        request.setCall("ListarClientes");
        request.setApp_key(dotenv.get("OMIE_APP_KEY"));
        request.setApp_secret(dotenv.get("OMIE_APP_SECRET"));

        Map<String, Object> emptyParam = new HashMap<>();
        emptyParam.put("pagina", 1);
        emptyParam.put("registros_por_pagina", 5);
        emptyParam.put("apenas_importado_api", "N");

        request.setParam(List.of(emptyParam));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OmieConsultaClienteRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.postForEntity(
                    "https://app.omie.com.br/api/v1/geral/clientes/",
                    entity,
                    (Class<Map<String, Object>>) (Class<?>) Map.class);

            Map<String, Object> responseBody = response.getBody();

            if (responseBody == null || !responseBody.containsKey("clientes_cadastro")) {
                throw new RuntimeException("Nenhum cliente encontrado");
            }

            List<?> clientesRaw = (List<?>) responseBody.get("clientes_cadastro");
            List<Map<String, Object>> clientes = clientesRaw.stream()
                    .filter(Map.class::isInstance)
                    .map(m -> (Map<String, Object>) m)
                    .collect(Collectors.toList());

            // return clientes.stream().map(c -> {
            // OmieClienteResponseDTO dto = new OmieClienteResponseDTO();
            // dto.setRazaoSocial((String) c.get("razao_social"));
            // dto.setCnpjCpf((String) c.get("cnpj_cpf"));
            // dto.setEndereco((String) c.get("endereco"));
            // dto.setTelefone1((String) c.get("telefone1"));
            // return dto;
            // }).toList();
            ObjectMapper mapper = new ObjectMapper();
            return clientes.stream().map(c -> {
                OmieClienteResponseDTO dto = new OmieClienteResponseDTO();

                // dto.setCodigoClienteOmie(((Number)
                // c.get("codigo_cliente_omie")).longValue());
                dto.setCodigoClienteOmie(((String) c.get("codigo_cliente_omie")));
                dto.setCodigoClienteIntegracao((String) c.get("codigo_cliente_integracao"));
                dto.setRazaoSocial((String) c.get("razao_social"));
                dto.setNomeFantasia((String) c.get("nome_fantasia"));
                dto.setCnpjCpf((String) c.get("cnpj_cpf"));
                dto.setInscricaoEstadual((String) c.get("inscricao_estadual"));
                dto.setInscricaoMunicipal((String) c.get("inscricao_municipal"));
                dto.setPessoaFisica((String) c.get("pessoa_fisica"));
                dto.setEndereco((String) c.get("endereco"));
                dto.setEnderecoNumero((String) c.get("endereco_numero"));
                dto.setComplemento((String) c.get("complemento"));
                dto.setBairro((String) c.get("bairro"));
                dto.setCidade((String) c.get("cidade"));
                dto.setCidadeIbge((String) c.get("cidade_ibge"));
                dto.setEstado((String) c.get("estado"));
                dto.setCep((String) c.get("cep"));
                dto.setCodigoPais((String) c.get("codigo_pais"));
                dto.setExterior((String) c.get("exterior"));
                dto.setBloquearFaturamento((String) c.get("bloquear_faturamento"));
                dto.setEnviarAnexos((String) c.get("enviar_anexos"));
                dto.setInativo((String) c.get("inativo"));

                dto.setDadosBancarios(
                        mapper.convertValue(c.get("dadosBancarios"), OmieClienteResponseDTO.DadosBancariosDTO.class));
                dto.setInfo(mapper.convertValue(c.get("info"), OmieClienteResponseDTO.InfoDTO.class));
                dto.setRecomendacoes(
                        mapper.convertValue(c.get("recomendacoes"), OmieClienteResponseDTO.RecomendacoesDTO.class));
                dto.setTags(mapper.convertValue(c.get("tags"), mapper.getTypeFactory()
                        .constructCollectionType(List.class, OmieClienteResponseDTO.TagDTO.class)));
                dto.setEnderecoEntrega(new OmieClienteResponseDTO.EnderecoEntregaDTO()); // placeholder

                return dto;
            }).toList();

        } catch (HttpStatusCodeException e) {
            System.err.println("Erro ao consultar clientes: " + e.getResponseBodyAsString());
            throw new RuntimeException("Erro da Omie: " + e.getResponseBodyAsString());
        }
    }

    public OmieClienteResponseDTO listarClientePorId(String codigo, String codigoIntegracao) {
        // Monta a requisição
        OmieConsultaClienteRequest request = new OmieConsultaClienteRequest();
        request.setCall("ConsultarCliente");
        request.setApp_key(dotenv.get("OMIE_APP_KEY"));
        request.setApp_secret(dotenv.get("OMIE_APP_SECRET"));

        Map<String, Object> param = new HashMap<>();
        // if ("omie".equalsIgnoreCase(tipoCodigo)) {
        // param.put("codigo_cliente_omie", codigo);
        // param.put("codigo_cliente_integracao", "");
        // } else if ("integracao".equalsIgnoreCase(tipoCodigo)) {
        // param.put("codigo_cliente_omie", "");
        // param.put("codigo_cliente_integracao", codigo);
        // } else {
        // throw new IllegalArgumentException("tipoCodigo deve ser 'omie' ou
        // 'integracao'");
        // }
        param.put("codigo_cliente_omie", codigo);
        param.put("codigo_cliente_integracao", codigoIntegracao);
        request.setParam(List.of(param));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OmieConsultaClienteRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://app.omie.com.br/api/v1/geral/clientes/",
                    entity,
                    Map.class);

            Map<String, Object> responseBody = response.getBody();
            ObjectMapper mapper = new ObjectMapper();
            Object cadastro = responseBody.get("clientes_cadastro");
            if (cadastro != null) {
                if (cadastro instanceof List) {
                    List<?> lista = (List<?>) cadastro;
                    if (!lista.isEmpty()) {
                        return mapper.convertValue(lista.get(0), OmieClienteResponseDTO.class);
                    }
                } else if (cadastro instanceof Map) {
                    return mapper.convertValue(cadastro, OmieClienteResponseDTO.class);
                }
            } else if (responseBody != null && responseBody.containsKey("codigo_cliente_omie")) {
                return mapper.convertValue(responseBody, OmieClienteResponseDTO.class);
            }
            throw new RuntimeException("Nenhum cliente encontrado");
        } catch (HttpStatusCodeException e) {
            System.err.println("Erro ao consultar clientes: " + e.getResponseBodyAsString());
            throw new RuntimeException("Erro da Omie: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro inesperado: " + e.getMessage());
        }
    }

    public PaginatedResponse<OmieClienteResponseDTO> consultarOmiePaginado(
            ClienteOmieFilter filter,
            Pageable pageable,
            String cidade,
            String estado,
            String status) {
        // 1. Verifica se há filtro principal
        boolean temFiltro = (filter.getCodigoClienteIntegracao() != null
                && !filter.getCodigoClienteIntegracao().isBlank())
                || (filter.getCodigoClienteOmie() != null && !filter.getCodigoClienteOmie().isBlank())
                || (filter.getEmail() != null && !filter.getEmail().isBlank());

        if (temFiltro) {
            // Busca na base interna
            Cliente cliente = null;
            if (filter.getCodigoClienteIntegracao() != null && !filter.getCodigoClienteIntegracao().isBlank()) {
                cliente = clienteService.findByCodigoClienteIntegracao(filter.getCodigoClienteIntegracao());
            } else if (filter.getCodigoClienteOmie() != null && !filter.getCodigoClienteOmie().isBlank()) {
                cliente = clienteService.findByCodigoClienteOmie(filter.getCodigoClienteOmie());
            } else if (filter.getEmail() != null && !filter.getEmail().isBlank()) {
                cliente = clienteService.getClienteEntityByEmail(filter.getEmail());
            }
            if (cliente != null) {
                // Busca na Omie pelo código
                Map<String, Object> param = new HashMap<>();
                param.put("pagina", 1);
                param.put("registros_por_pagina", 1);
                param.put("apenas_importado_api", "N");
                if (cliente.getCodigoClienteOmie() != null && !cliente.getCodigoClienteOmie().isBlank())
                    param.put("codigo_cliente_omie", cliente.getCodigoClienteOmie());
                // NÃO envie codigo_cliente_integracao para Omie, pois não é aceito na
                // ListarClientes
                OmieConsultaClienteRequest request = new OmieConsultaClienteRequest();
                request.setCall("ListarClientes");
                request.setApp_key(dotenv.get("OMIE_APP_KEY"));
                request.setApp_secret(dotenv.get("OMIE_APP_SECRET"));
                request.setParam(List.of(param));
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<OmieConsultaClienteRequest> entity = new HttpEntity<>(request, headers);
                ResponseEntity<Map> response = restTemplate.postForEntity(
                        "https://app.omie.com.br/api/v1/geral/clientes/",
                        entity,
                        Map.class);
                Map<String, Object> responseBody = response.getBody();
                List<Map<String, Object>> clientes = responseBody != null
                        && responseBody.containsKey("clientes_cadastro")
                                ? (List<Map<String, Object>>) responseBody.get("clientes_cadastro")
                                : Collections.emptyList();
                List<OmieClienteResponseDTO> dtos = clientes.stream().map(this::mapToDTO).collect(Collectors.toList());
                return new PaginatedResponse<>(dtos, 0, dtos.size(), dtos.size(), 1);
            } else {
                return new PaginatedResponse<>(Collections.emptyList(), 0, 0, 0, 0);
            }
        }

        // Se não há filtro, busca apenas uma amostra da Omie
        List<OmieClienteResponseDTO> todosClientes = new ArrayList<>();
        int paginaOmie = 1;
        int registrosPorPagina = pageable.getPageSize(); // usa o size do pageable
        boolean limiteAtingido = false;
        // Busca só a primeira página como amostra
        Map<String, Object> param = new HashMap<>();
        param.put("pagina", paginaOmie);
        param.put("registros_por_pagina", registrosPorPagina);
        param.put("apenas_importado_api", "N");
        OmieConsultaClienteRequest request = new OmieConsultaClienteRequest();
        request.setCall("ListarClientes");
        request.setApp_key(dotenv.get("OMIE_APP_KEY"));
        request.setApp_secret(dotenv.get("OMIE_APP_SECRET"));
        request.setParam(List.of(param));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OmieConsultaClienteRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://app.omie.com.br/api/v1/geral/clientes/",
                entity,
                Map.class);
        Map<String, Object> responseBody = response.getBody();
        List<Map<String, Object>> clientes = responseBody != null && responseBody.containsKey("clientes_cadastro")
                ? (List<Map<String, Object>>) responseBody.get("clientes_cadastro")
                : Collections.emptyList();
        for (Map<String, Object> c : clientes) {
            todosClientes.add(mapToDTO(c));
        }
        // Filtros em memória
        Stream<OmieClienteResponseDTO> stream = todosClientes.stream();
        if (cidade != null && !cidade.isBlank()) {
            stream = stream.filter(
                    dto -> dto.getCidade() != null && dto.getCidade().toLowerCase().contains(cidade.toLowerCase()));
        }
        if (estado != null && !estado.isBlank()) {
            stream = stream.filter(dto -> dto.getEstado() != null && dto.getEstado().equalsIgnoreCase(estado));
        }
        if (filter.getNomeFantasia() != null && !filter.getNomeFantasia().isBlank()) {
            stream = stream.filter(dto -> dto.getNomeFantasia() != null
                    && dto.getNomeFantasia().toLowerCase().contains(filter.getNomeFantasia().toLowerCase()));
        }
        if (filter.getEmail() != null && !filter.getEmail().isBlank()) {
            stream = stream.filter(dto -> dto.getEmail() != null
                    && dto.getEmail().toLowerCase().contains(filter.getEmail().toLowerCase()));
        }
        List<OmieClienteResponseDTO> filtrados = stream.collect(Collectors.toList());
        // Paginação em memória
        int totalElements = filtrados.size();
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, totalElements);
        List<OmieClienteResponseDTO> pagedContent = fromIndex >= totalElements ? Collections.emptyList()
                : filtrados.subList(fromIndex, toIndex);
        int totalPages = (int) Math.ceil((double) totalElements / size);
        return new PaginatedResponse<>(pagedContent, page, size, totalElements, totalPages);
    }

    private OmieClienteResponseDTO mapToDTO(Map<String, Object> c) {
        OmieClienteResponseDTO dto = new OmieClienteResponseDTO();

        // Ajuste seguro do codigo_cliente_omie:
        Object codOmie = c.get("codigo_cliente_omie");
        if (codOmie instanceof String) {
            dto.setCodigoClienteOmie((String) codOmie);
        } else if (codOmie instanceof Long) {
            dto.setCodigoClienteOmie(String.valueOf(codOmie));
        } else {
            dto.setCodigoClienteOmie(null);
        }

        dto.setCodigoClienteIntegracao((String) c.get("codigo_cliente_integracao"));
        dto.setRazaoSocial((String) c.get("razao_social"));
        dto.setNomeFantasia((String) c.get("nome_fantasia"));
        dto.setCnpjCpf((String) c.get("cnpj_cpf"));
        dto.setEstado((String) c.get("estado"));
        dto.setCidade((String) c.get("cidade"));

        String email = (String) c.get("email");
        dto.setEmail(email);

        // Status do cliente
        boolean existe = (email != null && clienteService.existsByEmail(email));
        if (existe) {
            ClienteResponseDTO clienteExistente = clienteService.findByEmail(email);
            dto.setStatus(clienteExistente.getStatus().name());
        } else {
            dto.setStatus("Bloqueado");
        }

        // Adicione outros campos conforme necessário
        return dto;
    }

    public boolean existeNoOmiePorEmail(String email) {
        // Monta a requisição para filtrar por e-mail
        OmieConsultaClienteRequest request = new OmieConsultaClienteRequest();
        request.setCall("ListarClientes");
        request.setApp_key(dotenv.get("OMIE_APP_KEY"));
        request.setApp_secret(dotenv.get("OMIE_APP_SECRET"));

        Map<String, Object> param = new HashMap<>();
        param.put("pagina", 1);
        param.put("registros_por_pagina", 10);
        param.put("apenas_importado_api", "N");
        param.put("email", email);

        request.setParam(List.of(param));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OmieConsultaClienteRequest> entity = new HttpEntity<>(request, headers);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://app.omie.com.br/api/v1/geral/clientes/",
                    entity,
                    Map.class);

            Map<String, Object> responseBody = response.getBody();
            if (responseBody == null || !responseBody.containsKey("clientes_cadastro"))
                return false;

            List<Map<String, Object>> clientes = (List<Map<String, Object>>) responseBody.get("clientes_cadastro");
            if (clientes == null)
                return false;

            // Procura o e-mail EXATAMENTE igual (já que Omie pode trazer por "contém")
            for (Map<String, Object> cliente : clientes) {
                String emailCliente = (String) cliente.get("email");
                if (emailCliente != null && emailCliente.equalsIgnoreCase(email)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            // Trate o erro conforme sua regra de negócio (pode logar)
            return false;
        }
    }

    // try {
    // ResponseEntity<Map> response = restTemplate.postForEntity(
    // "https://app.omie.com.br/api/v1/geral/clientes/",
    // entity,
    // Map.class);

    // Map<String, Object> responseBody = response.getBody();
    // if (responseBody == null)
    // throw new RuntimeException("Resposta vazia ao criar cliente na Omie");

    // // A resposta de inclusão costuma vir em "cliente_cadastro"
    // Object clienteCadastro = responseBody.get("cliente_cadastro");
    // if (clienteCadastro != null) {
    // return new ObjectMapper().convertValue(clienteCadastro,
    // OmieClienteResponseDTO.class);
    // }
    // // Alternativamente, pode vir na raiz
    // if (responseBody.containsKey("codigo_cliente_omie")) {
    // return new ObjectMapper().convertValue(responseBody,
    // OmieClienteResponseDTO.class);
    // }

    // throw new RuntimeException("Erro ao criar cliente na Omie: resposta
    // inesperada");
    // } catch (HttpStatusCodeException e) {
    // System.err.println("Erro ao criar cliente na Omie: " +
    // e.getResponseBodyAsString());
    // throw new RuntimeException("Erro Omie: " + e.getResponseBodyAsString());
    // } catch (Exception e) {
    // e.printStackTrace();
    // throw new RuntimeException("Erro inesperado ao criar cliente na Omie: " +
    // e.getMessage());
    // }
    // }
    public OmieClienteResponseDTO criarClienteNoOmie(SolicitacaoDeCadastroDTO dto) {
        SolicitacaoDeCadastroDTO omieRequest = new SolicitacaoDeCadastroDTO();
        omieRequest.setRazaoSocial(dto.getRazaoSocial());
        omieRequest.setNomeFantasia(dto.getNomeFantasia());
        omieRequest.setEmail(dto.getEmail());

        Map<String, Object> param = new ObjectMapper().convertValue(omieRequest, Map.class);

        Map<String, Object> request = new HashMap<>();
        request.put("call", "IncluirCliente");
        request.put("app_key", dotenv.get("OMIE_APP_KEY"));
        request.put("app_secret", dotenv.get("OMIE_APP_SECRET"));
        request.put("param", List.of(param));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        System.out.println("[DEBUG] Requisição que seria enviada para Omie:");
        System.out.println(entity);
        // Retorno simulado para testes locais
        OmieClienteResponseDTO response = new OmieClienteResponseDTO();
        response.setEmail(dto.getEmail());
        response.setRazaoSocial(dto.getRazaoSocial());
        response.setNomeFantasia(dto.getNomeFantasia());
        // Adicione outros campos simulados conforme necessário
        return response;
    }

}
