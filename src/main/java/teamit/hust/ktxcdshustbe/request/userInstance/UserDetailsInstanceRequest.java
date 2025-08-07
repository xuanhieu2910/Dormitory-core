package teamit.hust.ktxcdshustbe.request.userInstance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsInstanceRequest {
    private String username;
    private Integer sex;
    private String typeLogin;
    private String value;
    private String codeYearGroup;
    private String codePriorityGroup;
}
