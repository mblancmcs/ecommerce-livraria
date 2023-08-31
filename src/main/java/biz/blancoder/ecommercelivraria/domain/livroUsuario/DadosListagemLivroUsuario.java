package biz.blancoder.ecommercelivraria.domain.livroUsuario;

import java.time.LocalDateTime;

public record DadosListagemLivroUsuario(
        Integer id,
        LocalDateTime data,
        String perfilUsuario,
        String comentario,
        Integer avaliacao,
        Integer id_usuario,
        Integer id_livro
) {

    public DadosListagemLivroUsuario(LivroUsuario livroUsuario) {
        this(livroUsuario.getId(), livroUsuario.getData(), livroUsuario.getPerfilUsuario(), livroUsuario.getComentario(),
                livroUsuario.getAvaliacao(), livroUsuario.getUsuario().getId(), livroUsuario.getLivro().getId());
    }

}
