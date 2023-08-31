package biz.blancoder.ecommercelivraria.domain.livroPedido;

import biz.blancoder.ecommercelivraria.domain.livro.DadosListagemLivro;
import biz.blancoder.ecommercelivraria.domain.livro.Livro;
import biz.blancoder.ecommercelivraria.domain.pedido.DadosListagemPedido;
import biz.blancoder.ecommercelivraria.domain.pedido.Pedido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "livros_pedidos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class LivroPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private BigDecimal precoUnitario;
    private Integer quantidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_livro")
    private Livro livro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    public LivroPedido(DadosCadastroLivroPedido dados) {
        this.quantidade = dados.quantidade();
        this.pedido = dados.pedido();
        this.livro = dados.livro();
        this.precoUnitario = dados.livro().getPreco();
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public BigDecimal getValorLivros() {
        return precoUnitario.multiply(new BigDecimal(quantidade));
    }

    public DadosListagemPedido retornaListagemPedido() {
        return new DadosListagemPedido(this.pedido);
    }

    public DadosListagemLivro retornaListagemLivro() {
        return new DadosListagemLivro(this.livro);
    }

}
