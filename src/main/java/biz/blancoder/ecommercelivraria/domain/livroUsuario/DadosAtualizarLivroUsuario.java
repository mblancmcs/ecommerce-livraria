package biz.blancoder.ecommercelivraria.domain.livroUsuario;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizarLivroUsuario(
        @NotNull
        Integer id,
        String avaliacao,
        Double nota
) {
}
