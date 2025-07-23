package teamit.hust.ktxcdshustbe.response.yearGroup;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FindAllYearGroupsResponse {

    @JsonProperty("code_year_group")
    private String codeYearGroup;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("time_created")
    private Long timeCreated;

    @JsonProperty("time_modified")
    private Long timeModified;
}