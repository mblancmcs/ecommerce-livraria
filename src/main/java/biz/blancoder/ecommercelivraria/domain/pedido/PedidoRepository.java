package biz.blancoder.ecommercelivraria.domain.pedido;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    Pedido getReferenceByIdGateway(String idGateway);

    boolean existsByIdGateway(String text);

    boolean existsByIdAndAtivoTrue(Integer idPedido);
}
