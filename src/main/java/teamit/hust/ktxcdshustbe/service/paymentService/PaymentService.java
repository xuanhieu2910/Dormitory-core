package teamit.hust.ktxcdshustbe.service.paymentService;

import com.fasterxml.jackson.core.JsonProcessingException;
import teamit.hust.ktxcdshustbe.request.transactionPayment.CallBackPaymentRequest;

public interface PaymentService {

    void callBackPayment(CallBackPaymentRequest request) throws JsonProcessingException;
}
