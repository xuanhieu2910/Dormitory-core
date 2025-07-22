package teamit.hust.ktxcdshustbe.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DetailInformationUserResponse {

    @JsonProperty("code_user")
    private String codeUser;
    @JsonProperty("value")
    private String value;

}
