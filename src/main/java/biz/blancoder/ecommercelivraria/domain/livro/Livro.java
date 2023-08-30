package biz.blancoder.ecommercelivraria.domain.livro;

import biz.blancoder.ecommercelivraria.domain.livroUsuario.LivroUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "livros")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String titulo;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    private BigDecimal preco;
    private String descricao;
    private Boolean ativo;

}
