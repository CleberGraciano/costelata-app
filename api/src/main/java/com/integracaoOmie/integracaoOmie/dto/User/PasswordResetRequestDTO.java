package com.integracaoOmie.integracaoOmie.dto.User;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class PasswordResetRequestDTO {
    @NotBlank
    @Getter
    private String senhaProvisoria;

    @NotBlank
    @Getter
    private String novaSenha;
}
