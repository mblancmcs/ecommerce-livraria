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

}
