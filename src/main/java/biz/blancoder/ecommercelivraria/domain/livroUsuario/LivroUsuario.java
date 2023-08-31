package biz.blancoder.ecommercelivraria.domain.livroUsuario;

import biz.blancoder.ecommercelivraria.domain.livro.Livro;
import biz.blancoder.ecommercelivraria.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "livros_usuarios")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class LivroUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String comentario;
    private Integer avaliacao;
    private LocalDateTime data = LocalDateTime.now();
    private String perfilUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_livro")
    private Livro livro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private Boolean ativo;

    public LivroUsuario(DadosCadastroLivroUsuario dados) {
        if(dados.comentario() != null) {
            this.comentario = dados.comentario();
        }
        if(dados.avaliacao() != null) {
            this.avaliacao = dados.avaliacao();
        }
        this.data = LocalDateTime.now();
        this.perfilUsuario = dados.usuario().getPerfil();
        this.usuario = dados.usuario();
        this.livro = dados.livro();
    }

    public void atualizarInformacoes(DadosAtualizarLivroUsuario dados) {
        if(dados.avaliacao() != null) {
            this.avaliacao = dados.avaliacao();
        }
        if(dados.comentario() != null) {
            this.comentario = dados.comentario();
        }
    }

    public void exclusaoLogica() {
        this.ativo = false;
    }

}
