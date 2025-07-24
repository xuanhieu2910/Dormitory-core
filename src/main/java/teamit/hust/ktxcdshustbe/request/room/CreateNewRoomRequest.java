package teamit.hust.ktxcdshustbe.request.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamit.hust.ktxcdshustbe.request.serviceRoom.CreateNewServiceRoomRequest;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class  CreateNewRoomRequest {

    private String codeDepartment;
    private String title;
    private Integer sexRoom;
    private String price;
    private Integer limitAmountPeople;
    private Integer limitAmountPeopleRegister;
    private Integer status;
    private List<CreateNewServiceRoomRequest> servicesRoom;
}
