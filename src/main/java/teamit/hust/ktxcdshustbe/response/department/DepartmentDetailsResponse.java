package teamit.hust.ktxcdshustbe.response.department;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentDetailsResponse {
    @JsonProperty(value = "id_department")
    private Integer idDepartment;
    @JsonProperty(value = "code_department")
    private String codeDepartment;
    @JsonProperty(value = "title")
    private String title;
    @JsonProperty(value = "shortname")
    private String shortname;
    @JsonProperty(value = "time_created")
    private String timeCreated;
    @JsonProperty(value = "time_modified")
    private String timeModified;
    @JsonProperty(value = "code_parent")
    private String codeParent;
    @JsonProperty(value = "id_user_created")
    private Integer idUserCreated;
    @JsonProperty(value = "id_user_modified")
    private Integer idUserModified;
    @JsonProperty(value = "description")
    private String description;
    @JsonProperty(value = "title_parent")
    private String nameParent;
    @JsonProperty(value = "id_parent")
    private Integer idParent;
    @JsonProperty(value = "status")
    private Integer status;
}
