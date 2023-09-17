package biz.blancoder.ecommercelivraria.domain.livroPedido;

import biz.blancoder.ecommercelivraria.domain.livro.DadosListagemLivro;
import biz.blancoder.ecommercelivraria.domain.livro.Livro;
import biz.blancoder.ecommercelivraria.domain.pedido.DadosListagemPedido;
import biz.blancoder.ecommercelivraria.domain.pedido.Pedido;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity(name = "LivroPedido")
@Table(name = "livros_pedidos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class LivroPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private BigDecimal precoUnitario = new BigDecimal(0);
    private Integer quantidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_livro")
    private Livro livro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    public LivroPedido(DadosCadastroLivroPedido dados, Pedido pedido, Livro livro) {
        this.quantidade = dados.quantidade();
        this.pedido = pedido;
        this.livro = livro;
        this.precoUnitario = livro.getPreco();
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
