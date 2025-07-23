package teamit.hust.ktxcdshustbe.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamit.hust.ktxcdshustbe.response.role.RoleResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AuthenticationDto {

    private String codeUser;
    private String userName;
    private List<RoleResponse> roles;
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Integer isActive;
}
