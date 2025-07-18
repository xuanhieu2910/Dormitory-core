package teamit.hust.ktxcdshustbe.response.role;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FindAllRoleResponse {

    @JsonProperty("id_role")
    private Integer idRole;
    @JsonProperty("title_role")
    private String titleRole;
    @JsonProperty("content_name")
    private String contentName;
    @JsonProperty("description")
    private String description;
    @JsonProperty("status")
    private Integer status;
}
