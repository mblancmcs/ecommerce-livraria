package biz.blancoder.ecommercelivraria.domain.pedido.paymentsDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosPix(
        @NotNull
        @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z")
        String dataExpiracao
) {
}
