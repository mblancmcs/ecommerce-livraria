package biz.blancoder.ecommercelivraria.domain.livroUsuario;

import biz.blancoder.ecommercelivraria.domain.livro.Livro;
import biz.blancoder.ecommercelivraria.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "LivroUsuario")
@Table(name = "livros_usuarios")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class LivroUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String avaliacao;
    private Double nota;
    private LocalDateTime dataAquisicao = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_livro")
    private Livro livro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private Boolean ativo;

    public LivroUsuario(Usuario usuario, Livro livro) {
        this.dataAquisicao = LocalDateTime.now();
        this.usuario = usuario;
        this.livro = livro;
        this.ativo = true;
    }

    public void atualizarInformacoes(DadosAtualizarLivroUsuario dados) {
        if(dados.avaliacao() != null) {
            this.avaliacao = dados.avaliacao();
        }
        if(dados.nota() != null) {
            this.nota = dados.nota();
        }
    }

    public void exclusaoLogica() {
        this.ativo = false;
    }

}
