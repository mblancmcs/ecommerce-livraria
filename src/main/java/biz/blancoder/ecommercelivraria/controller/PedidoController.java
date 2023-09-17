package biz.blancoder.ecommercelivraria.controller;

import biz.blancoder.ecommercelivraria.domain.livro.LivroRepository;
import biz.blancoder.ecommercelivraria.domain.livroUsuario.service.LivroUsuarioService;
import biz.blancoder.ecommercelivraria.domain.pedido.*;
import biz.blancoder.ecommercelivraria.domain.pedido.service.PedidoService;
import biz.blancoder.ecommercelivraria.domain.usuario.UsuarioRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RestController
@RequestMapping("/pedido")
@Slf4j
public class PedidoController {

    private JsonNode json;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private LivroUsuarioService livroUsuarioService;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrarPedido(@RequestBody @Valid DadosCadastroPedido dados, UriComponentsBuilder uriBuilder) throws IOException {
        var pedido = pedidoService.cadastrarPedido(dados);
        var uri = uriBuilder.path("/pedido/id={id}").buildAndExpand(pedido.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhesPedido(pedido));
    }

    @PostMapping("/cartao")
    @Transactional
    public ResponseEntity pagamentoPedidoCartao(@RequestBody @Valid DadosPagamentoPedidoCartao dados, UriComponentsBuilder uriBuilder) throws IOException {
        var jsonString = new ObjectMapper().writeValueAsString(dados.pagamento());
        var pedido = pedidoService.realizarPagamento(dados.idPedido(), "cartao", jsonString);
        var uri = uriBuilder.path("/pedido/id={id}").buildAndExpand(pedido.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemPedido(pedido));
    }

    @PostMapping("/boleto")
    @Transactional
    public ResponseEntity pagamentoPedidoBoleto(@RequestBody @Valid DadosPagamentoPedidoBoleto dados, UriComponentsBuilder uriBuilder) throws IOException {
        var jsonString = new ObjectMapper().writeValueAsString(dados.pagamento());
        var pedido = pedidoService.realizarPagamento(dados.idPedido(), "boleto", jsonString);
        var uri = uriBuilder.path("/pedido/id={id}").buildAndExpand(pedido.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemPedido(pedido));
    }

    @PostMapping("/pix")
    @Transactional
    public ResponseEntity pagamentoPedidoPix(@RequestBody @Valid DadosPagamentoPedidoPix dados, UriComponentsBuilder uriBuilder) throws IOException {
        var jsonString = new ObjectMapper().writeValueAsString(dados.pagamento());
        var pedido = pedidoService.realizarPagamento(dados.idPedido(), "pix", jsonString);
        var uri = uriBuilder.path("/pedido/id={id}").buildAndExpand(pedido.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemPedido(pedido));
    }

    @PostMapping("/webhook_atualizar")
    @Transactional
    public ResponseEntity atualizar(@RequestBody String jsonRequisicao) throws IOException {
        json = new ObjectMapper().readTree(jsonRequisicao);
        if(!pedidoRepository.existsByIdGateway(json.get("data").get("id").asText())) {
            ResponseEntity.badRequest();
        }
        var pedido = pedidoRepository.getReferenceByIdGateway(json.get("data").get("id").asText());
        pedido.atualizarInformacoes(null, json.get("data").get("status").asText());
        if(pedido.getStatus().equals("pago")) {
            livroUsuarioService.cadastrarLivroUsuario(pedido, pedido.getUsuario());
        }
        log.info("Atualização do pedido realizada com sucesso para o status: " + json.get("data").get("status").asText());
        return ResponseEntity.noContent().build();
    }

}
