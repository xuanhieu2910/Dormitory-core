package teamit.hust.ktxcdshustbe.dto.bankingService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderToGetBillDto {

    private String id;
    private String amount;
    private String currency = "VNĐ";
    private String description;
    private List<OrderItemsToGetBillDto> items;

}
