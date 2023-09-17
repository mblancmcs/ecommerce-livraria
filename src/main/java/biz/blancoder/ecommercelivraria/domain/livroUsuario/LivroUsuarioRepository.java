package biz.blancoder.ecommercelivraria.domain.livroUsuario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LivroUsuarioRepository extends JpaRepository<LivroUsuario, Integer> {
    boolean existsByUsuarioIdAndLivroId(Integer idUsuario, Integer idLivro);

    boolean existsByIdAndAtivoTrue(Integer id);
}
