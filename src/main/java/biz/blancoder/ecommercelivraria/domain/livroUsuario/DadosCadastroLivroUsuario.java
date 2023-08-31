package biz.blancoder.ecommercelivraria.domain.livroUsuario;

import biz.blancoder.ecommercelivraria.domain.livro.Livro;
import biz.blancoder.ecommercelivraria.domain.usuario.Usuario;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroLivroUsuario(
        String comentario,
        Integer avaliacao,
        @NotNull
        Usuario usuario,
        @NotNull
        Livro livro
) {
}
