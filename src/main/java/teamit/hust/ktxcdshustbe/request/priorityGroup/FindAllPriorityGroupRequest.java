package teamit.hust.ktxcdshustbe.request.priorityGroup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import teamit.hust.ktxcdshustbe.request.RequestPageBase;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindAllPriorityGroupRequest extends RequestPageBase {

    private String titlePriorityGroup;
}
