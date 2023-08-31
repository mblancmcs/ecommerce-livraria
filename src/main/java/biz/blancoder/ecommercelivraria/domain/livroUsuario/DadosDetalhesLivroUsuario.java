package biz.blancoder.ecommercelivraria.domain.livroUsuario;

import java.time.LocalDateTime;

public record DadosDetalhesLivroUsuario(
        Integer id,
        LocalDateTime data,
        String perfilUsuario,
        String comentario,
        Integer avaliacao,
        Integer id_usuario,
        Integer id_livro
) {

    public DadosDetalhesLivroUsuario(LivroUsuario livroUsuario) {
        this(livroUsuario.getId(), livroUsuario.getData(), livroUsuario.getPerfilUsuario(), livroUsuario.getComentario(),
                livroUsuario.getAvaliacao(), livroUsuario.getUsuario().getId(), livroUsuario.getLivro().getId());
    }

}
