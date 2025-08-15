package com.integracaoOmie.integracaoOmie.controller;

import com.integracaoOmie.integracaoOmie.dto.NovaSenha.NovaSenhaDTO;
import com.integracaoOmie.integracaoOmie.dto.User.PasswordResetRequestDTO;
import com.integracaoOmie.integracaoOmie.dto.User.UserDTO;
import com.integracaoOmie.integracaoOmie.dto.User.UserResponseDTO;
import com.integracaoOmie.integracaoOmie.model.User.User;
import com.integracaoOmie.integracaoOmie.model.User.UserRole;
import com.integracaoOmie.integracaoOmie.repository.UserRepository;
import com.integracaoOmie.integracaoOmie.service.UserService; // seu serviço de negócio

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.models.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PutMapping("/{id}/editar")
    public ResponseEntity<UserResponseDTO> editarUsuario(@PathVariable String id, @RequestBody UserDTO dto) {
        User user = service.buscar(id);
        if (dto.getNomeFantasia() != null)
            user.setNomeFantasia(dto.getNomeFantasia());
        if (dto.getEmail() != null)
            user.setEmail(dto.getEmail());
        if (dto.getCnpj() != null)
            user.setCnpj(dto.getCnpj());
        if (dto.getTelefone() != null)
            user.setTelefone(dto.getTelefone());
        if (dto.getRole() != null)
            user.setRole((UserRole) dto.getRole());
        if (dto.getStatus() != null)
            user.setStatus(User.Status.valueOf(((RequestMapping) dto.getStatus()).name()));
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(
                    new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(dto.getPassword()));
        }
        User salvo = service.salvar(user);
        return ResponseEntity.ok(new UserResponseDTO(salvo));
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<UserResponseDTO> inativar(@PathVariable String id) {
        User user = service.buscar(id);
        user.setStatus(User.Status.Bloqueado);
        User salvo = service.salvar(user);
        return ResponseEntity.ok(new UserResponseDTO(salvo));
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<UserResponseDTO> ativar(@PathVariable String id) {
        User user = service.buscar(id);
        user.setStatus(User.Status.Ativo);
        User salvo = service.salvar(user);
        return ResponseEntity.ok(new UserResponseDTO(salvo));
    }

    @Autowired
    private UserService service;

    // @GetMapping
    // public List<UserResponseDTO> all() {
    // org.springframework.security.core.Authentication auth =
    // org.springframework.security.core.context.SecurityContextHolder
    // .getContext().getAuthentication();
    // User currentUser = (User) auth.getPrincipal();
    // return service.listar(currentUser);
    // }
    // @GetMapping
    // public List<UserResponseDTO> all() {
    // org.springframework.security.core.Authentication auth =
    // org.springframework.security.core.context.SecurityContextHolder
    // .getContext().getAuthentication();
    // Object principal = auth.getPrincipal();
    // User currentUser;
    // if (principal instanceof User) {
    // currentUser = (User) principal;
    // } else if (principal instanceof String) {
    // currentUser = service.buscarPorEmail((String) principal); // usa o método do
    // serviço
    // } else {
    // currentUser = null;
    // }
    // return service.listar(currentUser);
    // }
    @GetMapping
    public List<UserResponseDTO> all() {
        return service.listar(null); // ou apenas service.listar() se não precisar do parâmetro
    }

    @PostMapping
    public UserResponseDTO create(@Valid @RequestBody UserDTO dto) {
        User u = new User();
        u.setNomeFantasia(dto.getNomeFantasia());
        u.setEmail(dto.getEmail());
        u.setCnpj(dto.getCnpj());
        u.setTelefone(dto.getTelefone());
        // Gerar senha provisória
        String senhaProvisoria = gerarSenhaAleatoria();
        String encryptedPassword = new BCryptPasswordEncoder().encode(senhaProvisoria);
        u.setPassword(encryptedPassword);
        u.setSenhaProvisoria(senhaProvisoria);
        u.setSenhaProvisoriaExpiracao(java.time.LocalDateTime.now().plusDays(3));
        u.setStatus(User.Status.Bloqueado);
        UserResponseDTO response = service.criar(u);
        response.setSenhaProvisoria(senhaProvisoria);
        return response;
    }

    private String gerarSenhaAleatoria() {
        final String alfabeto = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        var rnd = new java.security.SecureRandom();
        var sb = new StringBuilder(12);
        for (int i = 0; i < 12; i++)
            sb.append(alfabeto.charAt(rnd.nextInt(alfabeto.length())));
        return sb.toString();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable String id) {
        return service.buscar(id);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable String id, @Valid @RequestBody UserDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deletar(id);
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity<?> redefinirSenha(@Valid @RequestBody PasswordResetRequestDTO dto) {
        String id = getLoggedUserId();
        User user = service.buscar(id);
        // Permitir redefinição se senha provisória está correta
        if (user.getSenhaProvisoria() != null && dto.getSenhaProvisoria() != null
                && dto.getSenhaProvisoria().equals(user.getSenhaProvisoria())) {
            String novaSenha = dto.getNovaSenha();
            user.setPassword(new BCryptPasswordEncoder().encode(novaSenha));
            user.setSenhaProvisoria(null);
            user.setSenhaProvisoriaExpiracao(null);
            service.salvar(user);
            Map<String, String> success = new HashMap<>();
            success.put("message", "Senha redefinida com sucesso.");
            return ResponseEntity.ok(success);
        }
        // Permitir redefinição se expirou
        if (user.getSenhaProvisoriaExpiracao() == null
                || user.getSenhaProvisoriaExpiracao().isBefore(java.time.LocalDateTime.now())) {
            String novaSenha = dto.getNovaSenha();
            user.setPassword(new BCryptPasswordEncoder().encode(novaSenha));
            user.setSenhaProvisoria(null);
            user.setSenhaProvisoriaExpiracao(null);
            service.salvar(user);
            Map<String, String> success = new HashMap<>();
            success.put("message", "Senha redefinida com sucesso.");
            return ResponseEntity.ok(success);
        }
        Map<String, String> error = new HashMap<>();
        error.put("error", "A senha provisória informada está incorreta ou não está mais válida.");
        return ResponseEntity.badRequest().body(error);
    }

    @PatchMapping("/{id}/renovar-senha-provisoria")
    public ResponseEntity<UserResponseDTO> renovarSenhaProvisoria(@PathVariable String id) {
        UserResponseDTO userAtualizado = service.renovarSenhaProvisoria(id);
        return ResponseEntity.ok(userAtualizado);
    }

    @GetMapping("/prefs")
    public ResponseEntity<Map<String, Object>> getPrefs() {
        String id = getLoggedUserId();
        User user = service.buscar(id);
        boolean senhaProvisoriaIndicator = service.hasSenhaProvisoria(id);
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("email", user.getEmail());
        prefs.put("senhaProvisoriaIndicator", senhaProvisoriaIndicator);
        prefs.put("role", user.getRole().name());
        prefs.put("status", user.getStatus().name());
        return ResponseEntity.ok(prefs);
    }

    // @GetMapping("/paginado")
    // public ResponseEntity<?> listarPaginado(
    // @RequestParam(required = false) String email,
    // @RequestParam(required = false) String nomeFantasia,
    // @RequestParam(required = false) String cidade,
    // @RequestParam(required = false) String estado,
    // @RequestParam(required = false) String codigoClienteIntegracao,
    // @RequestParam(required = false) String codigoClienteOmie,
    // @RequestParam(required = false) String userRole,
    // @RequestParam(required = false) String status,
    // @RequestParam(defaultValue = "0") int page,
    // @RequestParam(defaultValue = "10") int size) {

    // org.springframework.data.domain.Pageable pageable =
    // org.springframework.data.domain.PageRequest.of(page, size);

    // org.springframework.security.core.Authentication auth =
    // org.springframework.security.core.context.SecurityContextHolder
    // .getContext().getAuthentication();
    // Object principal = auth.getPrincipal();
    // User currentUser;
    // if (principal instanceof User) {
    // currentUser = (User) principal;
    // } else if (principal instanceof String) {
    // currentUser = service.buscarPorEmail((String) principal); // Use o método do
    // seu serviço
    // } else {
    // currentUser = null;
    // }

    // var usuarios = service.filtrarUsuarios(email, nomeFantasia, cidade, estado,
    // codigoClienteIntegracao,
    // codigoClienteOmie, userRole, status, pageable, currentUser);

    // return ResponseEntity.ok(usuarios);
    // }
    @GetMapping("/paginado")
    public ResponseEntity<?> listarPaginado(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String nomeFantasia,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String codigoClienteIntegracao,
            @RequestParam(required = false) String codigoClienteOmie,
            @RequestParam(required = false) String userRole,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);

        var usuarios = service.filtrarUsuarios(email, nomeFantasia, cidade, estado, codigoClienteIntegracao,
                codigoClienteOmie, userRole, status, pageable, null); // passe null para o usuário

        return ResponseEntity.ok(usuarios);
    }

    private String getLoggedUserId() {
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuário não autenticado");
        }
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
}