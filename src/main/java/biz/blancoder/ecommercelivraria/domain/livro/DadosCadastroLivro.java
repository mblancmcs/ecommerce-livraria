package biz.blancoder.ecommercelivraria.domain.livro;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record DadosCadastroLivro(
        @NotBlank
        String titulo,
        @NotNull
        Categoria categoria,
        @NotNull
        BigDecimal preco,
        String descricao,
        @Digits(integer = 13, fraction = 0)
        @NotNull
        Long isbn
) {
}
