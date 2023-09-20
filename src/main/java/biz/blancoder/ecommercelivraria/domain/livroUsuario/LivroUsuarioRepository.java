package biz.blancoder.ecommercelivraria.domain.livroUsuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivroUsuarioRepository extends JpaRepository<LivroUsuario, Integer> {
    boolean existsByUsuarioIdAndLivroId(Integer idUsuario, Integer idLivro);

    boolean existsByIdAndAtivoTrue(Integer id);

    Page<LivroUsuario> findAllByUsuarioId(Integer idUsuario, Pageable paginacao);

    Page<LivroUsuario> findAllByAtivoTrue(Pageable paginacao);
}
