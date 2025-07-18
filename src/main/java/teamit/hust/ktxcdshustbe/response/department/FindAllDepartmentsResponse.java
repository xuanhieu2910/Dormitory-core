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
    @JsonProperty("user_name_created")
    private String userNameCreated;
    @JsonProperty("full_name_created")
    private String fullNameCreated;
    @JsonProperty("user_name_modified")
    private String userNameModified;
    @JsonProperty("full_name_modified")
    private String fullNameModified;
    @JsonProperty("user_name_managed")
    private String userNameManaged;
    @JsonProperty("full_name_managed")
    private String fullNameManaged;
}
