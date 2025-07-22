package teamit.hust.ktxcdshustbe.request.priorityGroup;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdatePriorityGroupRequest {

    private String priorityGroupCode;
    private String titlePriorityGroup;
    private String description;
}
