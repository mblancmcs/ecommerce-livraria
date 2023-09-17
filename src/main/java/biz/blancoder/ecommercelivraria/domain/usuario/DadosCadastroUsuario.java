package biz.blancoder.ecommercelivraria.domain.usuario;

import biz.blancoder.ecommercelivraria.domain.endereco.DadosCadastroEndereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroUsuario(
        @NotBlank
        String nome,
        @NotNull
        Long cpf,
        @Email
        String email,
        @NotBlank
        String telefones,
        @NotNull @Valid
        DadosCadastroEndereco endereco,
        @NotBlank
        String login,
        @NotBlank
        String password,
        @NotNull
        PerfilUsuario perfil
) {
}
