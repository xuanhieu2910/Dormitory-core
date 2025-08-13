package teamit.hust.ktxcdshustbe.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApprovedUserRegisterRoomRequest {

    @JsonProperty("idStudentRegisterRoom")
    private Integer idStudentRegisterRoom;
    @JsonProperty("status")
    private Integer status;
}
