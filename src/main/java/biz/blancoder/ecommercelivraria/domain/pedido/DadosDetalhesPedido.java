package biz.blancoder.ecommercelivraria.domain.pedido;

import biz.blancoder.ecommercelivraria.domain.livroPedido.DadosListagemLivroPedido;
import biz.blancoder.ecommercelivraria.domain.usuario.DadosListagemUsuario;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record DadosDetalhesPedido(
        Integer id,
        BigDecimal valorTotal,
        LocalDateTime data,
        String status,
        String loginUsuario
) {

    public DadosDetalhesPedido(Pedido pedido) {
        this(pedido.getId(), pedido.getValorTotal(), pedido.getData(), pedido.getStatus(), pedido.getUsuario().getLogin());
    }

}
