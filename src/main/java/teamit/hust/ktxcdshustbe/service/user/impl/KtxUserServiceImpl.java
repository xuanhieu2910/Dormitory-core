package teamit.hust.ktxcdshustbe.service.user.impl;

import jakarta.servlet.ServletException;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import teamit.hust.ktxcdshustbe.dto.userRole.DepartmentUserRoleDto;
import teamit.hust.ktxcdshustbe.entity.*;
import teamit.hust.ktxcdshustbe.enums.OAuth2Factory;
import teamit.hust.ktxcdshustbe.enums.RolePattern;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.repository.user.KtxUserRepository;
import teamit.hust.ktxcdshustbe.request.user.FindAllStudentsRequest;
import teamit.hust.ktxcdshustbe.response.user.DetailInformationUserResponse;
import teamit.hust.ktxcdshustbe.response.user.FindAllStudentsResponse;
import teamit.hust.ktxcdshustbe.response.user.InformationStudentHiredResponse;
import teamit.hust.ktxcdshustbe.service.department.DepartmentService;
import teamit.hust.ktxcdshustbe.service.role.RoleService;
import teamit.hust.ktxcdshustbe.service.room.RoomService;
import teamit.hust.ktxcdshustbe.service.studentRegisterRoom.StudentRegisterRoomService;
import teamit.hust.ktxcdshustbe.service.studentRoom.StudentRoomService;
import teamit.hust.ktxcdshustbe.service.user.KtxUserService;
import teamit.hust.ktxcdshustbe.service.userRole.UserRoleService;
import teamit.hust.ktxcdshustbe.utility.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;


@Log4j2
@Service
public class KtxUserServiceImpl implements KtxUserService {


    @Autowired
    KtxUserRepository ktxUserRepository;
    @Lazy
    @Autowired
    StudentRoomService studentRoomService;
    @Autowired
    RoleService roleService;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    DepartmentService departmentService;


    @Override
    public UserDetails loadUserByUsername(String username){
        Optional<KtxUser> qldtUser = ktxUserRepository.loadUserByUsername(username);
        if (qldtUser.isEmpty()) {
            throw new NotFoundException();
        }
        if (!qldtUser.get().isAccountNonLocked()){
            throw new NotFoundException();
        }
        setIdsDepartment(qldtUser.get());
        return qldtUser.get();
    }

    private void setIdsDepartment(KtxUser ktxUser) {
        DepartmentUserRoleDto departmentUserRoleDto = userRoleService.getDepartmentCurrentUserRoleByCodeUser(ktxUser.getCodeUser());
        List<Integer> idsDepartment = departmentService.findIdsStructureDepartment(departmentUserRoleDto.getIdDepartment());
        ktxUser.setListDepartmentCurrent(idsDepartment);
        ktxUser.setIdDepartmentCurrent(departmentUserRoleDto.getIdDepartment());
    }


    @Override
    public Boolean exitsByUserName(String userName) {
        return ktxUserRepository.exitsByUserName(userName.trim());
    }

    @Override
    public KtxUser save(KtxUser ktxUser) {
        return ktxUserRepository.save(ktxUser);
    }

    @Override
    public KtxUser findKtxUserByKtxUserId(Integer idUser) {
        Optional<KtxUser> user = ktxUserRepository.findByKtxUserId(idUser);
        if (user.isEmpty()) {
            throw new NotFoundException();
        }
        if (!user.get().isAccountNonLocked()){
            throw new NotFoundException();
        }
        return user.get();
    }

    @Override
    public KtxUser findKtxUserByCodeUser(String codeUser) {
        Optional<KtxUser> user = ktxUserRepository.findByKtxUserCode(codeUser);
        if (user.isEmpty()) {
            throw new NotFoundException();
        }
        if (!user.get().isAccountNonLocked()){
            throw new NotFoundException();
        }
        return user.get();
    }

