package teamit.hust.ktxcdshustbe.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApprovedUserRegisterRoomRequest {

    @JsonProperty("codeUserRegister")
    private String codeUserRegister;
    @JsonProperty("status")
    private Integer status;
}
