package teamit.hust.ktxcdshustbe.response.batchesRegistrationSchedule;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamit.hust.ktxcdshustbe.response.priorityGroup.PriorityGroupDetailResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatchesRegistrationScheduleDetailResponse {

    @JsonProperty("id_batches_registration_schedule")
    private Integer idBatchesRegistrationSchedule;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("time_created")
    private Long timeCreated;
    @JsonProperty("time_modified")
    private Long timeModified;
    @JsonProperty("registration_start_time")
    private Long registrationStartTime;
    @JsonProperty("registration_end_time")
    private Long registrationEndTime;
    @JsonProperty("priority_group")
    private PriorityGroupDetailResponse priorityGroupDetailResponse;
}
