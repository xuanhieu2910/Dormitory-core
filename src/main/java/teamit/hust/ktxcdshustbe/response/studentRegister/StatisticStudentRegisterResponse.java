package teamit.hust.ktxcdshustbe.response.studentRegister;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticStudentRegisterResponse {
    @JsonProperty("total_student_register")
    private Integer totalStudentRegister;
    @JsonProperty("total_student_register_not_yet_paid")
    private Integer totalStudentRegisterNotYetPaid;
    @JsonProperty("total_student_register_paid")
    private Integer totalStudentRegisterPaid;
}
