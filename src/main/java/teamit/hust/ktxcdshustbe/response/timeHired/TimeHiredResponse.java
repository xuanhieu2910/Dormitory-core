package teamit.hust.ktxcdshustbe.response.timeHired;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TimeHiredResponse {

    @JsonProperty("id_time_hired")
    private Integer idTimeHired;
    @JsonProperty("time_hired_started")
    private Long timeHiredStarted;
    @JsonProperty("time_hired_ended")
    private Long timeHiredEnded;
    @JsonProperty("code_time_hired")
    private String codeTimeHired;
    @JsonProperty("title_time_hired")
    private String titleTimeHired;
}
