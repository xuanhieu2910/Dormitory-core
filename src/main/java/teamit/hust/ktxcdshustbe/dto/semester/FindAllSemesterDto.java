package teamit.hust.ktxcdshustbe.dto.semester;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindAllSemesterDto {

    private Integer idSemester;
    private String titleSemester;
    private String codeSemester;
    private Long timeCreated;
    private Long timeModified;
    private Integer status;
    private Integer idUserCreated;
    private Integer idUserModified;
    private String note;
}
