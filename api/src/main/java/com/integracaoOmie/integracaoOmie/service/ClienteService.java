package com.integracaoOmie.integracaoOmie.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import com.integracaoOmie.integracaoOmie.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.integracaoOmie.integracaoOmie.dto.Cliente.AlterarStatusRequestDTO;
import com.integracaoOmie.integracaoOmie.dto.Cliente.ClienteRequestDTO;
import com.integracaoOmie.integracaoOmie.dto.Cliente.ClienteResponseDTO;
import com.integracaoOmie.integracaoOmie.dto.Cliente.OmieClienteResponseDTO;
import com.integracaoOmie.integracaoOmie.dto.Cliente.SolicitacaoDeCadastroDTO;
import com.integracaoOmie.integracaoOmie.dto.User.UserResponseDTO;
import com.integracaoOmie.integracaoOmie.model.Cliente.Cliente;
import com.integracaoOmie.integracaoOmie.model.User.User;
import com.integracaoOmie.integracaoOmie.repository.ClienteRepository;
import com.integracaoOmie.integracaoOmie.repository.UserRepository;

@Service
public class ClienteService {
    public ClienteResponseDTO buscarPorCodigoIntegracao(String codigoIntegracao) {
        Cliente cliente = clienteRepository.findByCodigoClienteIntegracao(codigoIntegracao)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado pelo código de integração."));
        ClienteResponseDTO response = new ClienteResponseDTO();
        response.setId(cliente.getId());
        response.setCodigoClienteIntegracao(cliente.getCodigoClienteIntegracao());
        response.setCodigoClienteOmie(cliente.getCodigoClienteOmie());
        response.setEmail(cliente.getEmail());
        response.setStatus(cliente.getStatus());
        response.setCondicaoDePagamento(cliente.getCondicaoDePagamento());
        response.setExisteAtraso(cliente.isExisteAtraso());
        response.setNomeFantasia(cliente.getNomeFantasia());
        response.setRazaoSocial(cliente.getRazaoSocial());
        response.setCidade(cliente.getCidade());
        response.setEstado(cliente.getEstado());
        response.setTipoDeServico(cliente.getTipoDeServico());
        response.setIdUser(cliente.getIdUser());
        return response;
    }

    public ClienteResponseDTO buscarPorCodigoOmie(String codigoOmie) {
        Cliente cliente = clienteRepository.findByCodigoClienteOmie(codigoOmie)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado pelo código Omie."));
        ClienteResponseDTO response = new ClienteResponseDTO();
        response.setId(cliente.getId());
        response.setCodigoClienteIntegracao(cliente.getCodigoClienteIntegracao());
        response.setCodigoClienteOmie(cliente.getCodigoClienteOmie());
        response.setEmail(cliente.getEmail());
        response.setStatus(cliente.getStatus());
        response.setCondicaoDePagamento(cliente.getCondicaoDePagamento());
        response.setExisteAtraso(cliente.isExisteAtraso());
        response.setNomeFantasia(cliente.getNomeFantasia());
        response.setRazaoSocial(cliente.getRazaoSocial());
        response.setCidade(cliente.getCidade());
        response.setEstado(cliente.getEstado());
        response.setTipoDeServico(cliente.getTipoDeServico());
        response.setIdUser(cliente.getIdUser());
        return response;
    }

