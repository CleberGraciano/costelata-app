package com.integracaoOmie.integracaoOmie.dto.Cliente;

import com.integracaoOmie.integracaoOmie.model.Cliente.Cliente;
import com.integracaoOmie.integracaoOmie.model.Cliente.Cliente.CondicoesDePagamento;
import com.integracaoOmie.integracaoOmie.model.Cliente.Cliente.TipoDeServico;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
// @Builder
@Data
public class ClienteRequestDTO {
    private String idUser;
    @NotBlank(message = "Código de integração é obrigatório")
    private String codigoClienteIntegracao;

    private String codigoClienteOmie;

    // private String nomeAppOpmie;

    @Email(message = "E-mail inválido")
    @NotBlank(message = "E-mail é obrigatório")
    private String email;

    // @NotBlank(message = "Senha é obrigatória")
    // private String senha;

    // @NotBlank(message = "Confirmação de senha é obrigatória")
    // private String confirmarSenha;
    private String cidade;
    private String estado;

    @NotNull(message = "Status é obrigatório")
    private String status;
    @NotNull(message = "Nome Fantasia é obrigatório")
    private String nomeFantasia;

    private String razaoSocial;

    private CondicoesDePagamento condicaoDePagamento;

    private boolean existeAtraso;

    private TipoDeServico tipoDeServico;

    public boolean getExisteAtraso() {
        return existeAtraso;
    }
}
