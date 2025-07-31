package teamit.hust.ktxcdshustbe.request.batchesRegistrationSchedule;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBatchesRegistrationScheduleRequest {
    private String priorityGroupCode;
    private Long registrationStartTime;
    private Long registrationEndTime;
}
