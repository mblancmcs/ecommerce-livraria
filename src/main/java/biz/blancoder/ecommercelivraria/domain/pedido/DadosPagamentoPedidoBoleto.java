package biz.blancoder.ecommercelivraria.domain.pedido;

import biz.blancoder.ecommercelivraria.domain.pedido.paymentsDTO.DadosBoleto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record DadosPagamentoPedidoBoleto(
        @NotNull
        Integer idPedido,
        @NotNull
        @Valid
        DadosBoleto pagamento
) {
}
