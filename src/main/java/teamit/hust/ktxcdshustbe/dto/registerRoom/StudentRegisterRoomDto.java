package teamit.hust.ktxcdshustbe.dto.registerRoom;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentRegisterRoomDto {

    private Integer idStudentRegisterRoom;
    private String codeRoom;
    private String titleRoom;
    private String codeDepartment;
    private String titleDepartment;
    private Long timeCreated;
    private Long timeHiredStarted;
    private Long timeHiredEnded;
    private String price;
    private Integer statusStudentRegisterRoom;
    private Long expiresAt;
}
