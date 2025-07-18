package teamit.hust.ktxcdshustbe.response.department;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DepartmentStatisticDetailResponse {


    @JsonProperty("title_department")
    private String titleDepartment;
    @JsonProperty("total_student_hiring")
    private Integer totalStudentHiring;
    @JsonProperty("total_student_register")
    private Integer totalStudentRegister;
    @JsonProperty("total_student_register_not_payment")
    private Integer totalStudentRegisterNotPayment;
    @JsonProperty("total_student_register_payment")
    private Integer totalStudentRegisterPayment;
    @JsonProperty("total_room")
    private Integer totalRoom;
    @JsonProperty("total_room_active")
    private Integer totalRoomActive;
    @JsonProperty("total_room_un_active")
    private Integer totalRoomUnActive;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("code_department")
    private String codeDepartment;
}
