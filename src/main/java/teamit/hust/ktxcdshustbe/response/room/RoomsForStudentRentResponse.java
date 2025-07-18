package teamit.hust.ktxcdshustbe.response.room;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoomsForStudentRentResponse {

    @JsonProperty("code_room")
    private String codeRoom;
    @JsonProperty("title_room")
    private String titleRoom;
    @JsonProperty("price")
    private String price;
    @JsonProperty("limit_amount_people_register")
    private Integer limitAmountPeopleRegister;
    @JsonProperty("quantity_register")
    private Integer quantityRegister;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("remain_amount_register")
    private Integer remainAmountRegister;
    @JsonProperty("time_created")
    private Long timeCreated;
    @JsonProperty("time_modified")
    private Long timeModified;
    @JsonProperty("code_department")
    private String codeDepartment;
    @JsonProperty("title_department")
    private String titleDepartment;
}
