package teamit.hust.ktxcdshustbe.repository.orders.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.dto.orderItems.DetailOrderItemsDto;
import teamit.hust.ktxcdshustbe.dto.orders.DetailOrderDto;
import teamit.hust.ktxcdshustbe.entity.Orders;
import teamit.hust.ktxcdshustbe.repository.orders.OrdersRepositoryCustom;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrdersRepositoryImpl implements OrdersRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Optional<Orders> findOrdersByCodeOrder(String codeOrder) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select ord.id_order, ord.title_order, ord.code_order, " +
                "       ord.id_user, ord.status_order, ord.time_created, " +
                "       ord.time_modified, ord.id_transaction_payment, " +
                "       ord.id_user_created, ord.id_user_modified, " +
                "       ord.total_money, ord.type_order " +
                "from orders ord  " +
                "where ord.code_order = :codeOrder ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeOrder", codeOrder);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                return Optional.of(writeDataOrders(obj));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Orders> findOrdersByIdOrder(Integer idOrder) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select ord.id_order, ord.title_order, ord.code_order, " +
                "       ord.id_user, ord.status_order, ord.time_created, " +
                "       ord.time_modified, ord.id_transaction_payment, " +
                "       ord.id_user_created, ord.id_user_modified, " +
                "       ord.total_money, ord.type_order " +
                "from orders ord  " +
                "where ord.id_order = :idOrder ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("idOrder", idOrder);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                return Optional.of(writeDataOrders(obj));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<DetailOrderDto> findOrdersDetailByCodeOrders(String codeOrders) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select ord.id_order, ord.title_order, ord.code_order, " +
                "       ord.status_order, ord.total_money, ord.time_created, ord.time_modified, ord.value, " +
                "       ori.id_order_item, ori.code_order_item, ori.id_order, " +
                "       ori.id_object, ori.id_user, ori.quantity, ori.total_money, " +
                "       ori.time_created, ori.time_modified, ori.id_user_created, " +
                "       ori.id_user_modified, ori.notes, ori.status, ori.type_order, " +
                "       ro.code_room, ro.title " +
                "from orders ord " +
                "    inner join order_items ori on ord.id_order = ori.id_order " +
                "    inner join room ro on ori.id_object = ro.id_room " +
                "where ord.code_order = :codeOrder  ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeOrder", codeOrders);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            DetailOrderDto detailOrderDto = writeDataDetailOrder(result.get(0));
            List<DetailOrderItemsDto> itemsDtos = new ArrayList<>();
            for (Object[] obj : result){
                itemsDtos.add(writeDataDetailOrderItem(obj));
            }
            detailOrderDto.setItems(itemsDtos);
            return Optional.of(detailOrderDto);
        }
        return Optional.empty();
    }

    private DetailOrderItemsDto writeDataDetailOrderItem(Object[] obj) {
        DetailOrderItemsDto detailOrderItemsDto = new DetailOrderItemsDto();
        detailOrderItemsDto.setIdOrderItem(ValueUtil.getIntegerByObject(obj[8]));
        detailOrderItemsDto.setCodeOrderItem(ValueUtil.getStringByObject(obj[9]));
        detailOrderItemsDto.setIdOrder(ValueUtil.getIntegerByObject(obj[10]));
        detailOrderItemsDto.setIdObject(ValueUtil.getIntegerByObject(obj[11]));
        detailOrderItemsDto.setIdUser(ValueUtil.getIntegerByObject(obj[12]));
        detailOrderItemsDto.setQuantity(ValueUtil.getIntegerByObject(obj[13]));
        detailOrderItemsDto.setTotalMoney(ValueUtil.getStringByObject(obj[14]));
        detailOrderItemsDto.setTimeCreated(ValueUtil.getLongByObject(obj[15]));
        detailOrderItemsDto.setTimeModified(ValueUtil.getLongByObject(obj[16]));
        detailOrderItemsDto.setIdUserCreated(ValueUtil.getIntegerByObject(obj[17]));
        detailOrderItemsDto.setIdUserModified(ValueUtil.getIntegerByObject(obj[18]));
        detailOrderItemsDto.setNotes(ValueUtil.getStringByObject(obj[19]));
        detailOrderItemsDto.setStatus(ValueUtil.getIntegerByObject(obj[20]));
        detailOrderItemsDto.setTypeOrder(ValueUtil.getIntegerByObject(obj[21]));
        detailOrderItemsDto.setCodeRoom(ValueUtil.getStringByObject(obj[22]));
        detailOrderItemsDto.setTitleRoom(ValueUtil.getStringByObject(obj[23]));
        return detailOrderItemsDto;
    }

    private DetailOrderDto writeDataDetailOrder(Object[] obj) {
        DetailOrderDto detailOrderDto = new DetailOrderDto();
        detailOrderDto.setIdOrder(ValueUtil.getIntegerByObject(obj[0]));
        detailOrderDto.setTitleOrder(ValueUtil.getStringByObject(obj[1]));
        detailOrderDto.setCodeOrder(ValueUtil.getStringByObject(obj[2]));
        detailOrderDto.setStatus(ValueUtil.getIntegerByObject(obj[3]));
        detailOrderDto.setTotalMoney(ValueUtil.getStringByObject(obj[4]));
        detailOrderDto.setTimeCreated(ValueUtil.getLongByObject(obj[5]));
        detailOrderDto.setTimeModified(ValueUtil.getLongByObject(obj[6]));
        detailOrderDto.setValue(ValueUtil.getStringByObject(obj[7]));
        return detailOrderDto;
    }

    private Orders writeDataOrders(Object[] obj) {
        Orders orders = new Orders();
        orders.setIdOrder(ValueUtil.getIntegerByObject(obj[0]));
        orders.setTitleOrder(ValueUtil.getStringByObject(obj[1]));
        orders.setCodeOrder(ValueUtil.getStringByObject(obj[2]));
        orders.setIdUser(ValueUtil.getIntegerByObject(obj[3]));
        orders.setStatusOrder(ValueUtil.getIntegerByObject(obj[4]));
        orders.setTimeCreated(ValueUtil.getLongByObject(obj[5]));
        orders.setTimeModified(ValueUtil.getLongByObject(obj[6]));
        orders.setIdTransactionPayment(ValueUtil.getIntegerByObject(obj[7]));
        orders.setIdUserCreated(ValueUtil.getIntegerByObject(obj[8]));
        orders.setIdUserModified(ValueUtil.getIntegerByObject(obj[9]));
        orders.setTotalMoney(ValueUtil.getStringByObject(obj[10]));
        orders.setTypeOrder(ValueUtil.getIntegerByObject(obj[11]));
        return orders;
    }
}
