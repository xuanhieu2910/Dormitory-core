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

}
