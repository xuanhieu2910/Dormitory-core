package teamit.hust.ktxcdshustbe.response.timeHired;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeHiredCurrentResponse {

    @JsonProperty("time_started")
    private Long timeStarted;
    @JsonProperty("time_ended")
    private Long timeEnded;
}
