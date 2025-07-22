package teamit.hust.ktxcdshustbe.dto.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentSearchRoomDto {

    private String codeDepartment;
    private String titleDepartment;
    private String codeRoom;
    private String titleRoom;
    private String price;
    private Integer limitAmountPeopleRegister;
    private Integer sex;
    private Integer remainAmountRegister;
}
