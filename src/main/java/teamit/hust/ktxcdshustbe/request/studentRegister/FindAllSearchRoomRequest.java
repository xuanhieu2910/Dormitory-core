package teamit.hust.ktxcdshustbe.request.studentRegister;

import lombok.Getter;
import lombok.Setter;
import teamit.hust.ktxcdshustbe.request.RequestPageBase;

@Getter
@Setter
public class FindAllSearchRoomRequest  extends RequestPageBase {

    private String codeDepartment;
    private String titleRoom;

}
