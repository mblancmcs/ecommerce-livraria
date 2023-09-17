package biz.blancoder.ecommercelivraria.domain.pedido.service;

import biz.blancoder.ecommercelivraria.ValidacaoException;
import biz.blancoder.ecommercelivraria.domain.pedido.Pedido;
import biz.blancoder.ecommercelivraria.domain.pedido.paymentsDTO.DadosCartaoCredito;
import me.pagar.api.PagarmeApiSDKClient;
import me.pagar.api.controllers.TokensController;
import me.pagar.api.exceptions.ApiException;
import me.pagar.api.models.CreateCardTokenRequest;
import me.pagar.api.models.CreateTokenRequest;
import me.pagar.api.models.GetTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TokenizacaoPagarmeService {

    @Value("${ecommercelivraria.security.p_api_key}")
    private String apiKey;

    private PagarmeApiSDKClient client = new PagarmeApiSDKClient.Builder().httpClientConfig(configBuilder -> configBuilder.timeout(0))
            .serviceRefererName("TokensController")
            .basicAuthCredentials("Basic " + apiKey, "")
            .build();

    public String tokenizarCartao(DadosCartaoCredito dados, Pedido pedido) {

        TokensController tokensController = client.getTokensController();
        CreateTokenRequest request = new CreateTokenRequest.Builder("card", new CreateCardTokenRequest.Builder(
                dados.numero(),
                pedido.getUsuario().getNome(),
                dados.mesExpiracao(),
                dados.anoExpiracao(),
                dados.cvv(),
                null,
                "EBOOKS").build()
        ).build();

        try {
            GetTokenResponse result = tokensController.createToken(apiKey, request, null);
            return result.getId();
        } catch(ApiException e) {
            throw new ValidacaoException("Problema ao gerar o card_token");
        } catch (IOException e) {
            throw new ValidacaoException("Problema ao gerar o card_token");
        }
    }

}
