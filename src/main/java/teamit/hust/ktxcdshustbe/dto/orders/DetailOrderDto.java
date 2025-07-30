package teamit.hust.ktxcdshustbe.dto.orders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamit.hust.ktxcdshustbe.dto.orderItems.DetailOrderItemsDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailOrderDto {
    private Integer idOrder;
    private String titleOrder;
    private String codeOrder;
    private Integer status;
    private String totalMoney;
    private Long timeCreated;
    private Long timeModified;
    private String value;
    private List<DetailOrderItemsDto> items;
}
