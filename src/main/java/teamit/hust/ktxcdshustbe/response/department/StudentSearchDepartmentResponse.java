package teamit.hust.ktxcdshustbe.response.department;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentSearchDepartmentResponse {

    @JsonProperty("code_department")
    private String codeDepartment;
    @JsonProperty("title_department")
    private String titleDepartment;
}
