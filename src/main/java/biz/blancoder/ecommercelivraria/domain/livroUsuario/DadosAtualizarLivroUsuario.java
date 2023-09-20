package biz.blancoder.ecommercelivraria.domain.livroUsuario;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizarLivroUsuario(
        @NotNull
        Integer id,
        String avaliacao,
        @DecimalMin("0.00")
        @DecimalMax("10.0")
        Double nota
) {
}
