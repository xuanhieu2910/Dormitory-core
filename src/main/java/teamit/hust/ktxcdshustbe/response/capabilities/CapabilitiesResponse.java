package teamit.hust.ktxcdshustbe.response.capabilities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CapabilitiesResponse {

    @JsonProperty("component")
    private String component;
    @JsonProperty("capabilities")
    private List<String> capabilities;
}
