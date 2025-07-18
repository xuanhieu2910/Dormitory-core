package teamit.hust.ktxcdshustbe.request.role;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import teamit.hust.ktxcdshustbe.request.roleCapabilities.CreateNewRoleCapabilitiesRequest;

import java.util.List;

@Getter
@Setter
@NotNull
public class CreateNewRoleRequest {

    private String title;
    private String content;
    private String shortName;
    private String description;
    private Integer status;
    private List<CreateNewRoleCapabilitiesRequest> capabilities;
}
