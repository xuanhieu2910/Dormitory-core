package teamit.hust.ktxcdshustbe.dto.department;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindAllDepartmentDto {



    private Integer idDepartment;
    private String title;
    private Long timeCreated;
    private Long timeModified;
    private Integer status;
    private String userNameCreated;
    private String userNameModified;
    private String userNameManaged;
    private String fullNameCreated;
    private String fullNameModified;
    private String fullNameManaged;
    private String codeDepartment;
}
