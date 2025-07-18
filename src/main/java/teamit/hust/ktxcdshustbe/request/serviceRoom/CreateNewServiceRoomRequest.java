package teamit.hust.ktxcdshustbe.request.serviceRoom;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateNewServiceRoomRequest {

    private String codeService;
    private Integer status;
}
