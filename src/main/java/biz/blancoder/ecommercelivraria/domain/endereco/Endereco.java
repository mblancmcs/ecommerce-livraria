package biz.blancoder.ecommercelivraria.domain.endereco;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Endereco {

    private String logradouro;
    private Integer numero;
    private String bairro;
    private String cidade;
    private String uf;
    private Integer cep;
    private String complemento;

    public Endereco(DadosCadastroEndereco dados) {
        this.logradouro = dados.logradouro();
        this.numero = dados.numero();
        this.bairro = dados.bairro();
        this.cidade = dados.cidade();
        this.uf = dados.uf();
        this.cep = dados.cep();
        this.complemento = dados.complemento();
    }

    public void atualizarInformacoes(DadosEndereco dadosEndereco) {
        if(dadosEndereco.logradouro() != null) {
            this.logradouro = dadosEndereco.logradouro();
        }
        if(dadosEndereco.numero() != null) {
            this.numero = dadosEndereco.numero();
        }
        if(dadosEndereco.bairro() != null) {
            this.bairro = dadosEndereco.bairro();
        }
        if(dadosEndereco.cidade() != null) {
            this.cidade = dadosEndereco.cidade();
        }
        if(dadosEndereco.uf() != null) {
            this.uf = dadosEndereco.uf();
        }
        if(dadosEndereco.cep() != null) {
            this.cep = dadosEndereco.cep();
        }
        if(dadosEndereco.complemento() != null) {
            this.complemento = dadosEndereco.complemento();
        }
    }
}
