package teamit.hust.ktxcdshustbe.response.studentRoom;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ListStudentHiredRoomResponse {

    @JsonProperty("id_student_room")
    private Integer idStudentRoom;
    @JsonProperty("code_user")
    private String codeUser;
    @JsonProperty("value_user")
    private String valueUser;
    @JsonProperty("time_hired")
    private String timeHired;
    @JsonProperty("code_department")
    private String codeDepartment;
    @JsonProperty("title_department")
    private String titleDepartment;
    @JsonProperty("code_room")
    private String codeRoom;
    @JsonProperty("title_Room")
    private String titleRoom;
    @JsonProperty("code_user_modified")
    private String codeUserModified;
    @JsonProperty("value_user_modified")
    private String valueUserModified;
    @JsonProperty("status")
    private Integer status;
}
