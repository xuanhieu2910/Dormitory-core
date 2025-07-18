package teamit.hust.ktxcdshustbe.response.capabilities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class FindAllCapabilitiesResponse {

    @JsonProperty("id_capability")
    private Integer idCapability;
    @JsonProperty("name_capability")
    private String nameCapability;
    @JsonProperty("method")
    private String method;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("component")
    private String component;
}
