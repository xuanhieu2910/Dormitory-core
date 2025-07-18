package teamit.hust.ktxcdshustbe.request.serviceRoom;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditServiceRoomRequest {

    private String codeService;
    private Integer status;

}
