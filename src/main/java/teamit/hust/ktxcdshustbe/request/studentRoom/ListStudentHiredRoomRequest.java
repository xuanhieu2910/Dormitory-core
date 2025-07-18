package teamit.hust.ktxcdshustbe.request.studentRoom;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamit.hust.ktxcdshustbe.request.RequestPageBase;


@Getter
@Setter
@NoArgsConstructor
public class ListStudentHiredRoomRequest extends RequestPageBase {

    @JsonProperty("codeDepartment")
    private Integer codeDepartment;
    @JsonProperty("codeRoom")
    private Integer codeRoom;
    @JsonProperty("codeSemester")
    private Integer codeSemester;
    @JsonProperty("yearGrade")
    private Integer yearGrade;
    @JsonProperty("timeStarted")
    private String timeStarted;
    @JsonProperty("timeEnded")
    private String timeEnded;

}
