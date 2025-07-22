package teamit.hust.ktxcdshustbe.request.room;

import lombok.Getter;
import lombok.Setter;
import teamit.hust.ktxcdshustbe.request.RequestPageBase;


@Getter
@Setter
public class StudentSearchRoomRequest extends RequestPageBase {

    private String codeDepartment;
    private String titleRoom;
    private Integer gender;

}
