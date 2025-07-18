package teamit.hust.ktxcdshustbe.request.department;

import lombok.Getter;
import lombok.Setter;
import teamit.hust.ktxcdshustbe.request.RequestPageBase;

@Getter
@Setter
public class FindAllDepartmentRequest extends RequestPageBase {

    private Integer status;
    private String titleDepartment;
}
