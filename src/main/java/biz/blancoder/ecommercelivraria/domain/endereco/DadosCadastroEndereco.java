package biz.blancoder.ecommercelivraria.domain.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroEndereco(
        @NotBlank
        String logradouro,
        @NotNull
        Integer numero,
        @NotBlank
        String bairro,
        @NotBlank
        String cidade,
        @NotBlank
        String uf,
        @NotNull
        Integer cep,
        String complemento
) {

}
