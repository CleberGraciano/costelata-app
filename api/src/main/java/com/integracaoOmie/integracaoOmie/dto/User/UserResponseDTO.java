package com.integracaoOmie.integracaoOmie.dto.User;

import com.integracaoOmie.integracaoOmie.model.User.User;
import com.integracaoOmie.integracaoOmie.model.User.UserRole;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    private String id;
    private String nomeFantasia;
    private String email;
    private UserRole userRole;
    private String senhaProvisoria;
    private java.time.LocalDateTime senhaProvisoriaExpiracao;
    private String codigoClienteIntegracao;
    private String codigoClienteOmie;
    private String cidade;
    private String estado;
    private String telefone;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.nomeFantasia = user.getNomeFantasia();
        this.email = user.getEmail();
        this.userRole = user.getRole();
        this.senhaProvisoria = user.getSenhaProvisoria();
        this.senhaProvisoriaExpiracao = user.getSenhaProvisoriaExpiracao();
        this.codigoClienteIntegracao = user.getCodigoClienteIntegracao();
        this.codigoClienteOmie = user.getCodigoClienteOmie();
        this.cidade = user.getCidade();
        this.estado = user.getEstado();
        this.telefone = user.getTelefone();
    }

}
