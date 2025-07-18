package teamit.hust.ktxcdshustbe.response.registerRoom;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InformationRoomRegisterResponse {
    @JsonProperty("room")
    private String room;
    @JsonProperty("department")
    private String department;
    @JsonProperty("semester")
    private String semester;
    @JsonProperty("time_register")
    private String timeRegister;
    @JsonProperty("status_register_room")
    private Integer statusRegisterRoom;
    @JsonProperty("path_payment")
    private String pathPayment;
}
