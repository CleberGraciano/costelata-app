package com.integracaoOmie.integracaoOmie.dto.NovaSenha;

import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class NovaSenhaDTO {
    private String newPassword;
    private String confirmNewPassword;

    @NotNull
    public String getNewPassword() {
        return newPassword;
    }

    @NotNull
    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public boolean isValid() {
        boolean valid = Objects.nonNull(newPassword) && Objects.nonNull(confirmNewPassword);
        if (valid && newPassword.equalsIgnoreCase(confirmNewPassword)) {
            return true;
        }
        return false;
    }
}
