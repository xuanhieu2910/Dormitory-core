package teamit.hust.ktxcdshustbe.response.semester;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DetailSemesterResponse {

    @JsonProperty("title")
    private String title;
    @JsonProperty("status")
    private String status;
    @JsonProperty("codeSemester")
    private String codeSemester;
    @JsonProperty("id_user_created")
    private Integer idUserCreated;
    @JsonProperty("id_user_modified")
    private Integer idUserModified;
    @JsonProperty("time_created")
    private Long timeCreated;
    @JsonProperty("time_modified")
    private Long timeModified;
    @JsonProperty("user_name_created")
    private String userNameCreated;
    @JsonProperty("user_name_modified")
    private String userNameModified;
    @JsonProperty("code_user_created")
    private String codeUserCreated;
    @JsonProperty("code_user_modified")
    private String codeUserModified;
    @JsonProperty("note")
    private String note;
}
