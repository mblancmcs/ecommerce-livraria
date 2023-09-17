package biz.blancoder.ecommercelivraria.domain.pedido;

import biz.blancoder.ecommercelivraria.domain.livroPedido.DadosCadastroLivroPedido;
import biz.blancoder.ecommercelivraria.domain.pedido.paymentsDTO.DadosPix;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DadosPagamentoPedidoPix(
        @NotNull
        Integer idPedido,
        @NotNull
        @Valid
        DadosPix pagamento
) {
}
