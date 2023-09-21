package biz.blancoder.ecommercelivraria.domain.usuario;

import biz.blancoder.ecommercelivraria.domain.endereco.Endereco;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Deve retornar o usuário informado")
    void getUsuarioByLoginCenario1() {
        var endereco = new Endereco("Rua teste", 10, "Bairro teste", "Rio de Janeiro",
                "RJ", 11211333, "complemento");
        List<String> telefones = Arrays.asList("5521912345678", "5521987654321");

        var usuario = new Usuario(null, "Usuario Test", 11122233344l, "usuario@test.com.br", telefones,
                endereco, "usuario_cliente", "$2y$10$9uBv2zwqrHy8UnNIuKc3juRVy.GH5DljmHRpbmvmiRT.e8ZGKZtEi",
                PerfilUsuario.CLIENTE, true
        );

        em.persist(usuario);

        assertThat(usuarioRepository.getUsuarioByLogin(usuario.getLogin())).isEqualTo(usuario);
    }

    @Test
    @DisplayName("Deve retornar nulo caso não haja usuário com o login informado")
    void getUsuarioByLoginCenario2() {
        var endereco = new Endereco("Rua teste", 10, "Bairro teste", "Rio de Janeiro",
                "RJ", 11211333, "complemento");
        List<String> telefones = Arrays.asList("5521912345678", "5521987654321");

        var usuario = new Usuario(null, "Usuario Test", 11122233344l, "usuario@test.com.br", telefones,
                endereco, "usuario_cliente", "$2y$10$9uBv2zwqrHy8UnNIuKc3juRVy.GH5DljmHRpbmvmiRT.e8ZGKZtEi",
                PerfilUsuario.CLIENTE, true
        );

        em.persist(usuario);

        assertThat(usuarioRepository.getUsuarioByLogin("user")).isNull();
    }

}