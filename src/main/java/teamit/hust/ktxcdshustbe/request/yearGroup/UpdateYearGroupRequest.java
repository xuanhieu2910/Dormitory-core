package teamit.hust.ktxcdshustbe.request.yearGroup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamit.hust.ktxcdshustbe.request.RequestPageBase;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateYearGroupRequest extends RequestPageBase {
    private String codeYearGroup;
    private String title;
    private String description;
}
