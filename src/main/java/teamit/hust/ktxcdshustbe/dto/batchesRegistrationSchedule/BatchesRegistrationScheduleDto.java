package teamit.hust.ktxcdshustbe.dto.batchesRegistrationSchedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamit.hust.ktxcdshustbe.dto.priorityGroup.PriorityGroupDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatchesRegistrationScheduleDto {

    private Integer idBatchesRegistrationSchedule;
    private Integer status;
    private Long timeCreated;
    private Long timeModified;
    private Long registrationStartTime;
    private Long registrationEndTime;
    private PriorityGroupDto priorityGroupDto;
}
