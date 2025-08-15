package com.integracaoOmie.integracaoOmie.dto;

import com.integracaoOmie.integracaoOmie.model.User.UserRole;
import com.integracaoOmie.integracaoOmie.model.User.UserStatus;

public record RegisterDTO(String email, String password, UserRole role, UserStatus status) {
    // DTO para registro de usuário
    // Contém os campos necessários para o registro: email, senha e nome
    // Pode ser usado para receber dados de registro em requisições HTTP

}
