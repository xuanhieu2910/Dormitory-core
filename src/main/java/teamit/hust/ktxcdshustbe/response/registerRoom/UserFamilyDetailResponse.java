package teamit.hust.ktxcdshustbe.response.registerRoom;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserFamilyDetailResponse {

    @JsonProperty("idUserFamily")
    private Integer idUserFamily;
    @JsonProperty("fullName")
    private String fullName;
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("label")
    private Integer label;
    @JsonProperty("title")
    private String title;
    @JsonProperty("address")
    private String address;
    @JsonProperty("requireAddress")
    private String requireAddress;
    @JsonProperty("year")
    private Integer year;
}
