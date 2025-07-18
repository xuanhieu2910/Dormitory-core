package teamit.hust.ktxcdshustbe.request.studentRegister;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamit.hust.ktxcdshustbe.request.RequestPageBase;

@Getter
@Setter
@NoArgsConstructor
public class StudentRegisterRoomRequest extends RequestPageBase {

    private String codeDepartment;
    private Integer gender;

}
