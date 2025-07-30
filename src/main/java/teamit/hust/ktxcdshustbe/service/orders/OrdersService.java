package teamit.hust.ktxcdshustbe.service.orders;

import teamit.hust.ktxcdshustbe.entity.Orders;
import teamit.hust.ktxcdshustbe.request.orders.ConfirmOrderRequest;
import teamit.hust.ktxcdshustbe.request.orders.CreateOrdersRequest;
import teamit.hust.ktxcdshustbe.response.orders.ConfirmOrdersResponse;

public interface OrdersService {

    String createOrder(CreateOrdersRequest request);
    Orders findOrdersByCodeOrder(String codeOrder);
    Orders findOrdersByIdOrder(Integer idOrder);
    Orders saveOrder(Orders orders);

    ConfirmOrdersResponse confirmOrder(ConfirmOrderRequest request);
}
