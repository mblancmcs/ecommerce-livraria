package biz.blancoder.ecommercelivraria.controller;

import biz.blancoder.ecommercelivraria.domain.endereco.Endereco;
import biz.blancoder.ecommercelivraria.domain.livro.Categoria;
import biz.blancoder.ecommercelivraria.domain.livro.Livro;
import biz.blancoder.ecommercelivraria.domain.livroPedido.DadosCadastroLivroPedido;
import biz.blancoder.ecommercelivraria.domain.livroPedido.LivroPedido;
import biz.blancoder.ecommercelivraria.domain.pedido.*;
import biz.blancoder.ecommercelivraria.domain.pedido.service.PedidoService;
import biz.blancoder.ecommercelivraria.domain.usuario.PerfilUsuario;
import biz.blancoder.ecommercelivraria.domain.usuario.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;
//    private MockMvc mockMvc = MockMvcBuilders.standaloneSetup(ValidarCliente.class).setValidator(mockValidator);

    @Autowired
    private JacksonTester<DadosCadastroPedido> dadosCadastroPedidoJacksonTester;

    @Autowired
    private JacksonTester<DadosDetalhesPedido> dadosDetalhesPedidoJacksonTester;

    @MockBean
    private PedidoService pedidoService;

    @Test
    @DisplayName("Deve retornar o código http 400 quando houverem informações invalidas no cadastro do pedido")
    @WithMockUser(roles = {"CLIENTE"})
    void cadastrarCenario1() throws Exception {
        var response = mockMvc.perform(post("/pedido"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deve retornar o código http 201 quando o pedido for cadastrado com sucesso")
    @WithMockUser(roles = {"CLIENTE"})
    void cadastrarCenario2() throws Exception {

        var endereco = new Endereco("Rua teste", 10, "Bairro teste", "Rio de Janeiro",
                "RJ", 11211333, "complemento");
        List<String> telefones = Arrays.asList("5521912345678", "5521987654321");

        var usuario = new Usuario(2, "Marcelo", 11122233344l, "marcelo@test.com.br", telefones,
                endereco, "marcelo_cliente", "$2y$10$9uBv2zwqrHy8UnNIuKc3juRVy.GH5DljmHRpbmvmiRT.e8ZGKZtEi",
                PerfilUsuario.CLIENTE, true
        );
        // Forçando a autenticacao do usuário para passar numa validacao de autenticacao
        var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var livro1 = new Livro(1, "Livro teste", Categoria.ACAO, new BigDecimal(19.90), "descricao teste", LocalDate.now(),
                1234567890123l, true);
        var livro2 = new Livro(2, "Livro 2 teste", Categoria.AVENTURA, new BigDecimal(29.99), "descricao 2 teste", LocalDate.now(),
                1234567890123l, true);

        var dadosCadastroLivroPedido1 = new DadosCadastroLivroPedido(5, livro1.getId());
        var dadosCadastroLivroPedido2 = new DadosCadastroLivroPedido(3, livro2.getId());
        List<DadosCadastroLivroPedido> listaLivros = Arrays.asList(dadosCadastroLivroPedido1, dadosCadastroLivroPedido2);

        var pedido = new Pedido(usuario);

        var livroPedido1 = new LivroPedido(dadosCadastroLivroPedido1, pedido, livro1);
        var livroPedido2 = new LivroPedido(dadosCadastroLivroPedido2, pedido, livro2);

        pedido.adicionarLivro(livroPedido1);
        pedido.adicionarLivro(livroPedido2);

        var dadosCadastroPedido = new DadosCadastroPedido(listaLivros, usuario.getId());
        var dadosDetalhesPedido = new DadosDetalhesPedido(pedido);

        when(pedidoService.cadastrarPedido(any())).thenReturn(pedido);

        var response = mockMvc.perform(
                post("/pedido")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosCadastroPedidoJacksonTester
                                .write(dadosCadastroPedido).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        var jsonEsperado = dadosDetalhesPedidoJacksonTester.write(dadosDetalhesPedido).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);

    }

}