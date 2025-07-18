package teamit.hust.ktxcdshustbe.request.studentRoom;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferRoomRequest {

    private String codeUser;
    private String originalCodeRoom;
    private String destinationCodeRoom;
}
