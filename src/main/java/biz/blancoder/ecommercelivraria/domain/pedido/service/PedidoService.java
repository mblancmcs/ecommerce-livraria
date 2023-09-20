package biz.blancoder.ecommercelivraria.domain.pedido.service;

import biz.blancoder.ecommercelivraria.ValidacaoException;
import biz.blancoder.ecommercelivraria.domain.livro.LivroRepository;
import biz.blancoder.ecommercelivraria.domain.livroPedido.DadosCadastroLivroPedido;
import biz.blancoder.ecommercelivraria.domain.livroPedido.LivroPedido;
import biz.blancoder.ecommercelivraria.domain.livroUsuario.service.LivroUsuarioService;
import biz.blancoder.ecommercelivraria.domain.pedido.*;
import biz.blancoder.ecommercelivraria.domain.usuario.UsuarioRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PedidoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PagarmeService pagarmeService;

    @Autowired
    LivroUsuarioService livroUsuarioService;

    public Pedido cadastrarPedido(DadosCadastroPedido dados) throws IOException {

        if(!usuarioRepository.existsByIdAndAtivoTrue(dados.idUsuario())) {
            throw new ValidacaoException("Usuario inexistente ou inativo");
        }
        var usuario = usuarioRepository.getReferenceById(dados.idUsuario());
        var pedido = new Pedido(usuario);
        for(DadosCadastroLivroPedido cadastroLivroPedido : dados.listaLivrosPedidos()) {
            if(!livroRepository.existsByIdAndAtivoTrue(cadastroLivroPedido.idLivro())) {
                throw new ValidacaoException("Livro inexistente ou inativo");
            }
            var livro = livroRepository.getReferenceById(cadastroLivroPedido.idLivro());
            var livroPedido = new LivroPedido(cadastroLivroPedido, pedido, livro);
            pedido.adicionarLivro(livroPedido);
        }
        pedidoRepository.save(pedido);

        return pedido;
    }

    public Pedido realizarPagamento(Integer idPedido, String formaPagamento, String jsonPagamento) throws IOException {

        if(!pedidoRepository.existsByIdAndAtivoTrue(idPedido)) {
            throw new ValidacaoException("Pedido inexistente ou inativo");
        }

        var pedido = pedidoRepository.getReferenceById(idPedido);

        if(!pedido.getStatus().equals("aguardando pagamento")) {
            throw new ValidacaoException("Processo de pagamento já iniciado ou concluido");
        }

        JsonNode jsonPagarme = pagarmeService.pagamento(jsonPagamento, formaPagamento, pedido);
        pedido.atualizarInformacoes(jsonPagarme.get("id").asText(), jsonPagarme.get("status").asText());

        if(pedido.getStatus().equals("pago")) {
            livroUsuarioService.cadastrarLivroUsuario(pedido, pedido.getUsuario());
        }

        return pedido;
    }

}
