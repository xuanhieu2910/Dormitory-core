package teamit.hust.ktxcdshustbe.dto.department;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindDepartmentStatisticDetailDto {

    private Integer idDepartment;
    private String titleDepartment;
    private Integer totalStudentHiring;
    private Integer totalStudentRegister;
    private Integer totalStudentRegisterNotPayment;
    private Integer totalStudentRegisterPayment;
    private Integer totalRoom;
    private Integer totalRoomActive;
    private Integer totalRoomUnActive;
    private Integer status;
    private String codeDepartment;

}
