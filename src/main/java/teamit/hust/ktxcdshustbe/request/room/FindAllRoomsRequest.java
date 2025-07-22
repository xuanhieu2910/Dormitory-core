package teamit.hust.ktxcdshustbe.request.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import teamit.hust.ktxcdshustbe.request.RequestPageBase;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindAllRoomsRequest extends RequestPageBase {

    private String titleRoom;
    private Integer status;
    private Integer sex;
    private Integer statusRemain;
    private String codeDepartment;
}
