package biz.blancoder.ecommercelivraria.domain.usuario;

import biz.blancoder.ecommercelivraria.domain.endereco.DadosEndereco;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizarUsuario(
        @NotNull
        Integer id,
        String nome,
        String telefones,
        DadosEndereco dadosEndereco
) {
}
