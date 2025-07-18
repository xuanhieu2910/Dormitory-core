package teamit.hust.ktxcdshustbe.response.studentRoom;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class StudentSearchAddNewRoomResponse {

    @JsonProperty("code_user")
    private String codeUser;
    @JsonProperty("user_name")
    private String userName;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("number_student")
    private String numberStudent;
    @JsonProperty("title_major")
    private String titleMajor;
    @JsonProperty("path_avatar")
    private String pathAvatar;
    @JsonProperty("sex")
    private Integer sex;
    @JsonProperty("status_register_room")
    private Integer statusRegisterRoom;

}
