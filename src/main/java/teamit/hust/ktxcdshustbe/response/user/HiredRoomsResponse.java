package teamit.hust.ktxcdshustbe.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HiredRoomsResponse {

    @JsonProperty("id_hired_user_room")
    private Integer idHiredUserRoom;
    @JsonProperty("code_department")
    private String codeDepartment;
    @JsonProperty("title_department")
    private String titleDepartment;
    @JsonProperty("code_room")
    private String codeRoom;
    @JsonProperty("title_room")
    private String titleRoom;
    @JsonProperty("code_user_modified")
    private String codeUserModified;
    @JsonProperty("full_name_user_modified")
    private String fullNameUserModified;
    @JsonProperty("time_hired")
    private String timeHired;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("title_semester")
    private String titleSemester;
}
