package teamit.hust.ktxcdshustbe.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;

@Getter
@Setter
@NoArgsConstructor
public class FindAllStudentsResponse {

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
    @JsonProperty("status_declare_information")
    private Integer statusDeclareInformation;
    @JsonProperty("status_hire_room")
    private Integer statusHireRoom;

}
