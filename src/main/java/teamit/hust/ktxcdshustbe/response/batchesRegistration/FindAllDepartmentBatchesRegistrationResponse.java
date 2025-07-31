package teamit.hust.ktxcdshustbe.response.batchesRegistration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindAllDepartmentBatchesRegistrationResponse {

    @JsonProperty("code_batches_registration")
    private String codeBatchesRegistration;
    @JsonProperty("code_department")
    private String codeDepartment;
    @JsonProperty("title_department")
    private String titleDepartment;

}
