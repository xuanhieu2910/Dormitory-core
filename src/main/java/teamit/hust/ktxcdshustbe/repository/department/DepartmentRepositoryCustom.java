package teamit.hust.ktxcdshustbe.repository.department;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamit.hust.ktxcdshustbe.dto.department.FindAllDepartmentByCodeAndVisibleDto;
import teamit.hust.ktxcdshustbe.dto.department.FindAllDepartmentDto;
import teamit.hust.ktxcdshustbe.dto.department.FindDepartmentStatisticDetailDto;
import teamit.hust.ktxcdshustbe.entity.Department;
import teamit.hust.ktxcdshustbe.request.department.FindAllDepartmentRequest;
import teamit.hust.ktxcdshustbe.response.department.DepartmentDetailsResponse;
import teamit.hust.ktxcdshustbe.response.department.DepartmentStatisticDetailResponse;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepositoryCustom {

    Page<FindAllDepartmentDto> findAllDepartment(Pageable pageable, FindAllDepartmentRequest request);
    Optional<Department> findDepartmentById(Integer departmentId);
    Optional<Department> findDepartmentByTitle(String titleDepartment);
    FindDepartmentStatisticDetailDto findDepartmentStatisticDetailsByCodeDepartment(String codeDepartment);
    Optional<Department> findDepartmentByCode(String codeDepartment);

    List<FindAllDepartmentByCodeAndVisibleDto> findAllStructDepartmentByIdDepartment(Integer idDepartment);

    boolean checkExitsDepartmentByTitleOrShortName(String title, String shortName);

    boolean isExitsRoomByIdDepartment(Integer idDepartment);

    Optional<DepartmentDetailsResponse> findDepartmentDetailsByCode(String codeDepartment);
}
