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
    @JsonProperty("password")
    private String password;
    @JsonProperty("sex")
    private Integer sex;
    @JsonProperty("is_active")
    private Integer isActive;
    @JsonProperty("type_login")
    private String typeLogin;
    @JsonProperty("title_year_group")
    private String titleYearGroup;
    @JsonProperty("title_priority_group")
    private String titlePriorityGroup;
    @JsonProperty("is_initialize")
    private Integer isInitialize;
}
