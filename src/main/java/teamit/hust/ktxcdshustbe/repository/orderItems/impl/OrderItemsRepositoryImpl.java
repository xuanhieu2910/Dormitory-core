package teamit.hust.ktxcdshustbe.repository.orderItems.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.entity.OrderItems;
import teamit.hust.ktxcdshustbe.repository.orderItems.OrderItemsRepositoryCustom;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.ArrayList;
import java.util.List;

public class OrderItemsRepositoryImpl implements OrderItemsRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<OrderItems> findOrderItemsByIdOrder(Integer idOrder) {
        StringBuilder sb = new StringBuilder();
        sb.append("select oi.id_order_item, oi.code_order_item, oi.id_order,  " +
                "       oi.id_object, oi.id_user, oi.quantity, oi.total_money,   " +
                "       oi.time_created, oi.time_modified, oi.id_user_created,  " +
                "       oi.id_user_modified, oi.notes, oi.status, oi.type_order  " +
                "from order_items oi  " +
                "    inner join orders ord on oi.id_order = ord.id_order  " +
                "where ord.id_order = :idOrder ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("idOrder", idOrder);
        List<Object[]> result = query.getResultList();
        List<OrderItems>  orderItemsList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                orderItemsList.add(writeDataOrderItems(obj));
            }
        }
        return orderItemsList;
    }

    private OrderItems writeDataOrderItems(Object[] obj) {
        OrderItems orderItems = new OrderItems();
        orderItems.setIdOrderItem(ValueUtil.getIntegerByObject(obj[0]));
        orderItems.setCodeOrderItem(ValueUtil.getStringByObject(obj[1]));
        orderItems.setIdOrder(ValueUtil.getIntegerByObject(obj[2]));
        orderItems.setIdObject(ValueUtil.getIntegerByObject(obj[3]));
        orderItems.setIdUser(ValueUtil.getIntegerByObject(obj[4]));
        orderItems.setQuantity(ValueUtil.getIntegerByObject(obj[5]));
        orderItems.setTotalMoney(ValueUtil.getStringByObject(obj[6]));
        orderItems.setTimeCreated(ValueUtil.getLongByObject(obj[7]));
        orderItems.setTimeModified(ValueUtil.getLongByObject(obj[8]));
        orderItems.setIdUserCreated(ValueUtil.getIntegerByObject(obj[9]));
        orderItems.setIdUserModified(ValueUtil.getIntegerByObject(obj[10]));
        orderItems.setNotes(ValueUtil.getStringByObject(obj[11]));
        orderItems.setStatus(ValueUtil.getIntegerByObject(obj[12]));
        orderItems.setTypeOrder(ValueUtil.getIntegerByObject(obj[13]));
        return orderItems;
    }
}
