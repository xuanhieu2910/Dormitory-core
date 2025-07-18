package teamit.hust.ktxcdshustbe.response.studentRegister;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudentRegisterRoomResponse {

    @JsonProperty("id_student_register_room")
    private Integer studentRegisterRoomId;
    @JsonProperty("code_room")
    private String codeRoom;
    @JsonProperty("title_room")
    private String titleRoom;
    @JsonProperty("code_department")
    private String codeDepartment;
    @JsonProperty("title_department")
    private String titleDepartment;
    @JsonProperty("time_hired")
    private String timeHired;
    @JsonProperty("time_created")
    private String timeCreated;
    @JsonProperty("price")
    private String price;
    @JsonProperty("status_student_register_room")
    private Integer statusStudentRegisterRoom;
}
