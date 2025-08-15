package com.integracaoOmie.integracaoOmie.model.User;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "users")
@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {
    private String telefone;

    public User(String email, String password, UserRole role,
            com.integracaoOmie.integracaoOmie.model.User.UserStatus status) {
        this.email = email;
        this.password = password;
        this.role = role != null ? role : UserRole.USER;
        this.status = status != null ? Status.valueOf(status.name()) : Status.Ativo;
    }

    public enum Status {
        Ativo,
        Bloqueado;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.Ativo;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String nomeFantasia;

    private String email;

    private String cnpj;

    private String password;

    private String codigoClienteIntegracao;
    private String codigoClienteOmie;

    private String senhaProvisoria;
    private String cidade;
    private String estado;

    private java.time.LocalDateTime senhaProvisoriaExpiracao;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    public User(String email, String password, UserRole role, Status status) {
        this.email = email;
        this.password = password;
        this.role = role != null ? role : UserRole.USER;
        this.status = status != null ? status : Status.Ativo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }

    }

    @Override
    public String getUsername() {
        return email;
    }

    public User orElseThrow(Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'orElseThrow'");
    }

}
