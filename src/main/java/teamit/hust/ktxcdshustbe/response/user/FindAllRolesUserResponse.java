package teamit.hust.ktxcdshustbe.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FindAllRolesUserResponse {

    @JsonProperty("id_role")
    private Integer idRole;
    @JsonProperty("role")
    private String role;
    @JsonProperty("picked")
    private Integer picked;
}
