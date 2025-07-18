package teamit.hust.ktxcdshustbe.response.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomStudentsHiredResponse {

    @JsonProperty("userId")
    private Integer userId;
    @JsonProperty("fullName")
    private String fullName;
    @JsonProperty("numberStudent")
    private String numberStudent;
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("yearGrade")
    private String yearGrade;
    @JsonProperty("nameClass")
    private String nameClass;
    @JsonProperty("timeHired")
    private String timeHired;
}
