package biz.blancoder.ecommercelivraria.domain.livro;

import java.math.BigDecimal;

public record DadosDetalhesLivro(
        Integer id,
        String titulo,
        Categoria categoria,
        BigDecimal preco,
        String descricao
) {

    public DadosDetalhesLivro(Livro livro) {
        this(livro.getId(), livro.getTitulo(), livro.getCategoria(), livro.getPreco(), livro.getDescricao());
    }
}
