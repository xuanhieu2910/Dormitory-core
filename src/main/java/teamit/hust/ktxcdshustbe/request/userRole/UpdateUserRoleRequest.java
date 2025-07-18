package teamit.hust.ktxcdshustbe.request.userRole;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserRoleRequest {

    @JsonProperty("id_user_role")
    private Integer idUserRole;
    @JsonProperty("id_role")
    private Integer idRole;
}
