package teamit.hust.ktxcdshustbe.request.userRole;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddNewUserRoleDetailsRequest {

    @JsonProperty("id_role")
    private Integer idRole;
    @JsonProperty("title_role")
    private String titleRole;
}
