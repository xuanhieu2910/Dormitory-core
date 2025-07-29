package teamit.hust.ktxcdshustbe.repository.orderItems;

import teamit.hust.ktxcdshustbe.entity.OrderItems;

import java.util.List;

public interface OrderItemsRepositoryCustom {
    List<OrderItems> findOrderItemsByIdOrder(Integer idOrder);
}
