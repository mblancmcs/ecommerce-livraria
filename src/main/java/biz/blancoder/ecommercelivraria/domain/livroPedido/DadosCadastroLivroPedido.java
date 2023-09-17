package biz.blancoder.ecommercelivraria.domain.livroPedido;

import jakarta.validation.constraints.NotNull;

public record DadosCadastroLivroPedido(
        @NotNull
        Integer quantidade,
        @NotNull
        Integer idLivro
) {
}
