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
    private Integer idStudentRegisterRoom;
    @JsonProperty("code_room")
    private String codeRoom;
    @JsonProperty("title_room")
    private String titleRoom;
    @JsonProperty("code_department")
    private String codeDepartment;
    @JsonProperty("title_department")
    private String titleDepartment;
    @JsonProperty("time_created")
    private Long timeCreated;
    @JsonProperty("time_hired_started")
    private Long timeHiredStarted;
    @JsonProperty("time_hired_ended")
    private Long timeHiredEnded;
    @JsonProperty("price")
    private String price;
    @JsonProperty("status_student_register_room")
    private Integer statusStudentRegisterRoom;
    @JsonProperty("expires_at")
    private Long expiresAt;
    @JsonProperty("code_orders")
    private String codeOrders;
}
