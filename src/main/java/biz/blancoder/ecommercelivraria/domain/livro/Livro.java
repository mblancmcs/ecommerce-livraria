package biz.blancoder.ecommercelivraria.domain.livro;

import biz.blancoder.ecommercelivraria.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity(name = "Livro")
@Table(name = "livros")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String titulo;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    private BigDecimal preco = new BigDecimal(0);
    private String descricao;
    private LocalDate dataCadastro;
    private Long isbn;
    private Boolean ativo;

    public Livro(DadosCadastroLivro dados) {
        this.titulo = dados.titulo();
        this.categoria = dados.categoria();
        this.preco = dados.preco();
        this.descricao = dados.descricao();
        this.dataCadastro = LocalDate.now();
        this.isbn = dados.isbn();
        this.ativo = true;
    }

    public void atualizarInformacoes(DadosAtualizarLivro dados) {
        if(dados.titulo() != null) {
            this.titulo = dados.titulo();
        }
        if(dados.categoria() != null) {
            this.categoria = dados.categoria();
        }
        if(dados.preco() != null) {
            this.preco = dados.preco();
        }
        if(dados.descricao() != null) {
            this.descricao = dados.descricao();
        }
        if(dados.isbn() != null) {
            this.isbn = dados.isbn();
        }
    }

    public void exclusaoLogica() {
        this.ativo = false;
    }

    public DadosListagemLivro retornaListagemLivro() {
        return new DadosListagemLivro(this);
    }

}
