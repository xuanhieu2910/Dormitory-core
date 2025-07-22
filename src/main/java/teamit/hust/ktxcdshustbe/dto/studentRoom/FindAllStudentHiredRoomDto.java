package teamit.hust.ktxcdshustbe.dto.studentRoom;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindAllStudentHiredRoomDto {
    private Integer idStudentRoom;
    private String codeUser;
    private String valueUser;
    private String timeHired;
    private String codeDepartment;
    private String titleDepartment;
    private String codeRoom;
    private String titleRoom;
    private String codeUserModified;
    private String valueUserModified;
}
