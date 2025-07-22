package teamit.hust.ktxcdshustbe.request.department;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDepartmentRequest {

    private String codeParentDepartment;
    @NonNull
    private String title;
    private String shortName;
    private String description;
    @NonNull
    private Integer status;

}
