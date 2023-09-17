package biz.blancoder.ecommercelivraria.domain.livro;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivroRepository extends JpaRepository<Livro, Integer> {
    Page<Livro> findAllByAtivoTrue(Pageable paginacao);

    boolean existsByIdAndAtivoTrue(Integer id);
}
