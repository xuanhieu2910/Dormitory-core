package teamit.hust.ktxcdshustbe.response.roleAllowAssign;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindRestRoleResponse {

    @JsonProperty("id_role")
    private Integer idRole;
    @JsonProperty("name_role")
    private String nameRole;
}
