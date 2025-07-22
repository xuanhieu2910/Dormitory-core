package teamit.hust.ktxcdshustbe.service.department;

import org.springframework.data.domain.Page;
import teamit.hust.ktxcdshustbe.entity.Department;
import teamit.hust.ktxcdshustbe.request.department.CreateDepartmentRequest;
import teamit.hust.ktxcdshustbe.request.department.EditDepartmentRequest;
import teamit.hust.ktxcdshustbe.request.department.FindAllDepartmentRequest;
import teamit.hust.ktxcdshustbe.response.department.DepartmentDetailsResponse;
import teamit.hust.ktxcdshustbe.response.department.DepartmentStatisticDetailResponse;
import teamit.hust.ktxcdshustbe.response.department.FindAllDepartmentsResponse;

import java.util.List;

public interface DepartmentService {

    Page<FindAllDepartmentsResponse> findAllDepartment(FindAllDepartmentRequest request);
    Department findDepartmentById(Integer departmentId) ;
    DepartmentStatisticDetailResponse findDepartmentStatisticDetailByCode(String codeDepartment);
    void changeActiveDepartmentByCodeDepartment(String codeDepartment);
    void editDepartment(EditDepartmentRequest request);
    void createDepartment(CreateDepartmentRequest request);
    Department findDepartmentByCodeDepartment(String codeDepartment);

    List<Integer> findIdsStructureDepartment(Integer idDepartment);

    void deleteDepartmentByCodeDepartment(String codeDepartment);

    DepartmentDetailsResponse findDetailsDepartmentByCodeDepartment(String codeDepartment);
}
