package teamit.hust.ktxcdshustbe.dto.bankingService;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DeviceToGetBillDto {

    private String os = "Ubuntu";
    private String app = "Chrome";
    private String id = UUID.nameUUIDFromBytes((this.app + this.os).getBytes()).toString();
}
