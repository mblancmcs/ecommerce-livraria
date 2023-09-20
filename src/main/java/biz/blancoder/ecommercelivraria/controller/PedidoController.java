package biz.blancoder.ecommercelivraria.controller;

import biz.blancoder.ecommercelivraria.ValidacaoException;
import biz.blancoder.ecommercelivraria.domain.livro.LivroRepository;
import biz.blancoder.ecommercelivraria.domain.livroUsuario.service.LivroUsuarioService;
import biz.blancoder.ecommercelivraria.domain.pedido.*;
import biz.blancoder.ecommercelivraria.domain.pedido.service.PedidoService;
import biz.blancoder.ecommercelivraria.domain.usuario.UsuarioRepository;
import biz.blancoder.ecommercelivraria.domain.usuario.validadoresClientes.ValidarCliente;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/pedido")
@Slf4j
@SecurityRequirement(name = "bearer-key")
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

    @Autowired
    private List<ValidarCliente> validadoresCliente;

    @Secured("ROLE_ADMIN")
    @GetMapping
    public ResponseEntity<Page<DadosListagemPedido>> listarPedidos(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
                                                                   Pageable paginacao) {
        var paginaPedidos = pedidoRepository.findAllByAtivoTrue(paginacao).map(DadosListagemPedido::new);
        return ResponseEntity.ok(paginaPedidos);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/status={status}")
    public ResponseEntity<Page<DadosListagemPedido>> listarPorStatus(@PageableDefault(size = 10, sort = "status", direction = Sort.Direction.DESC)
                                                                     Pageable paginacao, @PathVariable String status) {
        var paginaPedidos = pedidoRepository.findAllByStatusAndAtivoTrue(status, paginacao).map(DadosListagemPedido::new);
        return ResponseEntity.ok(paginaPedidos);
    }

    @Secured({"ROLE_CLIENTE", "ROLE_FUNCIONARIO"})
    @GetMapping("/id={id}")
    public ResponseEntity listarPorId(@PathVariable Integer id) {
        validadoresCliente.forEach(validador -> validador.validarCliente(id));
        var pedido = pedidoRepository.getReferenceById(id);
        return ResponseEntity.ok(new DadosListagemPedido(pedido));
    }

    @Secured({"ROLE_CLIENTE", "ROLE_FUNCIONARIO"})
    @GetMapping("/id_usuario={id}")
    public ResponseEntity<Page<DadosListagemPedido>> listarPorUsuario(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
                                                                   Pageable paginacao, @PathVariable Integer id) {
        validadoresCliente.forEach(validador -> validador.validarCliente(id));
        var paginaPedido = pedidoRepository.findAllByUsuarioIdAndAtivoTrue(id, paginacao).map(DadosListagemPedido::new);
        return ResponseEntity.ok(paginaPedido);
    }

    @Secured("ROLE_CLIENTE")
    @PostMapping
    @Transactional
    public ResponseEntity cadastrarPedido(@RequestBody @Valid DadosCadastroPedido dados, UriComponentsBuilder uriBuilder) throws IOException {
        validadoresCliente.forEach(validador -> validador.validarCliente(dados.idUsuario()));
        var pedido = pedidoService.cadastrarPedido(dados);
        var uri = uriBuilder.path("/pedido/id={id}").buildAndExpand(pedido.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhesPedido(pedido));
    }

    @Secured("ROLE_CLIENTE")
    @PostMapping("/cartao_credito")
    @Transactional
    public ResponseEntity pagamentoPedidoCartao(@RequestBody @Valid DadosPagamentoPedidoCartao dados, UriComponentsBuilder uriBuilder) throws IOException {
        if(pedidoRepository.existsByIdAndAtivoTrue(dados.idPedido())) {
            var pedidoRequisicao = pedidoRepository.getReferenceById(dados.idPedido());
            validadoresCliente.forEach(validador -> validador.validarCliente(pedidoRequisicao.getUsuario().getId()));
        } else throw new ValidacaoException("Id do pedido inexistente ou inativo");

        var jsonString = new ObjectMapper().writeValueAsString(dados.pagamento());
        var pedido = pedidoService.realizarPagamento(dados.idPedido(), "cartao", jsonString);
        var uri = uriBuilder.path("/pedido/id={id}").buildAndExpand(pedido.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemPedido(pedido));
    }

    @Secured("ROLE_CLIENTE")
    @PostMapping("/boleto")
    @Transactional
    public ResponseEntity pagamentoPedidoBoleto(@RequestBody @Valid DadosPagamentoPedidoBoleto dados, UriComponentsBuilder uriBuilder) throws IOException {
        if(pedidoRepository.existsByIdAndAtivoTrue(dados.idPedido())) {
            var pedidoRequisicao = pedidoRepository.getReferenceById(dados.idPedido());
            validadoresCliente.forEach(validador -> validador.validarCliente(pedidoRequisicao.getUsuario().getId()));
        } else throw new ValidacaoException("Id do pedido inexistente ou inativo");

        var jsonString = new ObjectMapper().writeValueAsString(dados.pagamento());
        var pedido = pedidoService.realizarPagamento(dados.idPedido(), "boleto", jsonString);
        var uri = uriBuilder.path("/pedido/id={id}").buildAndExpand(pedido.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemPedido(pedido));
    }

    @Secured("ROLE_CLIENTE")
    @PostMapping("/pix")
    @Transactional
    public ResponseEntity pagamentoPedidoPix(@RequestBody @Valid DadosPagamentoPedidoPix dados, UriComponentsBuilder uriBuilder) throws IOException {
        if(pedidoRepository.existsByIdAndAtivoTrue(dados.idPedido())) {
            var pedidoRequisicao = pedidoRepository.getReferenceById(dados.idPedido());
            validadoresCliente.forEach(validador -> validador.validarCliente(pedidoRequisicao.getUsuario().getId()));
        } else throw new ValidacaoException("Id do pedido inexistente ou inativo");

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
            return ResponseEntity.badRequest().build();
        }
        var pedido = pedidoRepository.getReferenceByIdGateway(json.get("data").get("id").asText());
        pedido.atualizarInformacoes(null, json.get("data").get("status").asText());
        if(pedido.getStatus().equals("pago")) {
            livroUsuarioService.cadastrarLivroUsuario(pedido, pedido.getUsuario());
        }
        log.info("Atualização do pedido realizada com sucesso para o status: " + json.get("data").get("status").asText());
        return ResponseEntity.noContent().build();
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/id={id}")
    @Transactional
    public ResponseEntity exclusaoLogica(@PathVariable Integer id) {
        if(!pedidoRepository.existsByIdAndAtivoTrue(id)) {
            throw new ValidacaoException("Pedido inexistente ou já inativo");
        }
        var pedido = pedidoRepository.getReferenceById(id);
        pedido.exclusaoLogica();
        return ResponseEntity.noContent().build();
    }

}
