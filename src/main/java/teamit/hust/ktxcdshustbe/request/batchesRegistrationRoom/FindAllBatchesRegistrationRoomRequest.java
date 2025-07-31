package teamit.hust.ktxcdshustbe.request.batchesRegistrationRoom;

import lombok.Getter;
import lombok.Setter;
import teamit.hust.ktxcdshustbe.request.RequestPageBase;

@Getter
@Setter
public class FindAllBatchesRegistrationRoomRequest extends RequestPageBase {

    private String codeBatchesRegisterRoom;
    private String codeDepartment;
    private String titleRoom;
    private Integer status;
    private Integer sex;
    private Integer statusRemain;
}
