package biz.blancoder.ecommercelivraria.domain.livroPedido;

import biz.blancoder.ecommercelivraria.domain.livro.Livro;
import biz.blancoder.ecommercelivraria.domain.pedido.Pedido;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroLivroPedido(
        @NotNull
        Integer quantidade,
        @NotNull
        Livro livro,
        @NotNull
        Pedido pedido
) {
}
