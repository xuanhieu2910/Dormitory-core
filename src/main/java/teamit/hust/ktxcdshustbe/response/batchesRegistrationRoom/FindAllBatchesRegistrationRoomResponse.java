package teamit.hust.ktxcdshustbe.response.batchesRegistrationRoom;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindAllBatchesRegistrationRoomResponse {

    @JsonProperty("title_room")
    private String titleRoom;
    @JsonProperty("code_room")
    private String codeRoom;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("price")
    private String price;
    @JsonProperty("time_created")
    private Long timeCreated;
    @JsonProperty("time_modified")
    private Long timeModified;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("limit_amount_people")
    private Integer limitAmountPeople;
    @JsonProperty("quantity_hired")
    private Integer quantityHired;
    @JsonProperty("remain_amount")
    private Integer remainAmount;
    @JsonProperty("limit_amount_people_register")
    private Integer limitAmountPeopleRegister;
    @JsonProperty("quantity_registered")
    private Integer quantityRegistered;
    @JsonProperty("remain_amount_register")
    private Integer remainAmountRegister;

}
