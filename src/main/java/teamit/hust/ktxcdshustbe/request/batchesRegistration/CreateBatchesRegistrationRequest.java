package teamit.hust.ktxcdshustbe.request.batchesRegistration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamit.hust.ktxcdshustbe.request.batchesRegistrationRoom.CreateBatchesRegistrationRoomRequest;
import teamit.hust.ktxcdshustbe.request.batchesRegistrationSchedule.CreateBatchesRegistrationScheduleRequest;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBatchesRegistrationRequest {

    private String titleBatchesRegistration;
    private String codeSemester;
    private Long startTime;
    private Long endTime;
    private Integer idTimeHired;
    private String notes;
    private List<String> yearGroups;
    private List<CreateBatchesRegistrationScheduleRequest> batchesRegistrationSchedule;
    private List<CreateBatchesRegistrationRoomRequest> rooms;

}
