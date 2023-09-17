package biz.blancoder.ecommercelivraria.domain.pedido.service;

import biz.blancoder.ecommercelivraria.ValidacaoException;
import biz.blancoder.ecommercelivraria.domain.livroPedido.LivroPedido;
import biz.blancoder.ecommercelivraria.domain.pedido.Pedido;
import biz.blancoder.ecommercelivraria.domain.pedido.paymentsDTO.DadosBoleto;
import biz.blancoder.ecommercelivraria.domain.pedido.paymentsDTO.DadosCartaoCredito;
import biz.blancoder.ecommercelivraria.domain.pedido.paymentsDTO.DadosPix;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PagarmeService {

    @Value("${ecommercelivraria.security.s_api_key}")
    private String apiKey;

    @Autowired
    private TokenizacaoPagarmeService tokenizacao;

    public JsonNode pagamento(String jsonString, String formaPagamento, Pedido pedido) throws IOException {

        if(pedido.getLivroPedido().size() == 0) {
            throw new ValidacaoException("Pedido sem itens");
        }

        ObjectMapper mapper = new ObjectMapper();

        String telefone = pedido.getUsuario().getTelefones().get(0);
        String codPais = telefone.substring(0,2);
        String codArea = telefone.substring(2,4);
        String numero = telefone.substring(4);

        String itens = livrosNoPedido(pedido.getLivroPedido());
        Integer valorTotal = adicionarCasaDecimalSeNecessario(String.valueOf(pedido.getValorTotal()));

        String dadosPagamento = "";
        if(formaPagamento == "cartao") {
            DadosCartaoCredito dtoPagamento = mapper.readValue(jsonString, DadosCartaoCredito.class);
            String tokenCartao = tokenizacao.tokenizarCartao(dtoPagamento, pedido);
            dadosPagamento = "{" +
                    "\"credit_card\":{" +
                    "\"card\":" +
                    "{\"billing_address\":" +
                    "{\"line_1\":\""+pedido.getUsuario().getEndereco().getNumero()+","+pedido.getUsuario().getEndereco().getLogradouro()+
                    ","+pedido.getUsuario().getEndereco().getBairro()+"\"," +
                    "\"line_2\":\""+pedido.getUsuario().getEndereco().getComplemento()+"\"," +
                    "\"zip_code\":\""+pedido.getUsuario().getEndereco().getCep()+"\"," +
                    "\"city\":\""+pedido.getUsuario().getEndereco().getCidade()+"\"," +
                    "\"state\":\""+pedido.getUsuario().getEndereco().getUf()+"\"," +
                    "\"country\":\"BR\"" +
                    "}" +
                    "}," +
                    "\"installments\":"+dtoPagamento.parcelas()+"," +
                    "\"statement_descriptor\":\""+dtoPagamento.tituloCompra()+"\"," +
                    "\"card_token\":\"" + tokenCartao + "\"," +
                    "\"operation_type\":\"auth_and_capture\"" +
                    "}," +
                    "\"payment_method\":\"credit_card\"" +
                    "}";
        } else if(formaPagamento == "pix") {
            DadosPix dtoPagamento = mapper.readValue(jsonString, DadosPix.class);
            dadosPagamento = "{" +
                    "\"Pix\":{" +
                    "\"expires_at\":\""+dtoPagamento.dataExpiracao()+"\"" +
                    "}," +
                    "\"payment_method\":\"pix\"," +
                    "\"amount\":"+valorTotal+
                    "}";
        } else if(formaPagamento == "boleto") {
            DadosBoleto dtoPagamento = mapper.readValue(jsonString, DadosBoleto.class);
            dadosPagamento = "{" +
                    "\"boleto\":{" +
                    "\"bank\":\""+dtoPagamento.banco()+"\"," +
                    "\"due_at\":\""+dtoPagamento.dataVencimento()+"\"," +
                    "\"instructions\":\""+dtoPagamento.instrucoes()+"\"" +
                    "}," +
                    "\"payment_method\":\"boleto\"," +
                    "\"amount\":"+valorTotal+
                    "}";
        }

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                "{\"customer\":{" +
                        "\"address\":{" +
                        "\"line_1\":\""+pedido.getUsuario().getEndereco().getNumero()+","+pedido.getUsuario().getEndereco().getLogradouro()+
                        ","+pedido.getUsuario().getEndereco().getBairro()+"\"," +
                        "\"zip_code\":\""+pedido.getUsuario().getEndereco().getCep()+"\"," +
                        "\"city\":\""+pedido.getUsuario().getEndereco().getCidade()+"\"," +
                        "\"state\":\""+pedido.getUsuario().getEndereco().getUf()+"\"," +
                        "\"country\":\"BR\"" +
                        "}," +
                        "\"phones\":{" +
                        "\"mobile_phone\":{" +
                        "\"country_code\":\""+codPais+"\"," +
                        "\"area_code\":\""+codArea+"\"," +
                        "\"number\":\""+numero+"\"" +
                        "}" +
                        "}," +
                        "\"name\":\""+pedido.getUsuario().getNome()+"\"," +
                        "\"type\":\"individual\"," +
                        "\"code\":\""+pedido.getUsuario().getId()+"\"," +
                        "\"document\":\""+pedido.getUsuario().getCpf()+"\"," +
                        "\"document_type\":\"CPF\"," +
                        "\"email\":\""+pedido.getUsuario().getEmail()+"\"" +
                        "}," +
                        "\"items\":[" +
                        itens +
                        "]," +
                        "\"payments\":[" +
                        dadosPagamento +
                        "]," +
                        "\"closed\":true," +
                        "\"antifraud_enabled\":true" +
                        "}"
        );
        Request request = new Request.Builder()
                .url("https://api.pagar.me/core/v5/orders")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "Basic " + apiKey)
                .build();

        Response response = client.newCall(request).execute();

        if(response.isSuccessful()) {
            JsonNode json = new ObjectMapper().readTree(response.body().string());
            System.out.println(json);
            return json;
        } else {
            throw new ValidacaoException("Problema na requisição ao Pagar.me");
        }

    }

    public String livrosNoPedido(List<LivroPedido> livrosPedido) {
        String itens = "";
        for(LivroPedido livro : livrosPedido) {
            var precoString = adicionarCasaDecimalSeNecessario(livro.getPrecoUnitario().toString());
            itens += "{" +
                    "\"amount\":"+Integer.valueOf(precoString)+"," +
                    "\"description\":\""+livro.getLivro().getTitulo()+"\"," +
                    "\"quantity\":"+livro.getQuantidade()+"," +
                    "\"code\":\""+livro.getLivro().getId()+"\"" +
                    "},";
        }
        itens = itens.substring(0, itens.length() -1);
        return itens;
    }

    public Integer adicionarCasaDecimalSeNecessario(String preco) {
        if(preco.indexOf(".") + 1 == preco.length() - 1) {
            preco += "0";
        }
        preco = preco.replace(".", "");
        return Integer.valueOf(preco);
    }

}
