package teamit.hust.ktxcdshustbe.dto.user;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class UserRegisterRoomDto {

    private Integer idRegisterRoom;
    private String codeUser;
    private String value;
    private Long timeRegister;
    private String codeDepartment;
    private String titleDepartment;
    private String codeRoom;
    private String titleRoom;
    private Integer idTimeHired;
    private String timeHiredStarted;
    private String timeHiredEnded;
    private String codeSemester;
    private String titleSemester;
    private Integer statusInformationRegister;

}
