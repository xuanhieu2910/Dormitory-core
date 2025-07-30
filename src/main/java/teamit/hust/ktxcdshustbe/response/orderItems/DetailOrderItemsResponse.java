package teamit.hust.ktxcdshustbe.response.orderItems;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailOrderItemsResponse {

    @JsonProperty("code_order_item")
    private String codeOrderItem;
    @JsonProperty("code_room")
    private String codeRoom;
    @JsonProperty("title_room")
    private String titleRoom;
    @JsonProperty("quantity")
    private Integer quantity;
    @JsonProperty("total_money")
    private String totalMoney;

}
