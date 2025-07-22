package teamit.hust.ktxcdshustbe.response.timeHired;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeHiredDetailsResponse {
    @JsonProperty("id_time_hired")
    private Integer idTimeHired;
    @JsonProperty("time_started")
    private String timeStarted;
    @JsonProperty("time_ended")
    private String timeEnded;
    @JsonProperty("time_hired")
    private String timeHired;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("time_created")
    private String timeCreated;
    @JsonProperty("time_modified")
    private String timeModified;
    @JsonProperty("id_user_created")
    private Integer idUserCreated;
    @JsonProperty("id_user_modified")
    private Integer idUserModified;
    @JsonProperty("code_time_hired")
    private String codeTimeHired;
}
