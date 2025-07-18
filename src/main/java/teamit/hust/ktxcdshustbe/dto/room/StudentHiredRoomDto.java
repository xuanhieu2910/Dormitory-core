package teamit.hust.ktxcdshustbe.dto.room;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class StudentHiredRoomDto {

    private Integer userId;
    private String fullName;
    private String numberStudent;
    private String phoneNumber;
    private Integer yearGrade;
    private String classUser;
    private Integer timeHiredId;
    private Timestamp timeStared;
    private Timestamp timeEnded;
    private Integer status;
    private Integer semesterId;
    private String titleSemester;
}
