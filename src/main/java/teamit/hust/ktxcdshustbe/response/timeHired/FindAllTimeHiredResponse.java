package teamit.hust.ktxcdshustbe.response.timeHired;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindAllTimeHiredResponse {
    @JsonProperty("time_hired")
    private String timeHired;
    @JsonProperty("code_time_hired")
    private String codeTimeHired;
    @JsonProperty("id_time_hired")
    private Integer idTimeHired;
    @JsonProperty("time_started")
    private String timeStarted;
    @JsonProperty("time_end")
    private String timeEnd;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("time_created")
    private String timeCreated;
    @JsonProperty("time_modified")
    private String timeModified;
    @JsonProperty("id_user_created")
    private Integer idUserCreated;
    @JsonProperty("id_user_modified")
    private String idUserModified;
    @JsonProperty("title_time_hired")
    private String titleTimeHired;
}
