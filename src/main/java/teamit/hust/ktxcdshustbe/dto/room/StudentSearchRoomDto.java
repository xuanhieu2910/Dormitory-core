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


    private Integer idDepartment;
    private String codeDepartment;
    private String titleDepartment;
    private String timeHired;
    private Integer idRoom;
    private String codeRoom;
    private String titleRoom;
    private String price;
    private Integer limitationAmountRegisterRoom;
    private Integer sexRoom;
    private Integer remainAmountRegisterRoom;
}
