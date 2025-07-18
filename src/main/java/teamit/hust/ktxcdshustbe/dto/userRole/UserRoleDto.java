package teamit.hust.ktxcdshustbe.dto.userRole;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRoleDto {


    private Integer idUserRole;
    private Integer idUser;
    private Integer idRole;
    private String timeCreated;
    private String timeModified;
    private Integer idDepartment;
    private Integer picked;
    private String userName;
}
