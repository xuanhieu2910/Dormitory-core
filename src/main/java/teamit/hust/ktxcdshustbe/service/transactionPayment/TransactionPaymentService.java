package teamit.hust.ktxcdshustbe.service.transactionPayment;

import teamit.hust.ktxcdshustbe.entity.TransactionPayment;

public interface TransactionPaymentService {

    TransactionPayment saveTransactionPayment(TransactionPayment transactionPayment);

    TransactionPayment findByCheckSumAndStatusAndType(String checkSum, Integer status, Integer type);

    TransactionPayment findTransactionPaymentByIdOrderAndType(Integer idOrder, Integer type);
}
