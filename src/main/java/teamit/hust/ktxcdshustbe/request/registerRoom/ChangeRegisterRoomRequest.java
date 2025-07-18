package teamit.hust.ktxcdshustbe.request.registerRoom;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ChangeRegisterRoomRequest {


    @NotNull
    @JsonProperty("studentRegisterRoomId")
    private Integer studentRegisterRoomId;
    @NotNull
    @JsonProperty("codeRoom")
    private String codeRoom;
}
