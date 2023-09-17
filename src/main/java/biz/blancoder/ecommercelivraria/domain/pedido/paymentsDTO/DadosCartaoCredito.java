package biz.blancoder.ecommercelivraria.domain.pedido.paymentsDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCartaoCredito(
        @NotBlank
        String numero,
        @NotBlank
        String nome,
        @NotNull
        Integer mesExpiracao,
        @NotNull
        Integer anoExpiracao,
        @NotNull
        String cvv,
        @NotNull
        Integer parcelas,
        String tituloCompra
) {
}
