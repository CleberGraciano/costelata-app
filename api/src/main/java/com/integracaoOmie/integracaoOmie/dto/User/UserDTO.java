package com.integracaoOmie.integracaoOmie.dto.User;

import org.springframework.beans.factory.annotation.Autowired;

import com.integracaoOmie.integracaoOmie.model.User.UserRole;
import com.integracaoOmie.integracaoOmie.model.User.UserStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {
    private String telefone;
    @Autowired
    private UserRole role;
    @Autowired
    private UserStatus status;

    @NotBlank(message = "Nome é obrigatório")
    private String nomeFantasia;

    @Email(message = "E-mail deve ser válido")
    @NotBlank(message = "E-mail é obrigatório")
    private String email;

    // @CNPJ(message = "CNPJ inválido")
    private String cnpj;

    private String password;

    private String codigoClienteIntegracao;
    private String codigoClienteOmie;
    private String cidade;
    private String estado;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getCodigoClienteIntegracao() {
        return codigoClienteIntegracao;
    }

    public void setCodigoClienteIntegracao(String codigoClienteIntegracao) {
        this.codigoClienteIntegracao = codigoClienteIntegracao;
    }

    public String getCodigoClienteOmie() {
        return codigoClienteOmie;
    }

    public void setCodigoClienteOmie(String codigoClienteOmie) {
        this.codigoClienteOmie = codigoClienteOmie;
    }

    public Object getRole() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRole'");
    }

    public Object getStatus() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStatus'");
    }

    public String getTelefone() {
        return this.telefone;
    }
}
