package teamit.hust.ktxcdshustbe.request.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterAccountStudentRequest {

    @JsonProperty(namespace = "username",required = true)
    private String username;
    @JsonProperty(namespace = "password", required = true)
    private String password;

}
