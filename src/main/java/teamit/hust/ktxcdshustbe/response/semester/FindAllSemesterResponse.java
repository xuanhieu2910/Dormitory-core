package teamit.hust.ktxcdshustbe.response.semester;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindAllSemesterResponse {

    @JsonProperty("title_semester")
    private String titleSemester;
    @JsonProperty("code_semester")
    private String codeSemester;
    @JsonProperty("status")
    private Integer status;

}
