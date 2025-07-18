package teamit.hust.ktxcdshustbe.response.registerRoom;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudentRegisterRoomDetailResponse {

    @JsonProperty("id_student_register")
    private Integer studentRegisterId;
    @JsonProperty("code_user")
    private String codeUser;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("number_student")
    private String numberStudent;
    @JsonProperty("date_of_birth")
    private String dateOfBirth;
    @JsonProperty("sex")
    private String sex;
    @JsonProperty("religion")
    private String religion;
    @JsonProperty("cccd")
    private String cccd;
    @JsonProperty("nation")
    private String nation;
    @JsonProperty("area")
    private String area;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("email")
    private String email;
    @JsonProperty("class_user")
    private String classUser;
    @JsonProperty("faculty")
    private String faculty;
    @JsonProperty("year_grade")
    private String yearGrade;
    @JsonProperty("province")
    private String province;
    @JsonProperty("district")
    private String district;
    @JsonProperty("wards")
    private String wards;
    @JsonProperty("school")
    private String school;
    @JsonProperty("address")
    private String address;
    @JsonProperty("code_major")
    private String codeMajor;
    @JsonProperty("title_major")
    private String titleMajor;
    @JsonProperty("type_login")
    private String typeLogin;
    @JsonProperty("name_father")
    private String nameFather;
    @JsonProperty("year_father")
    private Integer yearFather;
    @JsonProperty("phone_number_father")
    private String phoneNumberFather;
    @JsonProperty("address_father")
    private String addressFather;
    @JsonProperty("name_mother")
    private String nameMother;
    @JsonProperty("year_mother")
    private Integer yearMother;
    @JsonProperty("phone_number_mother")
    private String phoneNumberMother;
    @JsonProperty("address_mother")
    private String addressMother;
    @JsonProperty("path_avatar")
    private String pathAvatar;
    @JsonProperty("address_contact")
    private String addressContact;
    @JsonProperty("information_room")
    private InformationRoomRegisterResponse informationRoomRegisterResponse;
}
