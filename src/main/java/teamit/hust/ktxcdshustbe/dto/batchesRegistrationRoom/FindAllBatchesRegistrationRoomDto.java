package teamit.hust.ktxcdshustbe.dto.batchesRegistrationRoom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindAllBatchesRegistrationRoomDto {

    private Integer idRoom;
    private String title;
    private Integer idDepartment;
    private Integer sexRoom;
    private String price;
    private Long timeCreated;
    private Long timeModified;
    private Integer idUserCreated;
    private Integer idUserModified;
    private Integer isActive;
    private Integer limitAmountPeople;
    private Integer quantityHired;
    private Integer remainAmount;
    private Integer limitAmountPeopleRegister;
    private Integer quantityRegistered;
    private Integer remainAmountRegister;
    private String codeRoom;

}
