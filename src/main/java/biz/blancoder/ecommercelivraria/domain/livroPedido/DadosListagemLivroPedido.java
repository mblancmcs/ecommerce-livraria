package biz.blancoder.ecommercelivraria.domain.livroPedido;

import biz.blancoder.ecommercelivraria.domain.livro.DadosListagemLivro;
import biz.blancoder.ecommercelivraria.domain.pedido.DadosListagemPedido;

import java.math.BigDecimal;

public record DadosListagemLivroPedido(
        Integer id,
        BigDecimal precoUnitario,
        Integer quantidade,
        DadosListagemLivro dadosListagemLivro
) {

    public DadosListagemLivroPedido(LivroPedido livroPedido) {
        this(livroPedido.getId(), livroPedido.getPrecoUnitario(), livroPedido.getQuantidade(), livroPedido.retornaListagemLivro());
    }

}
