package teamit.hust.ktxcdshustbe.response.orders;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import teamit.hust.ktxcdshustbe.response.orderItems.DetailOrderItemsResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailOrderResponse {

    @JsonProperty("title_order")
    private String titleOrder;
    @JsonProperty("code_order")
    private String codeOrder;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("total_money")
    private String totalMoney;
    @JsonProperty("time_created")
    private Long timeCreated;
    @JsonProperty("time_modified")
    private Long timeModified;
    @JsonProperty("items")
    private List<DetailOrderItemsResponse> items;

}
