package teamit.hust.ktxcdshustbe.request.room;

import lombok.Data;
import lombok.NoArgsConstructor;
import teamit.hust.ktxcdshustbe.request.RequestPageBase;

@Data
@NoArgsConstructor
public class SearchInformationRegisterRoomRequest extends RequestPageBase {

    private String codeDepartment;
    private String titleRoom;
    private String titleSemester;
    private Long timeRegistered;
    private Integer statusPayment;
}
