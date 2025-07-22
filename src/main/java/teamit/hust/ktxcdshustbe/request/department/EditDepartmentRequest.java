package teamit.hust.ktxcdshustbe.request.department;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditDepartmentRequest {
    private String codeDepartment;
    private String codeParentDepartment;
    private String title;
    private Integer status;
    private String shortName;
    private String Description;
}
