package teamit.hust.ktxcdshustbe.request.priorityGroup;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreatePriorityGroupRequest {

    private String titlePriorityGroup;
    private String description;
}
