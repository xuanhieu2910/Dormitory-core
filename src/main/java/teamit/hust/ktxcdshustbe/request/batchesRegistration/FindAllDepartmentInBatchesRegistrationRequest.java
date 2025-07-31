package teamit.hust.ktxcdshustbe.request.batchesRegistration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamit.hust.ktxcdshustbe.request.RequestPageBase;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindAllDepartmentInBatchesRegistrationRequest extends RequestPageBase {

    private String codeBatchesRegistration;
}
