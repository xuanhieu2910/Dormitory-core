package teamit.hust.ktxcdshustbe.response.user;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InformationStudentHiredResponse {

    @JsonProperty("code_user")
    private String codeUser;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("number_student")
    private String numberStudent;
    @JsonProperty("code_room")
    private String codeRoom;
    @JsonProperty("title_room")
    private String titleRoom;
    @JsonProperty("code_department")
    private String codeDepartment;
    @JsonProperty("title_department")
    private String titleDepartment;
    @JsonProperty("sex")
    private String sex;

}
