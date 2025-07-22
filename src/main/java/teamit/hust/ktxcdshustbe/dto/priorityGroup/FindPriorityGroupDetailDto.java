package teamit.hust.ktxcdshustbe.dto.priorityGroup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindPriorityGroupDetailDto {
    private Integer idPriorityGroup;
    private String priorityGroupCode;
    private String titlePriorityGroup;
    private String description;
    private Long timeCreated;
    private Long timeModified;
    private Integer idUserCreated;
    private Integer idUserModified;
    private String userNameCreated;
    private String userNameModified;

}
