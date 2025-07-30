package teamit.hust.ktxcdshustbe.dto.bankingService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemsToGetBillDto {
    private String id;
    private String name;
    private String description;
    private String unit_price;
    private String quantity;

}
