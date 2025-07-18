package teamit.hust.ktxcdshustbe.request.studentRegister;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class InformationStudentRegisterRequest {

    @JsonProperty(value = "userId")
    private Integer userId;
    @JsonProperty(value = "fullName",required = true)
    private String fullName;
    @JsonProperty(value = "numberStudent",required = true)
    private String numberStudent;
    @JsonProperty(value = "dateOfBirth",required = true)
    private String dateOfBirth;
    @JsonProperty(value = "cccd",required = true)
    private String cccd;
    @JsonProperty(value = "sex",required = true)
    private Integer sex;
    @JsonProperty(value = "nationId",required = true)
    private Integer nationId;
    @JsonProperty(value = "religionId",required = true)
    private Integer religionId;
    @JsonProperty(value = "areaId",required = true)
    private Integer areaId;
    @JsonProperty(value = "pathAvatar",required = true)
    private String pathAvatar;
    @JsonProperty(value = "codeProvince",required = true)
    private String codeProvince;
    @JsonProperty(value = "codeDistrict",required = true)
    private String codeDistrict;
    @JsonProperty(value = "codeWard",required = true)
    private String codeWard;
    @JsonProperty(value = "address",required = true)
    private String address;
    @JsonProperty(value = "schoolId",required = true)
    private Integer schoolId;
    @JsonProperty(value = "facultyId",required = true)
    private Integer facultyId;
    @JsonProperty(value = "yearGrade",required = true)
    private Integer yearGrade;
    @JsonProperty(value = "nameClass",required = true)
    private String nameClass;
    @JsonProperty(value = "phoneNumber",required = true)
    private String phoneNumber;
    @JsonProperty(value = "emailContact",required = true)
    private String emailContact;
    @JsonProperty(value = "addressContact",required = true)
    private String addressContact;
    @JsonProperty(value = "parentsStudentRegisterRequest")
    private List<InformationParentsStudentRegisterRequest> parentsStudentRegisterRequest;
}
