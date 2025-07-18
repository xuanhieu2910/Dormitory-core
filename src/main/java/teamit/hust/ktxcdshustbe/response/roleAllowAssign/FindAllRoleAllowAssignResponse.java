package teamit.hust.ktxcdshustbe.response.roleAllowAssign;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FindAllRoleAllowAssignResponse {

    @JsonProperty("source_role_assign")
    private SourceRoleAssignResponse sourceRoleAssignResponse;
    @JsonProperty("destination_role_assign")
    private List<DestinationRoleAssignResponse> destinationRoleAssignResponseList;
}
