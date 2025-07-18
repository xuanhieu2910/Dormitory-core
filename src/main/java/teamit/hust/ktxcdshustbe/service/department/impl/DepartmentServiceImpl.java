package teamit.hust.ktxcdshustbe.service.department.impl;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import teamit.hust.ktxcdshustbe.dto.department.FindAllDepartmentDto;
import teamit.hust.ktxcdshustbe.dto.department.FindDepartmentStatisticDetailDto;
import teamit.hust.ktxcdshustbe.entity.Department;
import teamit.hust.ktxcdshustbe.entity.KtxUser;
import teamit.hust.ktxcdshustbe.exception.ExitsObjectException;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.exception.ValidParametersException;
import teamit.hust.ktxcdshustbe.repository.department.DepartmentRepository;
import teamit.hust.ktxcdshustbe.request.department.CreateDepartmentRequest;
import teamit.hust.ktxcdshustbe.request.department.EditDepartmentRequest;
import teamit.hust.ktxcdshustbe.request.department.FindAllDepartmentRequest;
import teamit.hust.ktxcdshustbe.response.department.DepartmentStatisticDetailResponse;
import teamit.hust.ktxcdshustbe.response.department.FindAllDepartmentsResponse;
import teamit.hust.ktxcdshustbe.service.department.DepartmentService;
import teamit.hust.ktxcdshustbe.service.user.KtxUserService;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.PageUtils;

import java.util.*;


@Service
public class DepartmentServiceImpl implements DepartmentService {


    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    KtxUserService ktxUserService;


    @Override
    public Page<FindAllDepartmentsResponse> findAllDepartment(FindAllDepartmentRequest request){
        Pageable pageable = PageUtils.buildPage(request.getPage(), request.getSize());
        Page<FindAllDepartmentDto> departments = departmentRepository.findAllDepartment(pageable, request);
        return new PageImpl<>(convertToFindAllDepartmentResponse(departments.getContent()), pageable, departments.getTotalElements());
    }

    private List<FindAllDepartmentsResponse> convertToFindAllDepartmentResponse(List<FindAllDepartmentDto> contents) {
        List<FindAllDepartmentsResponse> responses = new ArrayList<>();
       for (FindAllDepartmentDto dto : contents) {
           FindAllDepartmentsResponse response = new FindAllDepartmentsResponse();
           response.setTitle(dto.getTitle());
           response.setCodeDepartment(dto.getCodeDepartment());
           response.setUserNameCreated(dto.getUserNameCreated());
           response.setFullNameCreated(dto.getFullNameCreated());
           response.setUserNameModified(dto.getUserNameModified());
           response.setFullNameModified(dto.getFullNameModified());
           response.setUserNameManaged(dto.getUserNameManaged());
           response.setFullNameManaged(dto.getFullNameManaged());
           responses.add(response);
       }
       return responses;
    }

    @Override
    public Department findDepartmentById(Integer departmentId){
        if (null == departmentId){
            throw new ValidParametersException();
        }
        Optional<Department> department = departmentRepository.findDepartmentById(departmentId);
        if (department.isEmpty()){
            throw new NotFoundException();
        }
      return department.get();
    }

    @Override
    public DepartmentStatisticDetailResponse findDepartmentStatisticDetailByCode(String codeDepartment) {
        Optional<Department> department = departmentRepository.findDepartmentByCode(codeDepartment);
        if (department.isEmpty()){
            throw new NotFoundException();
        }
        FindDepartmentStatisticDetailDto departmentStatisticDetailDto =
                departmentRepository.findDepartmentStatisticDetailsByCodeDepartment(department.get().getCodeDepartment());
        return convertToDepartmentStatisticDetailResponse(departmentStatisticDetailDto);
    }

    @Override
    public void changeActiveDepartmentByCodeDepartment(String codeDepartment) {
        if (StringUtils.isBlank(codeDepartment)){
            throw new ValidParametersException();
        }
        Optional<Department> department = departmentRepository.findDepartmentByCode(codeDepartment);
        if (department.isEmpty()){
            throw new NotFoundException();
        }
        if (department.get().getStatus().equals(Constants.STATUS_DEPARTMENT_IS_ACTIVE)){
            department.get().setStatus(Constants.STATUS_DEPARTMENT_IN_ACTIVE);
        } else {
            department.get().setStatus(Constants.STATUS_DEPARTMENT_IS_ACTIVE);
        }
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        department.get().setTimeModified(new Date().getTime());
        department.get().setIdUserModified(ktxUser.getIdKtxUser());
        departmentRepository.save(department.get());
    }

