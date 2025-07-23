package teamit.hust.ktxcdshustbe.response.yearGroup;
import lombok.Data;

@Data
public class YearGroupDetailResponse {
    private String codeYearGroup;
    private String title;
    private String description;
    private Long timeCreated;
    private Long timeModified;
    private String userNameCreated;
    private String fullNameCreated;
    private String userNameModified;
    private String fullNameModified;
}
