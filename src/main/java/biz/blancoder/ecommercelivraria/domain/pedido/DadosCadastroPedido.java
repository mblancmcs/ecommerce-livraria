package biz.blancoder.ecommercelivraria.domain.pedido;

import biz.blancoder.ecommercelivraria.domain.livroPedido.DadosCadastroLivroPedido;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DadosCadastroPedido(
        @NotNull @Valid
        List<DadosCadastroLivroPedido> listaLivrosPedidos,
        @NotNull
        Integer idUsuario
) {
}
