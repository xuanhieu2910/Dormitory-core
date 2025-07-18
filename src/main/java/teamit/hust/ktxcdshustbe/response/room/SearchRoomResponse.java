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
}