    public ClienteResponseDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado pelo id."));
        ClienteResponseDTO response = new ClienteResponseDTO();
        response.setId(cliente.getId());
        response.setCodigoClienteIntegracao(cliente.getCodigoClienteIntegracao());
        response.setCodigoClienteOmie(cliente.getCodigoClienteOmie());
        response.setEmail(cliente.getEmail());
        response.setStatus(cliente.getStatus());
        response.setCondicaoDePagamento(cliente.getCondicaoDePagamento());
        response.setExisteAtraso(cliente.isExisteAtraso());
        response.setNomeFantasia(cliente.getNomeFantasia());
        response.setRazaoSocial(cliente.getRazaoSocial());
        response.setCidade(cliente.getCidade());
        response.setEstado(cliente.getEstado());
        response.setTipoDeServico(cliente.getTipoDeServico());
        response.setIdUser(cliente.getIdUser());
        return response;
    }

    public Page<Cliente> filtrarClientes(
            String nomeFantasia,
            String cidade,
            String estado,
            String razaoSocial,
            String email,
            Cliente.Status status,
            Pageable pageable) {
        return clienteRepository.findByFilters(
                nomeFantasia,
                cidade,
                estado,
                razaoSocial,
                email,
                status,
                pageable);
    }

    public Cliente getClienteEntityByEmail(String email) {
        return clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado pelo email."));
    }

    public Cliente findByCodigoClienteIntegracao(String codigo) {
        return clienteRepository.findByCodigoClienteIntegracao(codigo)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado pelo código de integração."));
    }

    public Cliente findByCodigoClienteOmie(String codigoOmie) {
        return clienteRepository.findByCodigoClienteOmie(codigoOmie)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado pelo código Omie."));
    }

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    @Lazy
    private OmieClientService omieClientService;

    public ClienteResponseDTO criar(ClienteRequestDTO dto) {
        // if (dto.getSenha() == null || dto.getConfirmarSenha() == null
        // || !dto.getSenha().equals(dto.getConfirmarSenha())) {
        // throw new RuntimeException("As senhas estão nulas ou não conferem.");
        // }

        Cliente cliente = new Cliente();
        cliente.setCodigoClienteIntegracao(dto.getCodigoClienteIntegracao());
        cliente.setCodigoClienteOmie(dto.getCodigoClienteOmie());
        cliente.setEmail(dto.getEmail());
        cliente.setStatus(Cliente.Status.valueOf(dto.getStatus()));
        cliente.setCondicaoDePagamento(dto.getCondicaoDePagamento());
        cliente.setExisteAtraso(dto.getExisteAtraso());
        cliente.setNomeFantasia(dto.getNomeFantasia());
        cliente.setRazaoSocial(dto.getRazaoSocial());
        cliente.setCidade(dto.getCidade());
        cliente.setEstado(dto.getEstado());
        cliente.setTipoDeServico(dto.getTipoDeServico());
        cliente.setIdUser(dto.getIdUser());

        Cliente salvo = clienteRepository.save(cliente);

        ClienteResponseDTO response = new ClienteResponseDTO();
        response.setId(salvo.getId());
        response.setCodigoClienteIntegracao(salvo.getCodigoClienteIntegracao());
        response.setCodigoClienteOmie(salvo.getCodigoClienteOmie());
        response.setEmail(salvo.getEmail());
        response.setStatus(salvo.getStatus());
        response.setCondicaoDePagamento(salvo.getCondicaoDePagamento());
        response.setExisteAtraso(salvo.isExisteAtraso());
        response.setNomeFantasia(salvo.getNomeFantasia());
        response.setRazaoSocial(salvo.getRazaoSocial());
        response.setCidade(salvo.getCidade());
        response.setEstado(salvo.getEstado());
        response.setTipoDeServico(salvo.getTipoDeServico());
        response.setIdUser(salvo.getIdUser());

        return response;
    }

    public List<Cliente> findClientesNotInEmails(Set<String> emails) {
        if (emails == null || emails.isEmpty()) {
            return clienteRepository.findAll();
        }
        return clienteRepository.findByEmailNotIn(emails);
    }

    public ClienteResponseDTO solicitarCadastro(SolicitacaoDeCadastroDTO dto) {
        // 1) bloqueia duplicidade de e-mail
        if (clienteRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException(
                    "E-mail já cadastrado. Entre em contato com o suporte para aprovar o acesso.");
        }

        // 2) cria cliente Bloqueado e salva para obter o ID
        Cliente cliente = new Cliente();
        cliente.setEmail(dto.getEmail());
        cliente.setStatus(Cliente.Status.Bloqueado);
        cliente.setCondicaoDePagamento(Cliente.CondicoesDePagamento.AVISTA);
        cliente.setExisteAtraso(true);
        cliente.setNomeFantasia(dto.getNomeFantasia());
        cliente.setRazaoSocial(dto.getRazaoSocial());

        cliente = clienteRepository.save(cliente); // ainda dentro da transação

        // 3) monta codigo_cliente_integracao usando o ID (ex: PREFIXO-{id})
        String prefixo = Optional.ofNullable(dto.getCodigoClienteIntegracao()).orElse("");
        String codigoIntegracao = (prefixo.isBlank() ? "" : prefixo + "-") + cliente.getId();
        cliente.setCodigoClienteIntegracao(codigoIntegracao);

        // 4) cria no Omie (se falhar -> exception -> rollback de TUDO)
        SolicitacaoDeCadastroDTO paraOmie = toSolicitacaoDeCadastroDTO(dto, codigoIntegracao);
        OmieClienteResponseDTO criadoNoOmie = omieClientService.criarClienteNoOmie(paraOmie);

        // 5) persiste dados retornados pelo Omie
        cliente.setCodigoClienteOmie(String.valueOf(criadoNoOmie.getCodigoClienteOmie()));
        // se quiser manter exatamente o que o Omie devolveu:
        if (criadoNoOmie.getCodigoClienteIntegracao() != null) {
            cliente.setCodigoClienteIntegracao(criadoNoOmie.getCodigoClienteIntegracao());
        }
        cliente = clienteRepository.save(cliente);

        return toResponse(cliente);
    }

    public boolean existsByEmail(String email) {
        return clienteRepository.existsByEmail(email);
    }

    public ClienteResponseDTO findByEmail(String email) {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        return toDTO(cliente);
    }

    public ClienteResponseDTO aprovarClientePorEmail(String email) {
        // 1) precisa existir internamente
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseGet(() -> {
                    Cliente novo = new Cliente();
                    novo.setEmail(email);
                    novo.setStatus(Cliente.Status.Ativo);
                    novo.setCondicaoDePagamento(Cliente.CondicoesDePagamento.AVISTA);
                    novo.setExisteAtraso(true);
                    novo.setNomeFantasia("");
                    novo.setRazaoSocial("");
                    return clienteRepository.save(novo);
                });

        // 2) se já estiver Ativo, torna idempotente
        if (!Cliente.Status.Ativo.equals(cliente.getStatus())) {
            cliente.setStatus(Cliente.Status.Ativo);
            cliente = clienteRepository.save(cliente);
        }

        // 3) cria User se não existir e define senha aleatória
        if (!userRepository.existsByEmail(cliente.getEmail())) {
            String senhaPlano = gerarSenhaAleatoria();
            String hash = passwordEncoder.encode(senhaPlano);

            User user = new User();
            user.setEmail(cliente.getEmail());
            user.setPassword(hash);
            user.setCodigoClienteIntegracao(cliente.getCodigoClienteIntegracao());
            user.setCodigoClienteOmie(cliente.getCodigoClienteOmie());
            user.setSenhaProvisoria(senhaPlano);
            user.setSenhaProvisoriaExpiracao(java.time.LocalDateTime.now().plusDays(3));
            userRepository.save(user);

            // 4) TODO: enviar e-mail com a senha gerada (ou, preferível, token de criação
            // de senha)

        }

        return toResponse(cliente);
    }

    public ClienteResponseDTO alterarStatus(AlterarStatusRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        cliente.setStatus(Cliente.Status.valueOf(dto.getStatus()));
        Cliente salvo = clienteRepository.save(cliente);

        ClienteResponseDTO response = new ClienteResponseDTO();
        response.setId(salvo.getId());
        response.setCodigoClienteIntegracao(salvo.getCodigoClienteIntegracao());
        response.setCodigoClienteOmie(salvo.getCodigoClienteOmie());
        response.setEmail(salvo.getEmail());
        response.setStatus(salvo.getStatus());
        response.setCondicaoDePagamento(salvo.getCondicaoDePagamento());
        response.setExisteAtraso(salvo.isExisteAtraso());
        System.out.println(response);
        return response;
    }

    private ClienteResponseDTO toDTO(Cliente cliente) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setCodigoClienteIntegracao(cliente.getCodigoClienteIntegracao());
        dto.setCodigoClienteOmie(cliente.getCodigoClienteOmie());
        dto.setEmail(cliente.getEmail());
        dto.setStatus(cliente.getStatus());
        dto.setCondicaoDePagamento(cliente.getCondicaoDePagamento());
        dto.setExisteAtraso(cliente.isExisteAtraso());
        return dto;
    }

    private SolicitacaoDeCadastroDTO toSolicitacaoDeCadastroDTO(OmieClienteResponseDTO dto) {
        SolicitacaoDeCadastroDTO solicitacao = new SolicitacaoDeCadastroDTO();
        solicitacao.setEmail(dto.getEmail());
        solicitacao.setRazaoSocial(dto.getRazaoSocial());
        solicitacao.setNomeFantasia(dto.getNomeFantasia());
        solicitacao.setCodigoClienteIntegracao(dto.getCodigoClienteIntegracao());
        // Adicione outros campos conforme necessário para o cadastro
        return solicitacao;
    }

    private SolicitacaoDeCadastroDTO toSolicitacaoDeCadastroDTO(SolicitacaoDeCadastroDTO base,
            String codigoIntegracao) {
        // clone/mapper simples garantindo que vai o código de integração correto para o
        // Omie
        var out = new SolicitacaoDeCadastroDTO();
        out.setEmail(base.getEmail());
        out.setRazaoSocial(base.getRazaoSocial());
        out.setNomeFantasia(base.getNomeFantasia());
        out.setCodigoClienteIntegracao(codigoIntegracao);
        // mapear demais campos exigidos pelo Omie...
        return out;
    }

    private ClienteResponseDTO toResponse(Cliente c) {
        var r = new ClienteResponseDTO();
        r.setId(c.getId());
        r.setEmail(c.getEmail());
        r.setStatus(c.getStatus());
        r.setCodigoClienteIntegracao(c.getCodigoClienteIntegracao());
        r.setCodigoClienteOmie(c.getCodigoClienteOmie());
        r.setRazaoSocial(c.getRazaoSocial());
        r.setNomeFantasia(c.getNomeFantasia());
        r.setCondicaoDePagamento(c.getCondicaoDePagamento());
        r.setExisteAtraso(c.isExisteAtraso());
        User usuario = (User) userRepository.findByEmail(r.getEmail());
        if (usuario != null) {
            r.setSenhaProvisoria(usuario.getSenhaProvisoria());
            r.setSenhaProvisoriaExpiracao(usuario.getSenhaProvisoriaExpiracao());
        } else {
            r.setSenhaProvisoria(null);
            r.setSenhaProvisoriaExpiracao(null);
        }

        return r;
    }

    private String gerarSenhaAleatoria() {
        // mais robusto que RandomStringUtils
        final String alfabeto = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        var rnd = new SecureRandom();
        var sb = new StringBuilder(12);
        for (int i = 0; i < 12; i++)
            sb.append(alfabeto.charAt(rnd.nextInt(alfabeto.length())));
        return sb.toString();
    }

    public ClienteResponseDTO editarCliente(Long id, ClienteRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado pelo id."));

        cliente.setNomeFantasia(dto.getNomeFantasia());
        cliente.setRazaoSocial(dto.getRazaoSocial());
        cliente.setEmail(dto.getEmail());
        cliente.setCidade(dto.getCidade());
        cliente.setEstado(dto.getEstado());
        cliente.setStatus(Cliente.Status.valueOf(dto.getStatus()));
        cliente.setCondicaoDePagamento(dto.getCondicaoDePagamento());
        if (dto.getCondicaoDePagamento() == Cliente.CondicoesDePagamento.AVISTA) {
            cliente.setExisteAtraso(true);
        } else {
            cliente.setExisteAtraso(false);
        }
        cliente.setTipoDeServico(dto.getTipoDeServico());
        Cliente salvo = clienteRepository.save(cliente);
        return toResponse(salvo);
    }
}
