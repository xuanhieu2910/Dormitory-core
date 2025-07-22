package teamit.hust.ktxcdshustbe.response.user;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRoomResponse {

    @JsonProperty("id_register_room")
    private Integer idRegisterRoom;
    @JsonProperty("code_user")
    private String codeUser;
    @JsonProperty("value")
    private String value;
    @JsonProperty("time_register")
    private Long timeRegister;
    @JsonProperty("code_department")
    private String codeDepartment;
    @JsonProperty("title_department")
    private String titleDepartment;
    @JsonProperty("code_room")
    private String codeRoom;
    @JsonProperty("title_room")
    private String titleRoom;
    @JsonProperty("id_time_hired")
    private Integer timeHiredId;
    @JsonProperty("code_semester")
    private String codeSemester;
    @JsonProperty("title_semester")
    private String titleSemester;
    @JsonProperty("status_register_information")
    private Integer statusRegisterInformation;

}
