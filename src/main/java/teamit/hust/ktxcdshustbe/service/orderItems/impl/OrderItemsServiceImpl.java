package teamit.hust.ktxcdshustbe.service.orderItems.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.entity.OrderItems;
import teamit.hust.ktxcdshustbe.repository.orderItems.OrderItemsRepository;
import teamit.hust.ktxcdshustbe.service.orderItems.OrderItemsService;

import java.util.List;

@Service
public class OrderItemsServiceImpl implements OrderItemsService {

    @Autowired
    OrderItemsRepository orderItemsRepository;

    @Override
    public List<OrderItems> saveListOrderItems(List<OrderItems> orderItemsList) {
        return orderItemsRepository.saveAll(orderItemsList);
    }

    @Override
    public OrderItems saveOrderItems(OrderItems orderItems) {
        return orderItemsRepository.save(orderItems);
    }

    @Override
    public List<OrderItems> findOrderItemsByIdOrder(Integer idOrder) {
        return orderItemsRepository.findOrderItemsByIdOrder(idOrder);
    }
}
