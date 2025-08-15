package com.integracaoOmie.integracaoOmie.service;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.integracaoOmie.integracaoOmie.model.User.User;
import com.integracaoOmie.integracaoOmie.repository.UserRepository;

@Service
@Primary
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    public CustomUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    // @Override
    // public UserDetails loadUserByUsername(String email) throws
    // UsernameNotFoundException {
    // // User user = (User) repo.findByEmail(email)
    // // .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado:
    // " +
    // // email));

    // // return new org.springframework.security.core.userdetails.User(
    // // user.getEmail(),
    // // user.getPassword(),
    // // List.of(new SimpleGrantedAuthority("ROLE_USER")) // ou carregue
    // dinamicamente
    // // );
    // return repo
    // .findByEmail(email);
    // }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = (User) repo.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + email);
        }
        if (user.getStatus() != User.Status.Ativo) {
            throw new UsernameNotFoundException("Usuário bloqueado ou inativo. Entre em contato com o suporte.");
        }
        return user; // seu User deve implementar UserDetails
    }

}
