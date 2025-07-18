package teamit.hust.ktxcdshustbe.request.studentRegister;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InformationParentsStudentRegisterRequest {

    @JsonProperty(value = "label")
    private Integer label;
    @JsonProperty(value = "nameParent")
    private String nameParent;
    @JsonProperty(value = "year")
    private Integer year;
    @JsonProperty(value = "phoneNumber")
    private String phoneNumber;
    @JsonProperty(value = "address")
    private String address;

}
