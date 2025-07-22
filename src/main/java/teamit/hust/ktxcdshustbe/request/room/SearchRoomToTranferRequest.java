package teamit.hust.ktxcdshustbe.request.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchRoomToTranferRequest {

    @JsonProperty("codeDepartment")
    private String codeDepartment;
    @JsonProperty("codeRoom")
    private String codeRoom;
    @JsonProperty("sexUser")
    private Integer sexUser;
}
