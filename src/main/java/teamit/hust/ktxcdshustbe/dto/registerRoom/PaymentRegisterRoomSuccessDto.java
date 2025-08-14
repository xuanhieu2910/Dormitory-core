package teamit.hust.ktxcdshustbe.dto.registerRoom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRegisterRoomSuccessDto {
    private String userName;
    private String titleDepartment;
    private String titleRoom;
    private String money;
}
