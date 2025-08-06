package teamit.hust.ktxcdshustbe.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ListHiredRoomStudentResponse {

    @JsonProperty("id_student_room")
    private Integer studentRoomId;
    @JsonProperty("code_user")
    private String codeUser;
    @JsonProperty("code_department")
    private String codeDepartment;
    @JsonProperty("title_department")
    private String titleDepartment;
    @JsonProperty("code_room")
    private String codeRoom;
    @JsonProperty("title_room")
    private String titleRoom;
    @JsonProperty("hired_room")
    private String hiredRoom;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("sex")
    private Integer sex;
}
