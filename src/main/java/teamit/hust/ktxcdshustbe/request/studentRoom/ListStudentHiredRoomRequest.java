package teamit.hust.ktxcdshustbe.request.studentRoom;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamit.hust.ktxcdshustbe.request.RequestPageBase;

import java.util.List;


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
    @JsonProperty("timeStarted")
    private String timeStarted;
    @JsonProperty("timeEnded")
    private String timeEnded;
    private List<Integer> listDepartmentOriginal;

}
