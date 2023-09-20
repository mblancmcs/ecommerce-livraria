package biz.blancoder.ecommercelivraria.infra.security;

import biz.blancoder.ecommercelivraria.domain.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("ecommercelivraria.security.token.secret")
    private String secret;

    public String gerarToken(Usuario usuario) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("api-ecommerce-livraria")
                    .withSubject(usuario.getLogin())
//                    .withClaim("id", usuario.getId())
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar o token JWT", exception);
        }
    }

    public String getSubject(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("api-ecommerce-livraria")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido no cabeçalho Authorization");
        }
    }

//    public String getClaim(String tokenJWT) {
//        try {
//            var algoritmo = Algorithm.HMAC256(secret);
//            return JWT.require(algoritmo)
//                    .withIssuer("api-ecommerce-livraria")
//                    .build()
//                    .verify(tokenJWT)
//                    .getClaim("id")
//                    .asString();
//        } catch(JWTVerificationException exception) {
//            throw new RuntimeException("Token JWT inválido no cabeçalho Authorization");
//        }
//    }

    private Instant dataExpiracao() { // 2 horas, porem considerar a localizacao
        return LocalDateTime.now()
                .plusHours(2)
                .toInstant(ZoneOffset.of("-03:00"));
    }

}
