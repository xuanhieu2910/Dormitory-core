package teamit.hust.ktxcdshustbe.repository.transactionPayment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamit.hust.ktxcdshustbe.entity.TransactionPayment;

@Repository
public interface TransactionPaymentRepository extends JpaRepository<TransactionPayment, Integer>,
        TransactionPaymentRepositoryCustom {

}
