package teamit.hust.ktxcdshustbe.repository.transactionPayment.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.entity.TransactionPayment;
import teamit.hust.ktxcdshustbe.repository.transactionPayment.TransactionPaymentRepositoryCustom;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.List;
import java.util.Optional;

public class TransactionPaymentRepositoryImpl implements TransactionPaymentRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public Optional<TransactionPayment> findByIdOrderAndStatusAndType(Integer orderId, Integer status, Integer type) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select tp.id_transaction_payment, tp.id_order, tp.code_transaction_payment,  " +
                "       tp.status, tp.type, tp.time_created, tp.time_modified,  " +
                "       tp.json_data, tp.check_sum, tp.return_url, tp.cancel_url,  " +
                "       tp.method_payment, tp.merchant_id, tp.terminal_id  " +
                "from transaction_payment tp  " +
                "    inner join orders ord  " +
                "where tp.id_order = :idOrder  " +
                "and tp.status = :status " +
                "and tp.type = :type ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("idOrder", orderId);
        query.setParameter("status", status);
        query.setParameter("type", type);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj : result) {
                return Optional.of(writeDataTransactionPayment(obj));
            }
        }
        return Optional.empty();
    }

    private TransactionPayment writeDataTransactionPayment(Object[] obj) {
        TransactionPayment transactionPayment = new TransactionPayment();
        transactionPayment.setIdTransactionPayment(ValueUtil.getIntegerByObject(obj[0]));
        transactionPayment.setIdOrder(ValueUtil.getIntegerByObject(obj[1]));
        transactionPayment.setCodeTransactionPayment(ValueUtil.getStringByObject(obj[2]));
        transactionPayment.setStatus(ValueUtil.getIntegerByObject(obj[3]));
        transactionPayment.setType(ValueUtil.getIntegerByObject(obj[4]));
        transactionPayment.setTimeCreated(ValueUtil.getLongByObject(obj[5]));
        transactionPayment.setTimeModified(ValueUtil.getLongByObject(obj[6]));
        transactionPayment.setJsonData(ValueUtil.getStringByObject(obj[7]));
        transactionPayment.setCheckSum(ValueUtil.getStringByObject(obj[8]));
        transactionPayment.setReturnUrl(ValueUtil.getStringByObject(obj[9]));
        transactionPayment.setCancelUrl(ValueUtil.getStringByObject(obj[10]));
        transactionPayment.setMethodPayment(ValueUtil.getStringByObject(obj[11]));
        transactionPayment.setMerchantId(ValueUtil.getStringByObject(obj[12]));
        transactionPayment.setTerminalId(ValueUtil.getStringByObject(obj[13]));
        return transactionPayment;
    }
}
