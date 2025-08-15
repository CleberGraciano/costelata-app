package com.integracaoOmie.integracaoOmie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.integracaoOmie.integracaoOmie.config.TokenService;
import com.integracaoOmie.integracaoOmie.dto.AuthenticationDTO;
import com.integracaoOmie.integracaoOmie.dto.LoginResponseDTO;
import com.integracaoOmie.integracaoOmie.dto.RegisterDTO;
import com.integracaoOmie.integracaoOmie.model.User.User;
import com.integracaoOmie.integracaoOmie.repository.UserRepository;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO request) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        var auth = this.authManager.authenticate(usernamePassword);
        System.out.println("Login chamado");

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO registerDTO) {
        if (this.userRepository.findByEmail(registerDTO.email()) != null) {

            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(registerDTO.password());
        User newUser = new User(registerDTO.email(), encryptedPassword, registerDTO.role(), registerDTO.status());
        // user.setEmail(registerDTO.email());
        // user.setPassword(encryptedPassword);
        // user.setRole(registerDTO.role());
        this.userRepository.save(newUser);
        System.out.println("Usuário registrado com sucesso: " + registerDTO);
        List<User> todos = userRepository.findAll();
        UserDetails user1 = userRepository.findByEmail(registerDTO.email());
        System.out.println("Usuário encontrado: " + user1.getPassword());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/test")
    public String test() {
        System.out.println("🔍 AuthController ativo!");
        return "AuthController está funcionando!";
    }

    @GetMapping("/prefs")
    public ResponseEntity<?> prefs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Não autenticado.");
        }

        User user = (User) authentication.getPrincipal();
        var response = new java.util.HashMap<String, Object>();
        response.put("email", user.getEmail());
        response.put("role", user.getRole().name());
        response.put("status", user.getStatus().name());
        response.put("senhaProvisoria", user.getSenhaProvisoria());
        response.put("senhaProvisoriaExpiracao", user.getSenhaProvisoriaExpiracao());
        response.put("codigoClienteIntegracao", user.getCodigoClienteIntegracao());
        response.put("codigoClienteOmie", user.getCodigoClienteOmie());
        return ResponseEntity.ok(response);
    }
}
