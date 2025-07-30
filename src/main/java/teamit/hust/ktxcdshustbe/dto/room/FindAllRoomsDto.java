package teamit.hust.ktxcdshustbe.dto.room;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindAllRoomsDto {


    private Integer idRoom;
    private String title;
    private Integer idDepartment;
    private String price;
    private Integer sexRoom;
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
    private String codeDepartment;
    private String titleDepartment;
    private String userNameCreated;
    private String userNameModified;
    private String valueCreated;
    private String valueModified;
    private Integer statusRegister;
}
