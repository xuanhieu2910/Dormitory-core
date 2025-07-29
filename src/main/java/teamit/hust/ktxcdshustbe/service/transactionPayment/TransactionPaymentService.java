package teamit.hust.ktxcdshustbe.service.transactionPayment;

import teamit.hust.ktxcdshustbe.entity.TransactionPayment;

public interface TransactionPaymentService {

    TransactionPayment saveTransactionPayment(TransactionPayment transactionPayment);

    TransactionPayment findByIdOrderAndStatusAndType(Integer orderId, Integer status, Integer type);
}
