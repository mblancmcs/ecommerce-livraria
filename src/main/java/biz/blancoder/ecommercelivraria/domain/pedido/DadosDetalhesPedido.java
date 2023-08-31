package biz.blancoder.ecommercelivraria.domain.pedido;

import biz.blancoder.ecommercelivraria.domain.usuario.DadosListagemUsuario;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosDetalhesPedido(
        Integer id,
        BigDecimal valorTotal,
        LocalDateTime data,
        String livrosPedido,
        DadosListagemUsuario dadosListagemUsuario
) {

    public DadosDetalhesPedido(Pedido pedido) {
        this(pedido.getId(), pedido.getValorTotal(), pedido.getData(), pedido.retornaListagemLivrosPedido(), pedido.retornaListagemUsuario());
    }

}
