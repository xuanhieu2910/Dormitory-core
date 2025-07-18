package teamit.hust.ktxcdshustbe.request.user;

import lombok.*;
import teamit.hust.ktxcdshustbe.request.RequestPageBase;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRoomRequest extends RequestPageBase {

    private String codeDepartment;
    private String codeRoom;
    private String codeSemester;
    private Integer yearGrade;
    private String timeStarted;
    private String timeEnded;
    private Integer status;
}
