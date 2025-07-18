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
    private String fullName;
    private String numberStudent;
    private String phoneNumber;
    private Long timeRegister;
    private String codeDepartment;
    private String titleDepartment;
    private String codeRoom;
    private String titleRoom;
    private Integer idTimeHired;
    private Long timeHiredStarted;
    private Long timeHiredEnded;
    private String codeSemester;
    private String titleSemester;
    private Integer statusInformationRegister;

}
