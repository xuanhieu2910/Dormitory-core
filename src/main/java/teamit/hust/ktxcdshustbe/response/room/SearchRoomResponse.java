package teamit.hust.ktxcdshustbe.response.room;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchRoomResponse {

    @JsonProperty("departmentId")
    private Integer departmentId;
    @JsonProperty("titleDepartment")
    private String titleDepartment;
    @JsonProperty("roomId")
    private Integer roomId;
    @JsonProperty("titleRoom")
    private String titleRoom;
    @JsonProperty("codeDepartment")
    private String codeDepartment;
    @JsonProperty("codeRoom")
    private String codeRoom;
    @JsonProperty("price")
    private String price;
    @JsonProperty("capacity")
    private Integer capacity;
    @JsonProperty("quantity")
    private Integer quantity;
    @JsonProperty("remainQuantity")
    private Integer remainQuantity;
    @JsonProperty("sex")
    private Integer sex;
    @JsonProperty("status")
    private String status;
}
