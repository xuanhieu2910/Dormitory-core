package teamit.hust.ktxcdshustbe.repository.orders;

import teamit.hust.ktxcdshustbe.dto.orders.DetailOrderDto;
import teamit.hust.ktxcdshustbe.entity.Orders;

import java.util.Optional;

public interface OrdersRepositoryCustom {
    Optional<Orders> findOrdersByCodeOrder(String codeOrder);

    Optional<Orders> findOrdersByIdOrder(Integer idOrder);

    Optional<DetailOrderDto> findOrdersDetailByCodeOrders(String codeOrders);
}
