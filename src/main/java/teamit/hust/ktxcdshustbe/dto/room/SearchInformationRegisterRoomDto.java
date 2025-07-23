package teamit.hust.ktxcdshustbe.dto.room;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class SearchInformationRegisterRoomDto {


    private Integer codeUser;
    private String userName;
    private String numberStudent;
    private String phoneNumber;
    private Integer yearGrade;
    private String classUser;
    private Integer timeHiredId;
    private Timestamp timeStarted;
    private Timestamp timeEnded;
    private Integer status;
    private Integer semesterId;
    private String titleSemester;
    private String titleRoom;
    private String codeDepartment;
}
