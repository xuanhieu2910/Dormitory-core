package teamit.hust.ktxcdshustbe.dto.orderItems;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailOrderItemsDto {
    private Integer idOrderItem;
    private String codeOrderItem;
    private Integer idOrder;
    private Integer idObject;
    private Integer idUser;
    private Integer quantity;
    private String totalMoney;
    private Long timeCreated;
    private Long timeModified;
    private Integer idUserCreated;
    private Integer idUserModified;
    private String notes;
    private Integer status;
    private Integer typeOrder;
    private String codeRoom;
    private String titleRoom;
}
