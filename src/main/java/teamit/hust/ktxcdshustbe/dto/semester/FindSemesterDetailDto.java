package teamit.hust.ktxcdshustbe.dto.semester;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindSemesterDetailDto {

    private String titleSemester;
    private String status;
    private String codeSemester;
    private Integer idUserCreated;
    private Integer idUserModified;
    private Long timeCreated;
    private Long timeModified;
    private String userNameCreated;
    private String userNameModified;
    private String codeUserCreated;
    private String codeUserModified;
    private String note;

}
