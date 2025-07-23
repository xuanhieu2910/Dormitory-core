package teamit.hust.ktxcdshustbe.request.yearGroup;

import lombok.*;
import teamit.hust.ktxcdshustbe.request.RequestPageBase;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindAllYearGroupsRequest extends RequestPageBase {

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