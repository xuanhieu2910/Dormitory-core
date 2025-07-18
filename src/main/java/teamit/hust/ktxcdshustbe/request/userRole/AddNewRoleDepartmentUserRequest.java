package teamit.hust.ktxcdshustbe.request.userRole;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NotNull
public class AddNewRoleDepartmentUserRequest {

    @JsonProperty("code_user")
    private String codeUser;
    @JsonProperty("roles")
    private List<AddNewUserRoleDetailsRequest> roles;

}
