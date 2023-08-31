package biz.blancoder.ecommercelivraria.domain.livro;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "livros")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String titulo;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    private BigDecimal preco;
    private String descricao;
    private Boolean ativo;

    public Livro(DadosCadastroLivro dados) {
        this.titulo = dados.titulo();
        this.categoria = dados.categoria();
        this.preco = dados.preco();
        this.descricao = dados.descricao();
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
    }

    public void exclusaoLogica() {
        this.ativo = false;
    }

    public DadosListagemLivro retornaListagemLivro() {
        return new DadosListagemLivro(this);
    }

}
