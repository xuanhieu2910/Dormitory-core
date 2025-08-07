package teamit.hust.ktxcdshustbe.response.userInstance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsUserInstanceResponse {

    @JsonProperty("total_not_error")
    private Integer totalNotError = 0;
    @JsonProperty("total_error")
    private Integer totalError = 0;
}
