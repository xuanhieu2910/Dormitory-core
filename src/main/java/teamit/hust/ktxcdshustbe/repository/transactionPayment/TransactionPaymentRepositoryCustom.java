package teamit.hust.ktxcdshustbe.repository.transactionPayment;

import teamit.hust.ktxcdshustbe.entity.TransactionPayment;

import java.util.Optional;

public interface TransactionPaymentRepositoryCustom {
    Optional<TransactionPayment> findByCheckSumAndStatusAndType(String checkSum, Integer status, Integer type);

    Optional<TransactionPayment> findTransactionPaymentByIdOrderAndType(Integer idOrder, Integer type);
}
