package teamit.hust.ktxcdshustbe.dto.registerRoom;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AcceptStudentRegisterRoomDto {

    private String userName;
    private String titleDepartment;
    private String titleRoom;
    private Long timeCreated;
    private String timeHired;
    private Integer statusAccept;
    private String reason;
    private String price;
    
}