    @Override
    public int updateStatusRegisterRoom(Integer userId, Integer statusUserRegisterRoom) {
        return ktxUserRepository.updateStatusRegisterRoom(userId, statusUserRegisterRoom);
    }


    @Override
    public DetailInformationUserResponse getDetailInformationUser(OidcUser principal) {
        // SỬA ĐỔI: Gọi đến chính hàm loadUserByUsername của class này
        KtxUser userDetails = (KtxUser) this.loadUserByUsername(principal.getPreferredUsername().trim().toLowerCase());
        return convertToDetailInformationUserResponse(userDetails);
    }

    @Override
    public DetailInformationUserResponse getDetailInformationUserHiredRoomIdByHiredRoomId(Integer studentRoomId){
        StudentRoom studentRoom = studentRoomService.findStudentRoomByStudentRoomId(studentRoomId);
        Optional<KtxUser> userDetails = ktxUserRepository.findByKtxUserId(studentRoom.getIdUser());
        if (userDetails.isEmpty()) {
            throw new NotFoundException();
        }
        return convertToDetailInformationUserResponse(userDetails.get());
    }

    private DetailInformationUserResponse convertToDetailInformationUserResponse(KtxUser userDetails) {
        DetailInformationUserResponse response = new DetailInformationUserResponse();
        response.setCodeUser(userDetails.getCodeUser());
        response.setValue(userDetails.getValue());
//        response.setSex(userDetails.getSex().equals(Constants.FEMALE) ? Constants.TITLE_SEX[0] : Constants.TITLE_SEX[1]);
        return response;
    }


    @Override
    public InformationStudentHiredResponse searchInformationStudentByNumberStudent(String numberStudent) {
        Optional<InformationStudentHiredResponse> response = ktxUserRepository.searchInformationStudentHiredRoomByNumberStudent(numberStudent.trim());
        if (response.isEmpty()) {
            throw new NotFoundException();
        }
        return  response.get();
    }

    @Transactional
    @Override
    public void uploadFileAccountStudent(MultipartFile file) {
        ValidateExcelUtils.checkFileExcel(file);
        List<KtxUser> ktxUsers = handleUploadFileAccountStudent(file);
        ktxUserRepository.saveAll(ktxUsers);
        createUserRole(ktxUsers);
    }

    @Override
    public Page<FindAllStudentsResponse> findAllStudentRequest(FindAllStudentsRequest request) {
        Pageable pageable = PageUtils.buildPage(request.getPage(), request.getSize());
        return ktxUserRepository.findAllStudent(request, pageable);
    }

    @Override
    public DetailInformationUserResponse getDetailInformationUserByUserName(String userName) {
        KtxUser userDetails = (KtxUser) this.loadUserByUsername(userName);
        return convertToDetailInformationUserResponse(userDetails);
    }

    private void createUserRole(List<KtxUser> ktxUsers) {
        Role role = roleService.findRoleByTitleRole(RolePattern.STUDENT.name());
        List<UserRole> userRoles = new ArrayList<>();
        Long currentDate = new Date().getTime();
        for (KtxUser dto : ktxUsers){
            UserRole userRole = new UserRole();
            userRole.setIdUser(dto.getIdKtxUser());
            userRole.setIdRole(role.getIdRole());
            userRole.setTimeCreated(currentDate);
            userRole.setTimeModified(currentDate);
            userRoles.add(userRole);
        }
        userRoleService.saveAllUserRole(userRoles);
    }


