package teamit.hust.ktxcdshustbe.request.room;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamit.hust.ktxcdshustbe.request.RequestPageBase;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindAllRoomsForRentRequest extends RequestPageBase {
    private String codeDepartment;
    private String titleRoom;
    private Integer gender;
}
