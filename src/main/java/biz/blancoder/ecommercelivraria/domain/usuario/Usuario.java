package biz.blancoder.ecommercelivraria.domain.usuario;

import biz.blancoder.ecommercelivraria.domain.endereco.DadosEndereco;
import biz.blancoder.ecommercelivraria.domain.endereco.Endereco;
import biz.blancoder.ecommercelivraria.util.StringListConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "usuarios")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    private Long cpf;

    @Convert(converter = StringListConverter.class)
    private List<String> telefones = new ArrayList<>();

    @Embedded
    private Endereco endereco;

    private String login;
    private String password;
    private String perfil;
    private Boolean ativo;

    public Usuario(DadosCadastroUsuario dados) {
        this.nome = dados.nome();
        this.cpf = dados.cpf();
        this.telefones = Arrays.asList(dados.telefones().split(";"));
        this.endereco = new Endereco(dados.dadosCadastroEndereco());
        this.login = dados.login();
        this.password = dados.password();
        this.perfil = dados.perfil();
    }

    public void atualizarInformacoes(DadosAtualizarUsuario dados) {
        if(dados.nome() != null) {
            this.nome = dados.nome();
        }
        if(dados.telefones() != null) {
            this.telefones = Arrays.asList(dados.telefones().split(";"));
        }
        if(dados.dadosEndereco() != null) {
            this.endereco.atualizarInformacoes(dados.dadosEndereco());
        }
    }

    public void exclusaoLogica() {
        this.ativo = false;
    }

    public DadosListagemUsuario retornaListagemUsuario() {
        return new DadosListagemUsuario(this);
    }

    public DadosEndereco retornaEndereco() {
        return new DadosEndereco(this.endereco);
    }

}
