package com.integracaoOmie.integracaoOmie.dto;

public record AuthenticationDTO(String email, String password) {
    // DTO para autenticação de usuário
    // Contém os campos necessários para o login: email e senha
    // Pode ser usado para receber dados de autenticação em requisições HTTP

}
