package teamit.hust.ktxcdshustbe.response.batchesRegistration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamit.hust.ktxcdshustbe.response.batchesYearGroupRegistration.BatchesYearGroupRegistrationResponse;
import teamit.hust.ktxcdshustbe.response.timeHired.TimeHiredResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindAllBatchesRegistrationResponse {

    @JsonProperty("title_batches_registration")
    private String titleBatchesRegistration;
    @JsonProperty("code_batches_registration")
    private String codeBatchesRegistration;
    @JsonProperty("title_semester")
    private String titleSemester;
    @JsonProperty("code_semester")
    private String codeSemester;
    @JsonProperty("year_groups_registration")
    private List<BatchesYearGroupRegistrationResponse> batchesYearGroupRegistrationResponseList;
    @JsonProperty("start_time")
    private Long startTime;
    @JsonProperty("end_time")
    private Long endTime;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("time_hired")
    private TimeHiredResponse timeHiredResponse;

}
