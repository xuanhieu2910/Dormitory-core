package teamit.hust.ktxcdshustbe.dto.department;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FindAllDepartmentByCodeAndVisibleDto {
    private Integer idDepartment;
    private String name;
    private String codeDepartment;
    private Integer parent;
    private String timeCreated;
    private String timeModified;
    private Integer depth;
    private Integer status;
    private String path;
}
