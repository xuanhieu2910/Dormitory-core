package teamit.hust.ktxcdshustbe.repository.department;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamit.hust.ktxcdshustbe.dto.department.FindAllDepartmentDto;
import teamit.hust.ktxcdshustbe.dto.department.FindDepartmentStatisticDetailDto;
import teamit.hust.ktxcdshustbe.entity.Department;
import teamit.hust.ktxcdshustbe.request.department.FindAllDepartmentRequest;
import teamit.hust.ktxcdshustbe.response.department.DepartmentStatisticDetailResponse;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepositoryCustom {

    Page<FindAllDepartmentDto> findAllDepartment(Pageable pageable, FindAllDepartmentRequest request);


    Optional<Department> findDepartmentById(Integer departmentId);

    Optional<Department> findDepartmentByTitle(String titleDepartment);

    FindDepartmentStatisticDetailDto findDepartmentStatisticDetailsByCodeDepartment(String codeDepartment);
    Optional<Department> findDepartmentByCode(String codeDepartment);
}
