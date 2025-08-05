package teamit.hust.ktxcdshustbe.service.paymentService;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.util.MultiValueMap;
import teamit.hust.ktxcdshustbe.request.transactionPayment.CallBackPaymentRequest;

import java.util.Map;

public interface PaymentService {

    void callBackPayment(Map<String,Object> request) throws JsonProcessingException;
}
