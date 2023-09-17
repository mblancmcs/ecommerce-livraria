package biz.blancoder.ecommercelivraria.domain.pedido;

import biz.blancoder.ecommercelivraria.domain.livroPedido.DadosCadastroLivroPedido;
import biz.blancoder.ecommercelivraria.domain.pedido.paymentsDTO.DadosCartaoCredito;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DadosPagamentoPedidoCartao(
        @NotNull
        Integer idPedido,
        @NotNull
        @Valid
        DadosCartaoCredito pagamento
) {
}
