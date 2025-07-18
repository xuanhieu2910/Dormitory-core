package teamit.hust.ktxcdshustbe.request.department;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDepartmentRequest {

    private String titleDepartment;
    private Integer status;
    private String codeUserManaged;

}
