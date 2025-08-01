package teamit.hust.ktxcdshustbe.dto.batchesRegistration;

import lombok.Getter;
import lombok.Setter;
import teamit.hust.ktxcdshustbe.dto.batchesYearGroupRegistration.BatchesYearGroupRegistrationDto;
import teamit.hust.ktxcdshustbe.dto.timeHired.TimeHiredCurrentDto;

import java.util.List;

@Getter
@Setter
public class FindAllBatchesRegistrationDto {

    private Integer idBatchesRegistration;
    private String titleBatchesRegistration;
    private String codeBatchesRegistration;
    private String titleSemester;
    private String codeSemester;
    private Integer idSemester;
    private List<BatchesYearGroupRegistrationDto> batchesYearGroupRegistrationDtos;
    private TimeHiredCurrentDto timeHiredCurrentDto;
    private Long startTime;
    private Long endTime;
    private Integer status;

}