    private List<KtxUser> handleUploadFileAccountStudent(MultipartFile file) {
        List<KtxUser> customUserDetails = new ArrayList<>();
        int indexSheet = 0;
        int indexRowStartToReadData = 2;
        try {
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(indexSheet);
            int totalRow = xssfSheet.getLastRowNum();
            for (int i = indexRowStartToReadData; i <= totalRow; ++i) {
                XSSFRow row = xssfSheet.getRow(i);
                if(row != null){
                    customUserDetails.add(getCustomUserDetailsFromFile(row));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return customUserDetails;
    }

    private KtxUser getCustomUserDetailsFromFile(XSSFRow row) {
        KtxUser customUserDetails = new KtxUser();
        String userName = ((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(1), CellType.STRING))).toLowerCase();
        String fullNameTmp = ((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(2), CellType.STRING))).toLowerCase();
        String fullName = fullNameTmp.substring(0,1).toUpperCase() + fullNameTmp.substring(1);
        String numberStudent = ((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(3), CellType.STRING))).toLowerCase();
        Timestamp dateOfBirth = new Timestamp(DateUtil.formatDatePattern((String) ExcelUtil.convertValue(row.getCell(4),
                CellType.STRING),DateUtil.DDMMYYYY).getTime());
        String cccd = (String) ExcelUtil.convertValue(row.getCell(5), CellType.STRING);
        String sex = (String) ExcelUtil.convertValue(row.getCell(6), CellType.STRING);
        String nation = (String) ExcelUtil.convertValue(row.getCell(7), CellType.STRING);
        String religion = (String) ExcelUtil.convertValue(row.getCell(8), CellType.STRING);
        String area = (String) ExcelUtil.convertValue(row.getCell(9), CellType.STRING);
        String province = (String) ExcelUtil.convertValue(row.getCell(10), CellType.STRING);
        String district = (String) ExcelUtil.convertValue(row.getCell(11), CellType.STRING);
        String wards = (String) ExcelUtil.convertValue(row.getCell(12), CellType.STRING);
        String address = (String) ExcelUtil.convertValue(row.getCell(13), CellType.STRING);
        String school = (String) ExcelUtil.convertValue(row.getCell(14), CellType.STRING);
        String faculty = (String) ExcelUtil.convertValue(row.getCell(15), CellType.STRING);
        String codeMajor = (String) ExcelUtil.convertValue(row.getCell(16), CellType.STRING);
        String titleMajor = (String) ExcelUtil.convertValue(row.getCell(17), CellType.STRING);
        String phoneNumber = (String) ExcelUtil.convertValue(row.getCell(18), CellType.STRING);
        String email = (String) ExcelUtil.convertValue(row.getCell(19), CellType.STRING);
        Integer yearGrade = Integer.valueOf((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(20), CellType.STRING)));
        customUserDetails.setUserName(userName);
        if (Objects.nonNull(sex)) {
            customUserDetails.setSex(sex.equals(Constants.TITLE_SEX[0]) ? Constants.FEMALE : Constants.MALE);
        } else {
            customUserDetails.setSex(Constants.FEMALE);
        }
        customUserDetails.setIsActived(Constants.ACCOUNT_IS_ACTIVED);
        Long timeCurrent = new Date().getTime();
        customUserDetails.setTimeCreated(timeCurrent);
        customUserDetails.setTimeModified(timeCurrent);
        customUserDetails.setTypeLogin(OAuth2Factory.azure.name());
        customUserDetails.setCodeUser(String.valueOf(UUID.randomUUID()));
        return customUserDetails;
    }



    @Override
    public void hasCapability(String servletPath, String method) throws ServletException {
        KtxUser user =  (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<Capabilities> capabilities = new HashSet<>();
        user.getRole().stream().forEach(role -> capabilities.addAll(role.getCapabilities()));
        boolean isExitsRoleCapability = false;
        for (Capabilities capability : capabilities) {
            if(compareCapability(servletPath, method, capability)){
                isExitsRoleCapability = true;
                break;
            }
        }
        if (!isExitsRoleCapability){
            throw new ServletException("Don't Permission");
        }
    }

    private boolean compareCapability(String servletPath, String method, Capabilities capabilities) {
        String roleCapability = capabilities.getName().substring(capabilities.getName().indexOf(Constants.PATTERN_ROLE_SEPARATE) + 1);
        return servletPath.equals(roleCapability) && method.equals(capabilities.getCapType());
    }


}
