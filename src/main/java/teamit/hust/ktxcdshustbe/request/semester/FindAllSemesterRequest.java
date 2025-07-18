package teamit.hust.ktxcdshustbe.request.semester;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamit.hust.ktxcdshustbe.request.RequestPageBase;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindAllSemesterRequest extends RequestPageBase {

    private String titleSemester;
    private Integer status;
}
