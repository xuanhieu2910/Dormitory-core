package teamit.hust.ktxcdshustbe.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DetailInformationUserFamilyResponse {


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
    @JsonProperty("requireAddress")
    private String requireAddress;
}
