package teamit.hust.ktxcdshustbe.request.yearGroup;

import lombok.*;
import teamit.hust.ktxcdshustbe.request.RequestPageBase;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateYearGroupRequest extends RequestPageBase {
    private String title;
    private String description;
}