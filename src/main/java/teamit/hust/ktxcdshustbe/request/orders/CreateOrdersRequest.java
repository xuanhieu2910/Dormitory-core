package teamit.hust.ktxcdshustbe.request.orders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrdersRequest {

    private String titleOrder;
    private String codeRoom;
    private String methodPayment;

}
