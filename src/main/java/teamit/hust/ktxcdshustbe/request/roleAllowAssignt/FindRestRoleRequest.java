package teamit.hust.ktxcdshustbe.request.roleAllowAssignt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamit.hust.ktxcdshustbe.request.RequestPageBase;

@Getter
@Setter
@NoArgsConstructor
public class FindRestRoleRequest extends RequestPageBase {

    private Integer idDepartment;
    private String codeUser;
}
