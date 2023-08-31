package biz.blancoder.ecommercelivraria.domain.livro;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DadosAtualizarLivro(
        @NotNull
        Integer id,
        String titulo,
        Categoria categoria,
        BigDecimal preco,
        String descricao
) {

}
