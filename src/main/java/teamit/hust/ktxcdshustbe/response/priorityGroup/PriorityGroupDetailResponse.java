package teamit.hust.ktxcdshustbe.response.priorityGroup;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class PriorityGroupDetailResponse {

    @JsonProperty("title_priortity_group")
    private String titlePriortityGroup;
    @JsonProperty("description")
    private String description;
    @JsonProperty("priority_group_code")
    private String priorityGroupCode;
}
