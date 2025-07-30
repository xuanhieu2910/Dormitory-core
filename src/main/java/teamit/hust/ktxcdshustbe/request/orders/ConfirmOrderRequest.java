package teamit.hust.ktxcdshustbe.request.orders;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmOrderRequest {
    private String typePayment;
    private String codeOrder;
}
