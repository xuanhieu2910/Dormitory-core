package teamit.hust.ktxcdshustbe.response.registerRoom;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticStudentRegisterRoomResponse {

    @JsonProperty("total_student")
    private Integer totalStudent = 0;
    @JsonProperty("total_not_payment")
    private Integer totalNotPayment = 0;
    @JsonProperty("total_payment")
    private Integer totalPayment = 0;
}
