package biz.blancoder.ecommercelivraria.domain.usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Usuario findByCpfAndAtivoTrue(Long cpf);

    boolean existsByLogin(String login);

    Page<Usuario> findAllByAtivoTrue(Pageable paginacao);

    boolean existsByIdAndAtivoTrue(Integer id);

    boolean existsByCpfAndAtivoTrue(Long cpf);

    UserDetails findByLogin(String username);

    @Query("""
            SELECT u FROM Usuario u WHERE u.login = :login
            """)
    Usuario getUsuarioByLogin(String login);

    Usuario findByIdAndAtivoTrue(Integer id);
}
