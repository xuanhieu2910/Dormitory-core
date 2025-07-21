package teamit.hust.ktxcdshustbe.response.department;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FindAllDepartmentsResponse {

    @JsonProperty("title")
    private String title;
    @JsonProperty("code_department")
    private String codeDepartment;
    @JsonProperty("depth")
    private Integer depth;
    @JsonProperty("code_parent_department")
    private String codeParentDepartment;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("path")
    private String path;
    @JsonProperty("short_name")
    private String shortName;
}
