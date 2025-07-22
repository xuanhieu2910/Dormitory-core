package teamit.hust.ktxcdshustbe.response.batchesYearGroupRegistration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatchesYearGroupRegistrationResponse {

    @JsonProperty("id_batches_year_group_registration")
    private Integer idBatchesYearGroupRegistration;
    @JsonProperty("id_year_group")
    private Integer idYearGroup;
    @JsonProperty("title_year_group")
    private String titleYearGroup;
    @JsonProperty("status")
    private Integer status;
}
