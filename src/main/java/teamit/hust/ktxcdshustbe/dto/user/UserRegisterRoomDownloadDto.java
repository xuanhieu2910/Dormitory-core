package teamit.hust.ktxcdshustbe.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterRoomDownloadDto {
    private Integer idRegisterRoom;
    private String codeUser;
    private String value;
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
    private String titleOrders;
    private String codeOrders;
    private String totalMoney;
}
