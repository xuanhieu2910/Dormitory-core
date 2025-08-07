package teamit.hust.ktxcdshustbe.service.department.impl;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import teamit.hust.ktxcdshustbe.dto.department.FindAllDepartmentByCodeAndVisibleDto;
import teamit.hust.ktxcdshustbe.dto.department.FindAllDepartmentDto;
import teamit.hust.ktxcdshustbe.dto.department.FindDepartmentStatisticDetailDto;
import teamit.hust.ktxcdshustbe.dto.department.StudentSearchDepartmentDto;
import teamit.hust.ktxcdshustbe.dto.room.FindAllRoomsDto;
import teamit.hust.ktxcdshustbe.entity.Department;
import teamit.hust.ktxcdshustbe.entity.KtxUser;
import teamit.hust.ktxcdshustbe.entity.StudentRegisterRoom;
import teamit.hust.ktxcdshustbe.exception.ExitsObjectException;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.exception.ValidParametersException;
import teamit.hust.ktxcdshustbe.repository.department.DepartmentRepository;
import teamit.hust.ktxcdshustbe.request.department.CreateDepartmentRequest;
import teamit.hust.ktxcdshustbe.request.department.EditDepartmentRequest;
import teamit.hust.ktxcdshustbe.request.department.FindAllDepartmentRequest;
import teamit.hust.ktxcdshustbe.request.department.StudentSearchDepartmentRequest;
import teamit.hust.ktxcdshustbe.response.department.DepartmentDetailsResponse;
import teamit.hust.ktxcdshustbe.response.department.DepartmentStatisticDetailResponse;
import teamit.hust.ktxcdshustbe.response.department.FindAllDepartmentsResponse;
import teamit.hust.ktxcdshustbe.response.department.StudentSearchDepartmentResponse;
import teamit.hust.ktxcdshustbe.service.department.DepartmentService;
import teamit.hust.ktxcdshustbe.service.room.RoomService;
import teamit.hust.ktxcdshustbe.service.studentRegisterRoom.StudentRegisterRoomService;
import teamit.hust.ktxcdshustbe.service.user.KtxUserService;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.PageUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class DepartmentServiceImpl implements DepartmentService {


    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    private RoomService roomService;
    @Autowired
    private StudentRegisterRoomService studentRegisterRoomService;


    @Override
    public Page<FindAllDepartmentsResponse> findAllDepartment(FindAllDepartmentRequest request){
        Pageable pageable = PageUtils.buildPage(request.getPage(), request.getSize());
        Page<FindAllDepartmentDto> departments = departmentRepository.findAllDepartment(pageable, request);
        return new PageImpl<>(convertToFindAllDepartmentResponse(departments.get().collect(Collectors.toList())),
                pageable, departments.getTotalElements());
    }

    private List<FindAllDepartmentsResponse> convertToFindAllDepartmentResponse(List<FindAllDepartmentDto> contents) {
        List<FindAllDepartmentsResponse> responses = new ArrayList<>();
       for (FindAllDepartmentDto dto : contents) {
           FindAllDepartmentsResponse response = new FindAllDepartmentsResponse();
           response.setTitle(dto.getTitle());
           response.setCodeDepartment(dto.getCodeDepartment());
           response.setStatus(dto.getStatus());
           response.setDepth(dto.getDepth());
           response.setCodeParentDepartment(dto.getCodeParentDepartment());
           response.setPath(dto.getPath());
           response.setShortName(dto.getShortName());
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
        Department department = verifyEditDepartmentRequest(request);
        departmentRepository.save(editValueDepartment(department, request));
    }

    private Department editValueDepartment(Department department, EditDepartmentRequest request) {
        Long currentTime = new Date().getTime();
        KtxUser ktxUser =  (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (StringUtils.isNotBlank(request.getTitle())){
            department.setTitle(request.getTitle());
        }
        if (ObjectUtils.isNotEmpty(request.getStatus())){
            department.setStatus(request.getStatus());
        }
        department.setTimeModified(currentTime);
        department.setIdUserModified(ktxUser.getIdKtxUser());
        if (StringUtils.isNotBlank(request.getShortName())){
            department.setShortName(request.getShortName());
        }
        if (StringUtils.isNotBlank(request.getDescription())){
            department.setDescription(request.getDescription());
        }
        if (ObjectUtils.isNotEmpty(request.getCodeParentDepartment())){
            Optional<Department> departmentParentOptional = departmentRepository.findDepartmentByCode(request.getCodeParentDepartment());
            if (departmentParentOptional.isEmpty()){
                throw new NotFoundException();
            }
            department.setParent(departmentParentOptional.get().getIdDepartment());
        }
        return department;
    }

    @Override
    public void createDepartment(CreateDepartmentRequest request) {
        verifyCreateDepartmentRequest(request);
        Department department = initializeDepartment(request);
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

    @Override
    public List<Integer> findIdsStructureDepartment(Integer idDepartment) {
        List<FindAllDepartmentByCodeAndVisibleDto> dtos = findAllStructureDepartmentByIdDepartment(idDepartment);
        List<Integer> idsStructureDepartment = new ArrayList<>();
        for (FindAllDepartmentByCodeAndVisibleDto dto : dtos){
            idsStructureDepartment.add(dto.getIdDepartment());
        }
        return idsStructureDepartment;
    }

    @Override
    public void deleteDepartmentByCodeDepartment(String codeDepartment) {
        Optional<Department> departmentOptional = departmentRepository.findDepartmentByCode(codeDepartment);
        if (departmentOptional.isEmpty()){
            throw new NotFoundException();
        }
        if (departmentOptional.get().getParent() == null){
            throw new ExitsObjectException();
        }
        if (departmentRepository.isExitsRoomByIdDepartment(departmentOptional.get().getIdDepartment())){
            throw new ExitsObjectException();
        }
        departmentRepository.delete(departmentOptional.get());
    }

    @Override
    public DepartmentDetailsResponse findDetailsDepartmentByCodeDepartment(String codeDepartment) {
        Optional<Department> departmentOptional = departmentRepository.findDepartmentByCode(codeDepartment);
        if (departmentOptional.isEmpty()){
            throw new NotFoundException();
        }
        List<FindAllRoomsDto> findAllRoomsDtoList= roomService.findAllListRoomByCodeDepartment(codeDepartment);

        if(departmentOptional.get().getParent() != null){
            Optional<Department> departmentParentOptional = departmentRepository.findDepartmentById(departmentOptional.get().getParent());
            if (departmentParentOptional.isEmpty()){
                throw new NotFoundException();
            }
            return convertDataDepartmentDetailsWithParent(departmentParentOptional.get(),departmentOptional.get(),findAllRoomsDtoList);
        }


        return convertDataDepartmentDetails(departmentOptional.get(),findAllRoomsDtoList);
    }

    private DepartmentDetailsResponse convertDataDepartmentDetails(Department department, List<FindAllRoomsDto> findAllRoomsDtoList) {
        DepartmentDetailsResponse response = new DepartmentDetailsResponse();
        int totalRoom =0,totalRoomOpen=0,totalRoomClose=0,totalStudentHiring = 0,totalStudentRegister=0,totalStudentRegisterNotYetPaid=0,totalStudentRegisterPaid=0;
        response.setIdDepartment(department.getIdDepartment());
        response.setCodeDepartment(department.getCodeDepartment());
        response.setTitle(department.getTitle());
        response.setShortname(department.getShortName());
        response.setDescription(department.getDescription());
        response.setTimeCreated(String.valueOf(department.getTimeCreated()));
        response.setTimeModified(String.valueOf(department.getTimeModified()));
        response.setIdUserCreated(department.getIdUserCreated());
        response.setIdUserModified(department.getIdUserModified());
        response.setStatus(department.getStatus());
        for(FindAllRoomsDto findAllRoomsDto : findAllRoomsDtoList){
            if (findAllRoomsDto.getIsActive().equals(Constants.STATUS_BATCHES_REGISTRATION_ROOM_IN_ACTIVE)) {
                    totalRoomClose++;
                }
            if (findAllRoomsDto.getIsActive().equals(Constants.STATUS_BATCHES_REGISTRATION_ROOM_ACTIVE)) {
                    totalRoomOpen++;
                }


            totalRoom++;
            totalStudentHiring = totalStudentHiring + findAllRoomsDto.getQuantityHired();
            totalStudentRegister = totalStudentRegister + findAllRoomsDto.getQuantityRegistered();
            List<StudentRegisterRoom> studentRegisterRoomsPaid = studentRegisterRoomService.findListStudentRegisterRoomByCodeRoomAndStatus
                    (findAllRoomsDto.getCodeRoom(),Constants.STATUS_SUCCESS_PAYMENT_STUDENT_ROOM_REGISTER);
            List<StudentRegisterRoom> studentRegisterRoomsNotYetPaid = studentRegisterRoomService.findListStudentRegisterRoomByCodeRoomAndStatus
                    (findAllRoomsDto.getCodeRoom(),Constants.STATUS_HOLD_STUDENT_ROOM_REGISTER);
            totalStudentRegisterNotYetPaid = totalStudentRegisterNotYetPaid + studentRegisterRoomsNotYetPaid.size();
            totalStudentRegisterPaid = totalStudentRegisterPaid + studentRegisterRoomsPaid.size();
        }
        response.setTotalRoomOpen(totalRoomOpen);
        response.setTotalRoomClose(totalRoomClose);
        response.setTotalStudentHired(totalStudentHiring);
        response.setTotalRoom(totalRoom);
        response.setTotalStudentRegister(totalStudentRegister);
        response.setTotalStudentNotYetPaid(totalStudentRegisterNotYetPaid);
        response.setTotalStudentPaid(totalStudentRegisterPaid);
        return response;

    }

    private DepartmentDetailsResponse convertDataDepartmentDetailsWithParent(Department departmentParent,Department department, List<FindAllRoomsDto> findAllRoomsDtoList) {
        DepartmentDetailsResponse response = new DepartmentDetailsResponse();
        int totalRoom =0,totalRoomOpen=0,totalRoomClose=0,totalStudentHiring = 0,totalStudentRegister=0,totalStudentRegisterNotYetPaid=0,totalStudentRegisterPaid=0;
        response.setIdDepartment(department.getIdDepartment());
        response.setCodeDepartment(department.getCodeDepartment());
        response.setTitle(department.getTitle());
        response.setShortname(department.getShortName());
        response.setDescription(department.getDescription());
        response.setTimeCreated(String.valueOf(department.getTimeCreated()));
        response.setTimeModified(String.valueOf(department.getTimeModified()));
        response.setCodeParent(departmentParent.getCodeDepartment());
        response.setIdUserCreated(department.getIdUserCreated());
        response.setIdUserModified(department.getIdUserModified());
        response.setNameParent(departmentParent.getTitle());
        response.setIdParent(departmentParent.getIdDepartment());
        response.setStatus(department.getStatus());
        for(FindAllRoomsDto findAllRoomsDto : findAllRoomsDtoList){
            if (findAllRoomsDto.getIsActive().equals(Constants.STATUS_BATCHES_REGISTRATION_ROOM_IN_ACTIVE)) {
                    totalRoomClose++;
                }
            if (findAllRoomsDto.getIsActive().equals(Constants.STATUS_BATCHES_REGISTRATION_ROOM_ACTIVE)) {
                    totalRoomOpen++;
                }

            totalRoom++;
            totalStudentHiring = totalStudentHiring + findAllRoomsDto.getQuantityHired();
            totalStudentRegister = totalStudentRegister + findAllRoomsDto.getQuantityRegistered();
            List<StudentRegisterRoom> studentRegisterRoomsPaid = studentRegisterRoomService.findListStudentRegisterRoomByCodeRoomAndStatus
                    (findAllRoomsDto.getCodeRoom(),Constants.STATUS_SUCCESS_PAYMENT_STUDENT_ROOM_REGISTER);
            List<StudentRegisterRoom> studentRegisterRoomsNotYetPaid = studentRegisterRoomService.findListStudentRegisterRoomByCodeRoomAndStatus
                    (findAllRoomsDto.getCodeRoom(),Constants.STATUS_HOLD_STUDENT_ROOM_REGISTER);
            totalStudentRegisterNotYetPaid = totalStudentRegisterNotYetPaid + studentRegisterRoomsNotYetPaid.size();
            totalStudentRegisterPaid = totalStudentRegisterPaid + studentRegisterRoomsPaid.size();
        }
        response.setTotalRoom(totalRoom);
        response.setTotalStudentHired(totalStudentHiring);
        response.setTotalRoomOpen(totalRoomOpen);
        response.setTotalRoomClose(totalRoomClose);
        response.setTotalStudentRegister(totalStudentRegister);
        response.setTotalStudentNotYetPaid(totalStudentRegisterNotYetPaid);
        response.setTotalStudentPaid(totalStudentRegisterPaid);
        return response;


    }

    @Override
    public Page<StudentSearchDepartmentResponse> findAllStudentSearchDepartment(StudentSearchDepartmentRequest request) {
        Pageable pageable = PageUtils.buildPage(request.getPage(), request.getSize());
        Page<StudentSearchDepartmentDto> studentSearchDepartmentDtos = departmentRepository.findAllStudentSearchDepartment(pageable, request);
        return new PageImpl<>(convertToFindAllStudentSearchDepartment(studentSearchDepartmentDtos.getContent()), pageable, studentSearchDepartmentDtos.getTotalElements());
    }

    private List<StudentSearchDepartmentResponse> convertToFindAllStudentSearchDepartment(List<StudentSearchDepartmentDto> content) {
        List<StudentSearchDepartmentResponse> responses = new ArrayList<>();
        for (StudentSearchDepartmentDto dto : content) {
            StudentSearchDepartmentResponse response = new StudentSearchDepartmentResponse();
            response.setCodeDepartment(dto.getCodeDepartment());
            response.setTitleDepartment(dto.getTitleDepartment());
            responses.add(response);
        }
        return responses;
    }

    private List<FindAllDepartmentByCodeAndVisibleDto> findAllStructureDepartmentByIdDepartment(Integer idDepartment) {
        return departmentRepository.findAllStructDepartmentByIdDepartment(idDepartment);
    }

    private Department initializeDepartment(CreateDepartmentRequest request) {
        Long currentTime = new Date().getTime();
        KtxUser ktxUser =  (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Department department = new Department();
        department.setTitle(request.getTitle());
        department.setStatus(request.getStatus());
        department.setCodeDepartment(UUID.nameUUIDFromBytes((request.getTitle() + new Date().getTime()).getBytes()).toString());
        department.setTimeCreated(currentTime);
        department.setTimeModified(currentTime);
        department.setIdUserCreated(ktxUser.getIdKtxUser());
        department.setIdUserModified(ktxUser.getIdKtxUser());
        if (StringUtils.isNotBlank(request.getShortName())){
            department.setShortName(request.getShortName());
        }
        if (StringUtils.isNotBlank(request.getDescription())){
            department.setDescription(request.getDescription());
        }
        if (ObjectUtils.isNotEmpty(request.getCodeParentDepartment())){
            Optional<Department> departmentParentOptional = departmentRepository.findDepartmentByCode(request.getCodeParentDepartment());
            if (departmentParentOptional.isEmpty()){
                throw new NotFoundException();
            }
            department.setParent(departmentParentOptional.get().getIdDepartment());
        }
        return department;
    }

    private void verifyCreateDepartmentRequest(CreateDepartmentRequest request) {
        if (StringUtils.isBlank(request.getTitle()) || ObjectUtils.isEmpty(request.getStatus())){
            throw new ValidParametersException();
        }
        if (!request.getStatus().equals(Constants.STATUS_DEPARTMENT_IS_ACTIVE) &&
                !request.getStatus().equals(Constants.STATUS_DEPARTMENT_IN_ACTIVE)){
            throw new ValidParametersException();
        }
        Optional<Department> departmentByTitle =
                departmentRepository.findDepartmentByTitle(request.getTitle().trim());
        if (departmentByTitle.isPresent()) {
            throw new ExitsObjectException();
        }
    }

    private Department verifyEditDepartmentRequest(EditDepartmentRequest request) {
        if (StringUtils.isBlank(request.getCodeDepartment()) || StringUtils.isBlank(request.getTitle())){
            throw new ValidParametersException();
        }
        if (!request.getStatus().equals(Constants.STATUS_DEPARTMENT_IS_ACTIVE) &&
                !request.getStatus().equals(Constants.STATUS_DEPARTMENT_IN_ACTIVE)){
            throw new ValidParametersException();
        }
        Optional<Department> departmentOptional = departmentRepository.findDepartmentByCode(request.getCodeDepartment());
        if (departmentOptional.isEmpty()){
            throw new NotFoundException();
        }
        if ( (departmentOptional.get().getTitle() != null && StringUtils.isNotBlank(request.getTitle()) &&
                departmentOptional.get().getTitle().equals(request.getTitle())) ||
                (departmentOptional.get().getShortName() != null && StringUtils.isNotBlank(request.getShortName()) &&
                        !departmentOptional.get().getShortName().equals(request.getShortName()))) {
            if (departmentRepository.checkExitsDepartmentByTitleOrShortName(request.getTitle(),
                    request.getShortName())) {
                throw new ExitsObjectException();
            }
        }

        return departmentOptional.get();
    }

}
