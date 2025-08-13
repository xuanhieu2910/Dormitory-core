package teamit.hust.ktxcdshustbe.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;

@Getter
@Setter
@NoArgsConstructor
public class FindAllStudentsResponse {

    @JsonProperty("code_user")
    private String codeUser;
    @JsonProperty("user_name")
    private String userName;
    @JsonProperty("value")
    private String value;
    @JsonProperty("sex")
    private Integer sex;
    @JsonProperty("code_room")
    private String codeRoom;
    @JsonProperty("title_room")
    private String titleRoom;
    @JsonProperty("title_year_group")
    private String titleYearGroup;
    @JsonProperty("title_priority_group")
    private String titlePriorityGroup;

}
