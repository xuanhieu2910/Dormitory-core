package teamit.hust.ktxcdshustbe.repository.orders.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.entity.Orders;
import teamit.hust.ktxcdshustbe.repository.orders.OrdersRepositoryCustom;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

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
