package teamit.hust.ktxcdshustbe.request.userInstance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInstanceRequest {
    @JsonProperty("id_user_instance")
    private Integer idUserInstance;
    @JsonProperty("value")
    private String value;
    @JsonProperty("error")
    private Integer error;
}
