package com.integracaoOmie.integracaoOmie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.integracaoOmie.integracaoOmie.dto.Cliente.AlterarStatusRequestDTO;
import com.integracaoOmie.integracaoOmie.dto.Cliente.ClienteRequestDTO;
import com.integracaoOmie.integracaoOmie.dto.Cliente.ClienteResponseDTO;
import com.integracaoOmie.integracaoOmie.dto.Cliente.OmieClienteResponseDTO;
import com.integracaoOmie.integracaoOmie.dto.Cliente.SolicitacaoDeCadastroDTO;
import com.integracaoOmie.integracaoOmie.model.Cliente.Cliente;
import com.integracaoOmie.integracaoOmie.service.ClienteService;
import com.integracaoOmie.integracaoOmie.service.UserService;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/clientes")
// @RequiredArgsConstructor
public class ClienteController {
    @GetMapping("")
    public ResponseEntity<ClienteResponseDTO> buscarPorCodigo(
            @RequestParam(value = "codigo_cliente_integracao", required = false) String codigoIntegracao,
            @RequestParam(value = "codigo_cliente_omie", required = false) String codigoOmie) {
        if (codigoIntegracao != null) {
            ClienteResponseDTO response = clienteService.buscarPorCodigoIntegracao(codigoIntegracao);
            return ResponseEntity.ok(response);
        } else if (codigoOmie != null) {
            ClienteResponseDTO response = clienteService.buscarPorCodigoOmie(codigoOmie);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        ClienteResponseDTO response = clienteService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paginado")
    public ResponseEntity<?> listarPaginado(
            @RequestParam(required = false) String nomeFantasia,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String razaoSocial,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Cliente.Status status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        var clientes = clienteService.filtrarClientes(nomeFantasia, cidade, estado, razaoSocial, email, status,
                pageable);
        return ResponseEntity.ok(clientes);
    }

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/solicitar-cadastro")
    public ClienteResponseDTO solicitarCadastro(@Valid @RequestBody SolicitacaoDeCadastroDTO dto) {
        ClienteResponseDTO response = clienteService.solicitarCadastro(dto);
        return response;
    }

    @PostMapping
    public ClienteResponseDTO criar(@Valid @RequestBody ClienteRequestDTO dto) {
        ClienteResponseDTO response = clienteService.criar(dto);
        return response;
    }

    @PostMapping("/aprovar-omie")
    public ClienteResponseDTO aprovarClienteOmie(@Valid @RequestBody OmieClienteResponseDTO dto) {
        return clienteService.aprovarClientePorEmail(dto.getEmail());
    }

    @PutMapping("/alterar-status")
    public ResponseEntity<ClienteResponseDTO> alterarStatus(@Valid @RequestBody AlterarStatusRequestDTO dto) {
        System.out.println(dto);
        ClienteResponseDTO response = clienteService.alterarStatus(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<ClienteResponseDTO> editarCliente(@PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO dto) {
        ClienteResponseDTO response = clienteService.editarCliente(id, dto);
        return ResponseEntity.ok(response);
    }

}
