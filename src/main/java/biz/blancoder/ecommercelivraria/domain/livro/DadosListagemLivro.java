package biz.blancoder.ecommercelivraria.domain.livro;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosListagemLivro(
        Integer id,
        String titulo,
        Categoria categoria,
        BigDecimal preco,
        String descricao,
        LocalDate dataCadastro
) {

    public DadosListagemLivro(Livro livro) {
        this(livro.getId(), livro.getTitulo(), livro.getCategoria(), livro.getPreco(), livro.getDescricao(), livro.getDataCadastro());
    }

}
