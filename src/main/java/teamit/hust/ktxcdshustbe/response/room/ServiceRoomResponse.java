package teamit.hust.ktxcdshustbe.response.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceRoomResponse {

    @JsonProperty("code_service")
    private String codeService;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("status")
    private Integer status;
}
