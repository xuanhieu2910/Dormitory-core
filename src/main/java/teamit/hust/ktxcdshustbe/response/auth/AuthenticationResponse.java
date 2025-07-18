package teamit.hust.ktxcdshustbe.response.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import teamit.hust.ktxcdshustbe.response.role.RoleResponse;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthenticationResponse {

    @JsonProperty("code_user")
    private String codeUser;
    @JsonProperty("user_name")
    private String userName;
    @JsonProperty("roles")
    private List<RoleResponse> roles;
    @JsonProperty("status_register_room")
    private Integer statusRegisterRoom;
    @JsonProperty("full_name")
    private String fullName;

}
