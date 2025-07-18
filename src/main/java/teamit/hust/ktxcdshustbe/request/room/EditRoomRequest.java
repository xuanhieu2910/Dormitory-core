package teamit.hust.ktxcdshustbe.request.room;


import lombok.*;
import teamit.hust.ktxcdshustbe.request.serviceRoom.EditServiceRoomRequest;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditRoomRequest {

    private String codeDepartment;
    private String codeRoom;
    private Integer sexRoom;
    private String price;
    private String title;
    private Integer limitAmountPeople;
    private Integer status;
    private List<EditServiceRoomRequest> editServiceRoomRequest;
}
