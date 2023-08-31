package biz.blancoder.ecommercelivraria.domain.pedido;

import biz.blancoder.ecommercelivraria.domain.livroPedido.DadosListagemLivroPedido;
import biz.blancoder.ecommercelivraria.domain.livroPedido.LivroPedido;
import biz.blancoder.ecommercelivraria.domain.usuario.DadosListagemUsuario;
import biz.blancoder.ecommercelivraria.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedidos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private BigDecimal valorTotal;
    private LocalDateTime data = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<LivroPedido> livroPedido;

    private Boolean ativo;

    public Pedido(Usuario usuario) {
        this.usuario = usuario;
    }

    private void adicionarLivro(LivroPedido livro) {
        livro.setPedido(this);
        this.livroPedido.add(livro);
        this.valorTotal = this.valorTotal.add(livro.getValorLivros());
    }

    public void exclusaoLogica() {
        this.ativo = false;
    }

    public DadosListagemUsuario retornaListagemUsuario() {
        return new DadosListagemUsuario(this.usuario);
    }

    public String retornaListagemLivrosPedido() {
        String json = "";
        for(LivroPedido livro : this.livroPedido) {
            json += new DadosListagemLivroPedido(livro).toString();
        }
        return json;
    }

}
