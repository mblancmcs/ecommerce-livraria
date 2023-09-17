package biz.blancoder.ecommercelivraria.domain.livro;

import biz.blancoder.ecommercelivraria.domain.usuario.Usuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DadosCadastroLivro(
        @NotBlank
        String titulo,
        @NotNull
        Categoria categoria,
        @NotNull
        BigDecimal preco,
        String descricao
) {
}
