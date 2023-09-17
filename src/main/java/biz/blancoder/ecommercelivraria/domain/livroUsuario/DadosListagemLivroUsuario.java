package biz.blancoder.ecommercelivraria.domain.livroUsuario;

import java.time.LocalDateTime;

public record DadosListagemLivroUsuario(
        Integer id,
        LocalDateTime dataAquisicao,
        String avaliacao,
        Double nota,
        Integer id_usuario,
        Integer id_livro
) {

    public DadosListagemLivroUsuario(LivroUsuario livroUsuario) {
        this(livroUsuario.getId(), livroUsuario.getDataAquisicao(), livroUsuario.getAvaliacao(), livroUsuario.getNota(),
                livroUsuario.getUsuario().getId(), livroUsuario.getLivro().getId());
    }

}
