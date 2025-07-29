package teamit.hust.ktxcdshustbe.repository.transactionPayment;

import teamit.hust.ktxcdshustbe.entity.TransactionPayment;

import java.util.Optional;

public interface TransactionPaymentRepositoryCustom {
    Optional<TransactionPayment> findByIdOrderAndStatusAndType(Integer idOrder, Integer status, Integer type);
}
