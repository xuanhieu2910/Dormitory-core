package teamit.hust.ktxcdshustbe.dto.batchesRegistration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindAllDepartmentBatchesRegistrationDto {

    private Integer idDepartment;
    private String codeDepartment;
    private String titleDepartment;
    private String codeBatchesRegistration;
}
