package biz.blancoder.ecommercelivraria.domain.pedido;

import biz.blancoder.ecommercelivraria.domain.livroPedido.DadosCadastroLivroPedido;
import biz.blancoder.ecommercelivraria.domain.livroPedido.DadosListagemLivroPedido;
import biz.blancoder.ecommercelivraria.domain.livroPedido.LivroPedido;
import biz.blancoder.ecommercelivraria.domain.usuario.DadosListagemUsuario;
import biz.blancoder.ecommercelivraria.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Pedido")
@Table(name = "pedidos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String idGateway;
    private BigDecimal valorTotal = new BigDecimal(0);
    private LocalDateTime data = LocalDateTime.now();
    private String status = "aguardando pagamento";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<LivroPedido> livroPedido = new ArrayList();

    private Boolean ativo;

    public Pedido(Usuario usuario) {
        this.usuario = usuario;
        this.ativo = true;
    }

    public void adicionarLivro(LivroPedido livro) {
        this.livroPedido.add(livro);
        this.valorTotal = this.valorTotal.add(livro.getValorLivros());
    }

    public void atualizarInformacoes(String idGateway, String status) {
        if(idGateway != null) {
            this.idGateway = idGateway;
        }
        if(status != null) {
            if(status.equals("pending")) {
                this.status = "pendente";
            } else if(status.equals("paid")) {
                this.status = "pago";
            } else if(status.equals("canceled")) {
                this.status = "cancelado";
            } else if(status.equals("failed")) {
                this.status = "falha";
            }
        }
    }

    public void exclusaoLogica() {
        this.ativo = false;
    }

    public DadosListagemUsuario retornaListagemUsuario() {
        return new DadosListagemUsuario(this.usuario);
    }

    public List<DadosListagemLivroPedido> retornaListagemLivrosPedido() {
        List<DadosListagemLivroPedido> listaLivrosPedido = new ArrayList<>();
        for(LivroPedido livro : this.livroPedido) {
            listaLivrosPedido.add(new DadosListagemLivroPedido(livro));
        }
        return this.livroPedido.stream().map(DadosListagemLivroPedido::new).toList();
    }

}
