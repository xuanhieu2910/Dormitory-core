package teamit.hust.ktxcdshustbe.request.role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import teamit.hust.ktxcdshustbe.request.roleCapabilities.UpdateRoleCapabilitiesRequest;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UpdateRoleRequest {

    @NonNull
    private Integer idRole;
    private String shortName;
    private String description;
    private Integer status;
    private List<UpdateRoleCapabilitiesRequest> capabilities;

}