    private DepartmentStatisticDetailResponse convertToDepartmentStatisticDetailResponse(FindDepartmentStatisticDetailDto detailDto) {
        DepartmentStatisticDetailResponse response = new DepartmentStatisticDetailResponse();
        response.setTitleDepartment(detailDto.getTitleDepartment());
        response.setTotalStudentHiring(detailDto.getTotalStudentHiring());
        response.setTotalStudentRegister(detailDto.getTotalStudentRegister());
        response.setTotalStudentRegisterNotPayment(detailDto.getTotalStudentRegisterNotPayment());
        response.setTotalStudentRegisterPayment(detailDto.getTotalStudentRegisterPayment());
        response.setTotalRoom(detailDto.getTotalRoom());
        response.setTotalRoomActive(detailDto.getTotalRoomActive());
        response.setTotalRoomUnActive(detailDto.getTotalRoomUnActive());
        response.setStatus(detailDto.getStatus());
        response.setCodeDepartment(detailDto.getCodeDepartment());
        return response;
    }

    @Override
    public void editDepartment(EditDepartmentRequest request) {
        verifyEditDepartmentRequest(request);
        Optional<Department> department = departmentRepository.findDepartmentByCode(request.getCodeDepartment());
        if (department.isEmpty()){
            throw new NotFoundException();
        }
        if (!request.getTitle().equals(department.get().getTitle())) {
            Optional<Department> departmentByTitle = departmentRepository.findDepartmentByTitle(request.getTitle().trim());
            if (departmentByTitle.isPresent()) {
                throw new ExitsObjectException();
            } else {
                KtxUser userManaged = ktxUserService.findKtxUserByCodeUser(request.getCodeUserManaged());
                KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                department.get().setTitle(request.getTitle());
                department.get().setStatus(request.getStatus());
                department.get().setTimeModified(new Date().getTime());
                department.get().setIdUserModified(ktxUser.getIdKtxUser());
                department.get().setIdUserManaged(userManaged.getIdKtxUser());
                departmentRepository.save(department.get());
            }
        }
    }

    @Override
    public void createDepartment(CreateDepartmentRequest request) {
        verifyCreateDepartmentRequest(request);
        KtxUser ktxUser = ktxUserService.findKtxUserByCodeUser(request.getCodeUserManaged());
        Department department = initializeDepartment(request, ktxUser);
        departmentRepository.save(department);
    }

    @Override
    public Department findDepartmentByCodeDepartment(String codeDepartment) {
        Optional<Department> department = departmentRepository.findDepartmentByCode(codeDepartment);
        if (department.isEmpty()){
            throw new NotFoundException();
        }
        return department.get();
    }

    private Department initializeDepartment(CreateDepartmentRequest request, KtxUser ktxUserManager) {
        Long currentTime = new Date().getTime();
        KtxUser ktxUser =  (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Department department = new Department();
        department.setTitle(request.getTitleDepartment());
        department.setStatus(request.getStatus());
        department.setCodeDepartment(UUID.nameUUIDFromBytes(request.getTitleDepartment().getBytes()).toString());
        department.setTimeCreated(currentTime);
        department.setTimeModified(currentTime);
        department.setIdUserCreated(ktxUser.getIdKtxUser());
        department.setIdUserModified(ktxUser.getIdKtxUser());
        department.setIdUserManaged(ktxUserManager.getIdKtxUser());
        return department;
    }

    private void verifyCreateDepartmentRequest(CreateDepartmentRequest request) {
        if (StringUtils.isBlank(request.getTitleDepartment()) || ObjectUtils.isEmpty(request.getStatus())){
            throw new ValidParametersException();
        }
        if (!request.getStatus().equals(Constants.STATUS_DEPARTMENT_IS_ACTIVE) &&
                !request.getStatus().equals(Constants.STATUS_DEPARTMENT_IN_ACTIVE)){
            throw new ValidParametersException();
        }
        Optional<Department> departmentByTitle =
                departmentRepository.findDepartmentByTitle(request.getTitleDepartment().trim());
        if (departmentByTitle.isPresent()) {
            throw new ExitsObjectException();
        }
    }

    private void verifyEditDepartmentRequest(EditDepartmentRequest request) {
        if (StringUtils.isBlank(request.getCodeDepartment()) || StringUtils.isBlank(request.getTitle())
        || ObjectUtils.isEmpty(request.getStatus())){
            throw new ValidParametersException();
        }
        if (!request.getStatus().equals(Constants.STATUS_DEPARTMENT_IS_ACTIVE) &&
                !request.getStatus().equals(Constants.STATUS_DEPARTMENT_IN_ACTIVE)){
            throw new ValidParametersException();
        }
    }

}
