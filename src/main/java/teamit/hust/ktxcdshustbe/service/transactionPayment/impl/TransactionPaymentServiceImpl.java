package teamit.hust.ktxcdshustbe.service.transactionPayment.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import teamit.hust.ktxcdshustbe.entity.TransactionPayment;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.exception.ValidParametersException;
import teamit.hust.ktxcdshustbe.repository.transactionPayment.TransactionPaymentRepository;
import teamit.hust.ktxcdshustbe.service.transactionPayment.TransactionPaymentService;

import java.util.Optional;

@Service
public class TransactionPaymentServiceImpl implements TransactionPaymentService {

    @Autowired
    TransactionPaymentRepository transactionPaymentRepository;

    @Override
    public TransactionPayment saveTransactionPayment(TransactionPayment transactionPayment) {
        return transactionPaymentRepository.save(transactionPayment);
    }

    @Override
    public TransactionPayment findByIdOrderAndStatusAndType(Integer orderId, Integer status, Integer type) {
        Optional<TransactionPayment> transactionPayment =
                transactionPaymentRepository.findByIdOrderAndStatusAndType(orderId, status, type);
        if (transactionPayment.isEmpty()){
            throw new NotFoundException();
        }
        return transactionPayment.get();
    }
}
