package teamit.hust.ktxcdshustbe.request.studentRoom;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RemoveStudentInRoomRequest {
    @JsonProperty("codeUser")
    private String codeUser;
    @JsonProperty("codeRoom")
    private String codeRoom;
}
