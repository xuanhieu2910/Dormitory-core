package teamit.hust.ktxcdshustbe.response.roleAllowAssign;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FindAllRoleAllowResponse {

    @JsonProperty("id_role")
    private Integer idRole;
    @JsonProperty("name_role")
    private String nameRole;
}
