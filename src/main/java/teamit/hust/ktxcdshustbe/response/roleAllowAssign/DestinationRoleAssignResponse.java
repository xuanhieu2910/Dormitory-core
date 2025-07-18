package teamit.hust.ktxcdshustbe.response.roleAllowAssign;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DestinationRoleAssignResponse {

    @JsonProperty("id_destination_role_assign")
    private Integer idDestinationRoleAssign;
    @JsonProperty("name_destination_role_assign")
    private String nameDestinationRoleAssign;
    @JsonProperty("status")
    private Integer status;
}
