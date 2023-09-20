package biz.blancoder.ecommercelivraria.domain.pedido;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    Pedido getReferenceByIdGateway(String idGateway);

    boolean existsByIdGateway(String text);

    boolean existsByIdAndAtivoTrue(Integer idPedido);

    Page<Pedido> findAllByAtivoTrue(Pageable paginacao);

    Page<Pedido> findAllByStatusAndAtivoTrue(String status, Pageable paginacao);

    Page<Pedido> findAllByUsuarioIdAndAtivoTrue(Integer id, Pageable paginacao);
}
