package teamit.hust.ktxcdshustbe.dto.batchesYearGroupRegistration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BatchesYearGroupRegistrationDto {

    private Integer idBatchesYearGroupRegistration;
    private Integer idYearGroup;
    private String titleYearGroup;
    private Integer status;
    private Long timeCreated;
    private Long timeModified;

}
