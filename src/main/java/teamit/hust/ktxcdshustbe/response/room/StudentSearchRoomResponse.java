package teamit.hust.ktxcdshustbe.response.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentSearchRoomResponse {

    @JsonProperty("code_department")
    private String codeDepartment;
    @JsonProperty("title_department")
    private String titleDepartment;
    @JsonProperty("code_room")
    private String codeRoom;
    @JsonProperty("title_room")
    private String titleRoom;
    @JsonProperty("price")
    private String price;
    @JsonProperty("limit_amount_people_register")
    private Integer limitAmountPeopleRegister;
    @JsonProperty("sex")
    private String sex;
    @JsonProperty("remain_amount_register")
    private Integer remainAmountRegister;
}
