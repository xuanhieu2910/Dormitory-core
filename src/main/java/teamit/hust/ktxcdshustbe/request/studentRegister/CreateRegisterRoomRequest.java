package teamit.hust.ktxcdshustbe.request.studentRegister;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateRegisterRoomRequest {

    @JsonProperty(value = "codeRoom")
    private String codeRoom;
    @JsonProperty(value = "hiredId")
    private Integer hiredId;
}
