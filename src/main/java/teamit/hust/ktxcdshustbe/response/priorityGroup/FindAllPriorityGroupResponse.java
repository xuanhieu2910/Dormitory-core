package teamit.hust.ktxcdshustbe.response.priorityGroup;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindAllPriorityGroupResponse {

    @JsonProperty("title_priority_group")
    private String titlePriorityGroup;
    @JsonProperty("priority_group_code")
    private String priorityGroupCode;
    @JsonProperty("description")
    private String description;
}
