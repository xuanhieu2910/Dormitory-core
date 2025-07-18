package teamit.hust.ktxcdshustbe.response.serviceRoom;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceRoomResponse {
    @JsonProperty("code_service")
    private String codeService;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
}
