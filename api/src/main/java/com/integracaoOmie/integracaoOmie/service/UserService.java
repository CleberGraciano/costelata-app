package com.integracaoOmie.integracaoOmie.service;

import com.integracaoOmie.integracaoOmie.config.TokenService;
import com.integracaoOmie.integracaoOmie.dto.Cliente.OmieClienteResponseDTO;
import com.integracaoOmie.integracaoOmie.dto.User.UserDTO;
import com.integracaoOmie.integracaoOmie.dto.User.UserResponseDTO;
import com.integracaoOmie.integracaoOmie.exception.ResourceNotFoundException;
import com.integracaoOmie.integracaoOmie.model.User.User;
import com.integracaoOmie.integracaoOmie.model.User.UserRole;
import com.integracaoOmie.integracaoOmie.repository.UserRepository;

import jakarta.validation.Valid;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private OmieClientService omieClientService;

    public List<UserResponseDTO> listar(User currentUser) {
        if (currentUser == null) {
            // Retorne todos os usuários, ou o comportamento desejado para requisições sem
            // usuário logado
            List<User> usuarios = repo.findAll();
            return usuarios.stream().map(UserResponseDTO::new).toList();
        }
        if (currentUser.getRole() == UserRole.ADMIN) {
            return repo.findAll()
                    .stream().map(UserResponseDTO::new)
                    .collect(Collectors.toList());
        } else {
            return repo.findAll()
                    .stream()
                    .filter(u -> u.getStatus() == User.Status.Ativo)
                    .map(UserResponseDTO::new)
                    .collect(Collectors.toList());
        }
    }

    public User buscar(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public User buscarPorEmail(String email) {
        UserDetails userDetails = repo.findByEmail(email);
        if (userDetails == null)
            throw new RuntimeException("Usuário não encontrado");
        return (User) userDetails;
    }

    public User salvar(User user) {
        return repo.save(user);
    }

    public UserResponseDTO criar(@Valid @RequestBody User user) {
        for (OmieClienteResponseDTO cliente : omieClientService.listarClientes()) {
            if (cliente.getEmail() != null) {
                System.out.printf("Email %s encontrado na lista de clientes vinda do Omie", cliente.getEmail());
                // Crio Cliente com codigo_cliente_omie e User
            } else {
                System.out.printf("Email %s NÃO encontrado na lista de clientes vinda do Omie, Criar novo",
                        cliente.getEmail());
                // Crio Cliente com codigo_cliente_integracao e User
            }
        }
        // Gera senha provisória e expiração
        String senhaProvisoria = gerarSenhaAleatoria();
        user.setSenhaProvisoria(senhaProvisoria);
        user.setSenhaProvisoriaExpiracao(java.time.LocalDateTime.now().plusDays(3));
        user.setPassword(encoder.encode(senhaProvisoria));
        User salvo = repo.save(user);
        return new UserResponseDTO(salvo);
    }

    public UserResponseDTO renovarSenhaProvisoria(String id) {
        User user = buscar(id);
        String novaSenha = gerarSenhaAleatoria();
        user.setSenhaProvisoria(novaSenha);
        user.setSenhaProvisoriaExpiracao(java.time.LocalDateTime.now().plusDays(3));
        salvar(user);
        return new UserResponseDTO(user);
    }

    private String gerarSenhaAleatoria() {
        final String alfabeto = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        var rnd = new java.security.SecureRandom();
        var sb = new StringBuilder(12);
        for (int i = 0; i < 12; i++)
            sb.append(alfabeto.charAt(rnd.nextInt(alfabeto.length())));
        return sb.toString();
    }

    // public User atualizar(Long id, User user) {
    // // user.setId(id);
    // return repo.save(user);
    // }

    // public void deletar(Long id) {
    // repo.deleteById(id);
    // }
    // public UserResponseDTO criar(UserDTO dto) {
    // User user = new User();
    // user.setNomeFantasia(dto.getNomeFantasia());
    // user.setEmail(dto.getEmail());
    // user.setCnpj(dto.getCnpj());
    // user.setPassword(encoder.encode(dto.getPassword()));
    // User userSalvo = repo.save(user);
    // return new UserResponseDTO(userSalvo.getNomeFantasia(), userSalvo.getEmail(),
    // userSalvo.getCnpj());
    // }

    public User atualizar(@Valid @RequestBody String id, UserDTO dto) {
        User existente = buscar(id);
        existente.setNomeFantasia(dto.getNomeFantasia());
        existente.setEmail(dto.getEmail());
        existente.setCnpj(dto.getCnpj());
        existente.setTelefone(dto.getTelefone());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existente.setPassword(encoder.encode(dto.getPassword()));
        }

        return repo.save(existente);
    }

    public void deletar(String id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado");
        }
        repo.deleteById(id);
    }

    // public void solicitarRecuperacaoSenha(String email) {
    // User usuario = repo.findByEmail(email)
    // .orElseThrow(() -> new ResourceNotFoundException("E-mail não encontrado"));

    // // Gera token JWT curto, só para redefinição de senha (ex: 15min)
    // Token token = tokenService.generateToken(usuario);

    // String urlReset = systemUrl + "/recuperar-senha?token=" + token.getToken();
    // mailService.enviarLinkRecuperacaoSenha(usuario, urlReset);
    // }

    public boolean hasSenhaProvisoria(String id) {
        User user = buscar(id);
        // Indicador deve ser true se senhaProvisoria está preenchida
        return user.getSenhaProvisoria() != null;
    }

    public org.springframework.data.domain.Page<User> filtrarUsuarios(
            String email,
            String nomeFantasia,
            String cidade,
            String estado,
            String codigoClienteIntegracao,
            String codigoClienteOmie,
            String userRole,
            String status,
            org.springframework.data.domain.Pageable pageable,
            User currentUser) {

        // Exemplo simples de filtragem, adapte conforme seu repository
        if (currentUser == null) {
            // Usuário não logado vê todos conforme filtro
            return repo.findByFilters(email, nomeFantasia, cidade, estado, codigoClienteIntegracao,
                    codigoClienteOmie, userRole, status, pageable);
        } else if (currentUser.getRole() == UserRole.ADMIN) {
            // Admin vê todos conforme filtro
            return repo.findByFilters(email, nomeFantasia, cidade, estado, codigoClienteIntegracao,
                    codigoClienteOmie, userRole, status, pageable);
        } else {
            // Usuário comum só vê usuários com status Ativo
            return repo.findByFilters(email, nomeFantasia, cidade, estado, codigoClienteIntegracao,
                    codigoClienteOmie, userRole, "Ativo", pageable);
        }
    }
}
