package teamit.hust.ktxcdshustbe.dto.yearGroup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindAllYearGroupsDto {
    private Integer idYearGroup;
    private String codeYearGroup;
    private String title;
    private String description;
    private Long timeCreated;
    private Long timeModified;
    private Integer idUserCreated;
    private Integer idUserModified;
    private String userNameCreated;
    private String fullNameCreated;
    private String userNameModified;
    private String fullNameModified;
}
