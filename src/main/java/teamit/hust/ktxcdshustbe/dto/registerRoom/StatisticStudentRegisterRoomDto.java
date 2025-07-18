package teamit.hust.ktxcdshustbe.dto.registerRoom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticStudentRegisterRoomDto {

    private Integer totalStudent = 0;
    private Integer totalNotPayment = 0;
    private Integer totalPayment = 0;
}
