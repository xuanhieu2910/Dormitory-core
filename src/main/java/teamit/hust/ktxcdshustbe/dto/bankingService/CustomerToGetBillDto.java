package teamit.hust.ktxcdshustbe.dto.bankingService;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerToGetBillDto {

    private String first_name = "";
    private String last_name = "";
    private String phone_number = "";
    private String email = "";
}
