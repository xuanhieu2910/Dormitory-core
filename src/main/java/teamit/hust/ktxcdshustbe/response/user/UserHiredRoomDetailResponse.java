package teamit.hust.ktxcdshustbe.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import teamit.hust.ktxcdshustbe.response.registerRoom.UserFamilyDetailResponse;

import java.util.List;

@Data
public class UserHiredRoomDetailResponse {

    @JsonProperty("studentHiredRoomId")
    private Integer studentHiredRoomId;
    @JsonProperty("userId")
    private Integer userId;
    @JsonProperty("fullName")
    private String fullName;
    @JsonProperty("numberStudent")
    private String numberStudent;
    @JsonProperty("dateOfBirth")
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
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("email")
    private String email;
    @JsonProperty("classUser")
    private String classUser;
    @JsonProperty("faculty")
    private String faculty;
    @JsonProperty("yearGrade")
    private String yearGrade;
    @JsonProperty("province")
    private String province;
    @JsonProperty("district")
    private String district;
    @JsonProperty("wards")
    private String wards;
    @JsonProperty("nameSchool")
    private String nameSchool;
    @JsonProperty("nameFaculty")
    private String nameFaculty;
    @JsonProperty("address")
    private String address;
    @JsonProperty("userFamily")
    private List<UserFamilyDetailResponse> userFamilyDetailResponseList;
    @JsonProperty("pathAvatar")
    private String pathAvatar;
    @JsonProperty("addressContact")
    private String addressContact;
}
