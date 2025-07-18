package teamit.hust.ktxcdshustbe.response.room;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class FindAllRoomsResponse {

    @JsonProperty("title")
    private String title;
    @JsonProperty("price")
    private String price;
    @JsonProperty("limit_amount_people_hired")
    private Integer limitAmountPeopleHired;
    @JsonProperty("quantity_hired")
    private Integer quantityHired;
    @JsonProperty("sex")
    private String sex;
    @JsonProperty("remain_amount")
    private Integer remainAmount;
    @JsonProperty("is_active")
    private Integer isActive;
    @JsonProperty("code_room")
    private String codeRoom;
}
