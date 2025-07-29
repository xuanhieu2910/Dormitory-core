package teamit.hust.ktxcdshustbe.request.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamit.hust.ktxcdshustbe.request.RequestPageBase;

@Getter
@Setter
@NoArgsConstructor
public class StudentListRoomHiredRequest extends RequestPageBase {

    private String codeUser;
    private String timeStarted;
    private String timeEnded;
    private Integer departmentId;
    private Integer roomId;
}
