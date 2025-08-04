package teamit.hust.ktxcdshustbe.dto.batchesRegistration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamit.hust.ktxcdshustbe.dto.batchesYearGroupRegistration.BatchesYearGroupRegistrationDto;
import teamit.hust.ktxcdshustbe.dto.timeHired.TimeHiredCurrentDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatchesRegistrationDetailDto {
    private Integer idBatchesRegistration;
    private String titleBatchesRegistration;
    private String codeBatchesRegistration;
    private String titleSemester;
    private Integer idSemester;
    private String codeSemester;
    private List<BatchesYearGroupRegistrationDto> batchesYearGroupRegistrationDtos;
    private Long startTime;
    private Long endTime;
    private Integer status;
    private String notes;
    private String description;
    private TimeHiredCurrentDto timeHiredCurrentDto;

}
