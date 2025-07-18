package teamit.hust.ktxcdshustbe.response.roleAllowAssign;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SourceRoleAssignResponse {

    @JsonProperty("id_source_role_assign")
    private Integer idSourceRoleAssign;
    @JsonProperty("name_source_role_assign")
    private String nameSourceRoleAssign;
}
