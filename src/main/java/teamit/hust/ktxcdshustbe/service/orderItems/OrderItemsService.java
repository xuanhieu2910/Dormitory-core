package teamit.hust.ktxcdshustbe.service.orderItems;

import teamit.hust.ktxcdshustbe.entity.OrderItems;

import java.util.List;

public interface OrderItemsService {

    List<OrderItems> saveListOrderItems(List<OrderItems> orderItemsList);
    OrderItems saveOrderItems(OrderItems orderItems);
    List<OrderItems> findOrderItemsByIdOrder(Integer idOrder);
}
