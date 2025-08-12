package teamit.hust.ktxcdshustbe.request.user;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateProfileUserRequest {
    private String codeUser;
    private String username;
    private String password;
    private Integer sex;
    private Integer isActive;
    private String typeLogin;
    private String value;
    private Integer idYearGroup;
    private Integer idPriorityGroup;
}
