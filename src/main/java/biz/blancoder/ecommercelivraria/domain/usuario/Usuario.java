package biz.blancoder.ecommercelivraria.domain.usuario;

import biz.blancoder.ecommercelivraria.domain.endereco.Endereco;
import biz.blancoder.ecommercelivraria.util.StringListConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
    private String role;
    private Boolean ativo;

}
