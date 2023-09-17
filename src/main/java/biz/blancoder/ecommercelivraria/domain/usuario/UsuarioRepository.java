package biz.blancoder.ecommercelivraria.domain.usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Usuario findByCpfAndAtivoTrue(Long cpf);

    boolean existsByLogin(String login);

    Page<Usuario> findAllByAtivoTrue(Pageable paginacao);

    boolean existsByIdAndAtivoTrue(Integer id);

    boolean existsByCpfAndAtivoTrue(Long cpf);
}
