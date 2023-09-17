package biz.blancoder.ecommercelivraria.domain.pedido.paymentsDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosBoleto(
        @NotNull
        String banco,
        @Pattern(regexp = "\b[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z\b")
        String dataVencimento,
        String instrucoes) {
}
