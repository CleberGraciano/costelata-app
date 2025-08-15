package com.integracaoOmie.integracaoOmie.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.integracaoOmie.integracaoOmie.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Vou Filtrar as requisições HTTP e verificar o token JWT
    // Vou usar o JwtAuthFilter para interceptar as requisições e validar o token

    // // Criei uma configuração de segurança para o Spring Security que define as
    // // sessoes como stateless,
    // // ou seja, não mantém estado entre as requisições.

    // // .requestMatchers("/auth/**",
    // // "/users",
    // // "/v3/api-docs/**",
    // // "/swagger-ui.html",
    // // "/swagger-ui/**",
    // // "/swagger-resources/**",
    // // "/webjars/**")
    // // .hasRole("ADMIN")
    // // .anyRequest().authenticated())
    // // .addFilterBefore(jwtAuthFilter,
    // UsernamePasswordAuthenticationFilter.class)
    // // .sessionManagement(sess ->
    // // sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    // // .build();
    // }
    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService uds) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(uds);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            CustomUserDetailsService uds,
            DaoAuthenticationProvider authProvider) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                // .cors(withDefaults())
                // durante testes, remova ou ajuste o STATELESS
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**",
                                "/swagger-ui.html", "/webjars/**")
                        .permitAll()
                        .requestMatchers("/pedidos/**").authenticated()
                        .requestMatchers("/categorias/**").permitAll()
                        .requestMatchers("/produtos/**").authenticated()
                        .requestMatchers("/users/**").permitAll()
                        .requestMatchers("/auth/test").authenticated()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/omie/**").permitAll()
                        .requestMatchers("/omie/clientes/paginado").permitAll()
                        .requestMatchers("/clientes/**").permitAll()
                        .requestMatchers("/clientes-integracao/paginado").permitAll()
                        .requestMatchers("/transportadoras/**").permitAll()
                        .requestMatchers("/fretes/**").permitAll()
                        .anyRequest().authenticated())
                .authenticationProvider(authProvider)
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        // .httpBasic(withDefaults());
        return http.build();
    }

}