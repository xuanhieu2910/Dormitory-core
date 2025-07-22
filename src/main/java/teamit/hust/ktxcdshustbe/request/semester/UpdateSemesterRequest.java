package teamit.hust.ktxcdshustbe.request.semester;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSemesterRequest {
    private String titleSemester;
    private Integer status;
    private String codeSemester;
    private String note;

}
