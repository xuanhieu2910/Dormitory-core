package teamit.hust.ktxcdshustbe.dto.timeHired;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindAllTimeHiredDto {
    private String timeHired;
    private String codeTimeHired;
    private Integer idTimeHired;
    private String timeStarted;
    private String timeEnd;
    private Integer status;
    private String timeCreated;
    private String timeModified;
    private Integer idUserCreated;
    private String idUserModified;
}
