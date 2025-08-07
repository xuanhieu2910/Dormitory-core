package teamit.hust.ktxcdshustbe.service.user.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
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
import teamit.hust.ktxcdshustbe.request.user.UpdateProfileUserRequest;
import teamit.hust.ktxcdshustbe.response.user.DetailInformationUserResponse;
import teamit.hust.ktxcdshustbe.response.user.FindAllStudentsResponse;
import teamit.hust.ktxcdshustbe.response.user.InformationStudentHiredResponse;
import teamit.hust.ktxcdshustbe.service.department.DepartmentService;
import teamit.hust.ktxcdshustbe.service.priorityGroup.PriorityGroupService;
import teamit.hust.ktxcdshustbe.service.role.RoleService;
import teamit.hust.ktxcdshustbe.service.room.RoomService;
import teamit.hust.ktxcdshustbe.service.studentRegisterRoom.StudentRegisterRoomService;
import teamit.hust.ktxcdshustbe.service.studentRoom.StudentRoomService;
import teamit.hust.ktxcdshustbe.service.user.KtxUserService;
import teamit.hust.ktxcdshustbe.service.userInstance.KtxUserInstanceService;
import teamit.hust.ktxcdshustbe.service.userRole.UserRoleService;
import teamit.hust.ktxcdshustbe.service.yearGroup.YearGroupService;
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
    YearGroupService yearGroupService;
    @Autowired
    PriorityGroupService priorityGroupService;
    @Lazy
    @Autowired
    KtxUserInstanceService ktxUserInstanceService;

    @Override
    public UserDetails loadUserByUsername(String username){
        Optional<KtxUser> qldtUser = ktxUserRepository.loadUserByUsername(username);
        if (qldtUser.isEmpty()) {
            throw new NotFoundException();
        }
        if (!qldtUser.get().isAccountNonLocked()){
            throw new NotFoundException();
        }
//        setIdsDepartment(qldtUser.get());
        return qldtUser.get();
    }

//    private void setIdsDepartment(KtxUser ktxUser) {
//        DepartmentUserRoleDto departmentUserRoleDto = userRoleService.getDepartmentCurrentUserRoleByCodeUser(ktxUser.getCodeUser());
//        List<Integer> idsDepartment = departmentService.findIdsStructureDepartment(departmentUserRoleDto.getIdDepartment());
//        ktxUser.setListDepartmentCurrent(idsDepartment);
//        ktxUser.setIdDepartmentCurrent(departmentUserRoleDto.getIdDepartment());
//    }


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

    @Transactional
    @Override
    public void updateUserProfile(UpdateProfileUserRequest request) {
        Optional<KtxUser> ktxUserOptional = ktxUserRepository.findByKtxUserCode(request.getCodeUser());
        if (ktxUserOptional.isEmpty()) {
            throw new NotFoundException();
        }
        updateInfoUser(ktxUserOptional.get(),request);
    }

    @Override
    public KtxUser findKtxUserByUserName(String lowerCase) {
        Optional<KtxUser> ktxUserOptional = ktxUserRepository.findByKtxUserByUserName(lowerCase);
        if (ktxUserOptional.isEmpty()) {
            throw new NotFoundException();
        }
        return ktxUserOptional.get();
    }

    @Override
    public void saveAllValue(List<KtxUser> customUserDetails) {
        ktxUserRepository.saveAll(customUserDetails);
    }

    private void updateInfoUser(KtxUser ktxUser, UpdateProfileUserRequest request) {

        YearGroup yearGroup = yearGroupService.findYearGroupByTitle(request.getTitleYearGroup());
        PriorityGroup priorityGroup =  priorityGroupService.findPriorGroupByTitle(request.getTitlePriorityGroup());
        Long currentTime = new Date().getTime();
        KtxUser ktxUserCurrent =  (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (StringUtils.isNotBlank(request.getUsername())){
            ktxUser.setUserName(request.getUsername());
        }
        if (StringUtils.isNotBlank(request.getPassword())){
            ktxUser.setPassword(request.getPassword());
        }
        if (ObjectUtils.isNotEmpty(request.getSex())){
            ktxUser.setSex(request.getSex());
        }
        if (ObjectUtils.isNotEmpty(request.getIsActive())){
            ktxUser.setIsActived(request.getIsActive());
        }
        if (StringUtils.isNotBlank(request.getTypeLogin())){
            ktxUser.setTypeLogin(request.getTypeLogin());
        }
        if (StringUtils.isNotBlank(request.getValue())){
            ktxUser.setValue(request.getValue());
        }
        ktxUser.setTimeModified(currentTime);
        ktxUser.setIdUserModified(ktxUserCurrent.getIdUserModified());
        ktxUser.setIdYearGroup(yearGroup.getIdYearGroup());
        ktxUser.setIdPriorityGroup(priorityGroup.getIdPriorityGroup());
        ktxUserRepository.save(ktxUser);
    }


    @Override
    public DetailInformationUserResponse getDetailInformationUser() {
        // SỬA ĐỔI: Gọi đến chính hàm loadUserByUsername của class này
        KtxUser userDetails = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
        YearGroup yearGroup = yearGroupService.findYearGroupByIdYearGroup(userDetails.getIdYearGroup());
        PriorityGroup priorityGroup =  priorityGroupService.findPriorGroupByIdPriorGroup(userDetails.getIdPriorityGroup());
        DetailInformationUserResponse response = new DetailInformationUserResponse();
        response.setCodeUser(userDetails.getCodeUser());
        response.setValue(userDetails.getValue());
        response.setPassword(userDetails.getPassword());
        response.setSex(userDetails.getSex());
        response.setIsActive(userDetails.getIsActived());
        response.setTypeLogin(userDetails.getTypeLogin());
        response.setTitleYearGroup(yearGroup.getTitle());
        response.setTitlePriorityGroup(priorityGroup.getTitle());
//        response.setSex(userDetails.getSex().equals(Constants.FEMALE) ? Constants.TITLE_SEX[0] : Constants.TITLE_SEX[1]);
        return response;
    }


    @Override
    public InformationStudentHiredResponse searchInformationStudentByNumberStudent(String codeStudent) {
        Optional<InformationStudentHiredResponse> response = ktxUserRepository.searchInformationStudentHiredRoomByNumberStudent(codeStudent.trim());
        if (response.isEmpty()) {
            throw new NotFoundException();
        }
        return  response.get();
    }

    @Transactional
    @Override
    public void uploadFileAccountStudent(MultipartFile file) {
        ValidateExcelUtils.checkFileExcel(file);
        List<KtxUserInstance> ktxUsersInstance = handleUploadFileAccountStudent(file);
        ktxUserInstanceService.saveAllData(ktxUsersInstance);
//        ktxUserRepository.saveAll(ktxUsers);
//        createUserRole(ktxUsers);
    }

    @Override
    public Page<FindAllStudentsResponse> findAllStudentRequest(FindAllStudentsRequest request) {
        Pageable pageable = PageUtils.buildPage(request.getPage(), request.getSize());
        return ktxUserRepository.findAllStudent(request, pageable);
    }

    @Override
    public DetailInformationUserResponse getDetailInformationUserByCodeUser(String codeUser) {
        Optional<KtxUser>  userDetails = ktxUserRepository.findByKtxUserCode(codeUser);
        YearGroup yearGroup = yearGroupService.findYearGroupByIdYearGroup(userDetails.get().getIdYearGroup());
        PriorityGroup priorityGroup =  priorityGroupService.findPriorGroupByIdPriorGroup(userDetails.get().getIdPriorityGroup());
        return convertToDetailInformationStudentResponse(userDetails.get(),yearGroup,priorityGroup);
    }

    private DetailInformationUserResponse convertToDetailInformationStudentResponse(KtxUser ktxUser, YearGroup yearGroup, PriorityGroup priorityGroup) {
        DetailInformationUserResponse response = new DetailInformationUserResponse();
        response.setCodeUser(ktxUser.getCodeUser());
        response.setValue(ktxUser.getValue());
        response.setPassword(ktxUser.getPassword());
        response.setSex(ktxUser.getSex());
        response.setIsActive(ktxUser.getIsActived());
        response.setTypeLogin(ktxUser.getTypeLogin());
        response.setTitleYearGroup(yearGroup.getTitle());
        response.setTitlePriorityGroup(priorityGroup.getTitle());
        return response;
    }

    private void createUserRole(List<KtxUser> ktxUsers) {
        KtxUser ktxUserCurrent =  (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Role role = roleService.findRoleByTitleRole(RolePattern.STUDENT.name());
        List<UserRole> userRoles = new ArrayList<>();
        Long currentDate = new Date().getTime();
        for (KtxUser dto : ktxUsers){
            UserRole userRole = new UserRole();
            userRole.setIdUser(dto.getIdKtxUser());
            userRole.setIdRole(role.getIdRole());
            userRole.setTimeCreated(currentDate);
            userRole.setTimeModified(currentDate);
            userRole.setIdUserCreated(ktxUserCurrent.getIdKtxUser());
            userRole.setIdUserModified(ktxUserCurrent.getIdKtxUser());
            userRole.setPicked(Constants.ROLE_USER_PICKED);
            userRoles.add(userRole);
        }
        userRoleService.saveAllUserRole(userRoles);
    }


    private List<KtxUserInstance> handleUploadFileAccountStudent(MultipartFile file) {
        Map<String, Integer> mapYearGroup =
                convertToMapYearGroup(yearGroupService.
                        getAllTYearGroup());
        Map<String, Integer> mapPriorityGroup =
                convertToMapPriorityGroup(priorityGroupService.
                        getAllTPriorityGroup());
        int indexSheet = 0;
        int indexRowStartToReadData = 3;

        int constantMaximumRow = 2000;

        try {
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(indexSheet);
//            int totalRow = xssfSheet.getLastRowNum();
            int totalRow = xssfSheet.getPhysicalNumberOfRows();
            log.info("Total row is: {}", totalRow);
            if (totalRow > (constantMaximumRow + indexRowStartToReadData)) {
                totalRow = constantMaximumRow;
            }
              List<XSSFRow> allRowsConfirm = new ArrayList<>();
            for (int i = indexRowStartToReadData; i <= totalRow; ++i) {
                XSSFRow row = xssfSheet.getRow(i);
                if(row != null && hasDataInRow(row, 2)){
                    allRowsConfirm.add(row);
                }
            }
            List<KtxUserInstance> instances = new ArrayList<>();
            KtxUser ktxUserCurrent =  (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String timeCurrent = String.valueOf(new Date().getTime());
            ObjectMapper mapper = new ObjectMapper();
            for (XSSFRow row : allRowsConfirm){
                KtxUserInstance instance = new KtxUserInstance();
                instance.setIdUserCreated(ktxUserCurrent.getIdKtxUser());
                instance.setIdUserModified(ktxUserCurrent.getIdKtxUser());
                instance.setTimeCreated(timeCurrent);
                instance.setTimeModified(timeCurrent);

                int error = -1;

                Map<String, Object> commonData = new HashMap<>();
                String numberStudent = ExcelUtil.convertValue(row.getCell(1), CellType.STRING) == null ? null :
                ((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(1), CellType.STRING))).toLowerCase();
                commonData.put("number_student", numberStudent);
                String userName = ExcelUtil.convertValue(row.getCell(2), CellType.STRING) == null ? null :
                        ((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(2), CellType.STRING))).toLowerCase();
                commonData.put("user_name", userName);
                String SBD = ExcelUtil.convertValue(row.getCell(3), CellType.STRING) == null ? null :
                        ((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(3), CellType.STRING))).toLowerCase();
                commonData.put("sbd", SBD);
                String fullNameTmp = ExcelUtil.convertValue(row.getCell(4), CellType.STRING) == null ? null :
                        ((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(4), CellType.STRING))).toLowerCase();
                String fullName = fullNameTmp.substring(0,1).toUpperCase() + fullNameTmp.substring(1);
                commonData.put("full_name", fullName);
                String dateOfBirth = ExcelUtil.convertValue(row.getCell(5), CellType.STRING) == null ? null :
                        (String) ExcelUtil.convertValue(row.getCell(5), CellType.STRING);
                commonData.put("date_of_birth", dateOfBirth);
                String sex = (String) ExcelUtil.convertValue(row.getCell(6), CellType.STRING);
                if (Objects.nonNull(sex)) {
                    commonData.put("sex",sex.equals(Constants.TITLE_SEX[0]) ? Constants.FEMALE : Constants.MALE);
                } else {
                    commonData.put("sex",Constants.FEMALE);
                }

                String admissionCode = (String) ExcelUtil.convertValue(row.getCell(7), CellType.STRING);
                commonData.put("admission_code",admissionCode);
                String admissionName = (String) ExcelUtil.convertValue(row.getCell(8), CellType.STRING);
                commonData.put("admission_name",admissionName);
                String PTXTCode = (String) ExcelUtil.convertValue(row.getCell(9), CellType.STRING);
                commonData.put("PTXT_code",PTXTCode);
                String admissionCombinationCode = (String) ExcelUtil.convertValue(row.getCell(10), CellType.STRING);
                commonData.put("admission_combination_code",admissionCombinationCode);
                String numberOrderOfOrigin = (String) ExcelUtil.convertValue(row.getCell(11), CellType.STRING);
                commonData.put("number_order_of_origin",numberOrderOfOrigin);
                Double graduationScore = Double.valueOf((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(12), CellType.STRING)));
                commonData.put("graduation_score",graduationScore);
                Integer encourageScore = Integer.valueOf((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(13), CellType.STRING)));
                commonData.put("encourage_score",encourageScore);
                String firstLessonName = (String) ExcelUtil.convertValue(row.getCell(14), CellType.STRING);
                commonData.put("first_lesson_name",firstLessonName);
                Double firstLessonScore = Double.valueOf((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(15), CellType.STRING)));
                commonData.put("first_lesson_score",firstLessonScore);
                String secondLessonName = (String) ExcelUtil.convertValue(row.getCell(16), CellType.STRING);
                commonData.put("second_lesson_name",secondLessonName);
                Double secondLessonScore = Double.valueOf((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(17), CellType.STRING)));
                commonData.put("second_lesson_score",secondLessonScore);
                String thirdLessonName = (String) ExcelUtil.convertValue(row.getCell(18), CellType.STRING);
                commonData.put("third_lesson_name",thirdLessonName);
                Double thirdLessonScore =  Double.valueOf((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(19), CellType.STRING)));
                commonData.put("third_lesson_score",thirdLessonScore);
                String priorityObject = (String) ExcelUtil.convertValue(row.getCell(20), CellType.STRING);
                commonData.put("priority_object",priorityObject);
                Integer priorityArea =  Integer.valueOf((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(21), CellType.STRING)));
                commonData.put("priority_area",priorityArea);
                Integer yearGrade = Integer.valueOf((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(22), CellType.STRING)));
                commonData.put("year_grade",yearGrade);
                String academicPerformance = (String) ExcelUtil.convertValue(row.getCell(23), CellType.STRING);
                commonData.put("academic_performance",academicPerformance);
                String conduct = (String) ExcelUtil.convertValue(row.getCell(24), CellType.STRING);
                commonData.put("conduct",conduct);
                Double scoreAverageTwelve =  Double.valueOf((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(25), CellType.STRING)));
                commonData.put("score_average_twelve",scoreAverageTwelve);
                String collegeGraduate = (String) ExcelUtil.convertValue(row.getCell(26), CellType.STRING);
                commonData.put("college_graduate",collegeGraduate);
                String highSchoolGraduate = (String) ExcelUtil.convertValue(row.getCell(27), CellType.STRING);
                commonData.put("high_school_graduate",highSchoolGraduate);
                String codeProvince = (String) ExcelUtil.convertValue(row.getCell(28), CellType.STRING);
                commonData.put("code_province",codeProvince);
                String nameProvince = (String) ExcelUtil.convertValue(row.getCell(29), CellType.STRING);
                commonData.put("name_province",nameProvince);
                String codeDistrict = (String) ExcelUtil.convertValue(row.getCell(30), CellType.STRING);
                commonData.put("code_district",codeDistrict);
                String nameDistrict = (String) ExcelUtil.convertValue(row.getCell(31), CellType.STRING);
                commonData.put("name_district",nameDistrict);
                String codeWards = (String) ExcelUtil.convertValue(row.getCell(32), CellType.STRING);
                commonData.put("code_wards",codeWards);
                String nameWards = (String) ExcelUtil.convertValue(row.getCell(33), CellType.STRING);
                commonData.put("name_wards",nameWards);
                String codeProvinceTwelve = (String) ExcelUtil.convertValue(row.getCell(34), CellType.STRING);
                commonData.put("code_province_twelve",codeProvinceTwelve);
                String codeSchoolTwelve = (String) ExcelUtil.convertValue(row.getCell(35), CellType.STRING);
                commonData.put("code_school_twelve",codeSchoolTwelve);
                String numberPhone = (String) ExcelUtil.convertValue(row.getCell(36), CellType.STRING);
                commonData.put("number_phone",numberPhone);
                String emailOther = (String) ExcelUtil.convertValue(row.getCell(37), CellType.STRING);
                commonData.put("email_other",emailOther);
                String NotificationAddress = (String) ExcelUtil.convertValue(row.getCell(38), CellType.STRING);
                commonData.put("Notification_Address",NotificationAddress);
                String placeOfBirth = (String) ExcelUtil.convertValue(row.getCell(39), CellType.STRING);
                commonData.put("place_of_birth",placeOfBirth);
                Integer codeNation =  Integer.valueOf((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(40), CellType.STRING)));
                commonData.put("code_nation",codeNation);
                String nation = (String) ExcelUtil.convertValue(row.getCell(41), CellType.STRING);
                commonData.put("nation",nation);
                String cccd = (String) ExcelUtil.convertValue(row.getCell(42), CellType.STRING);
                commonData.put("cccd",cccd);

                String titleYearGroup = ExcelUtil.convertValue(row.getCell(48), CellType.STRING) == null ? "" :
                        String.valueOf(ExcelUtil.convertValue(row.getCell(48), CellType.STRING));
                Integer idYearGroup = mapYearGroup.get(titleYearGroup);
                commonData.put("title_year_group",titleYearGroup);
                commonData.put("id_year_group",idYearGroup);
                String titlePriorityGroup = ExcelUtil.convertValue(row.getCell(49), CellType.STRING) == null ? "" :
                        String.valueOf(ExcelUtil.convertValue(row.getCell(49), CellType.STRING));
                Integer idPriorityGroup = mapPriorityGroup.get(titlePriorityGroup);
                commonData.put("title_year_group",titlePriorityGroup);
                commonData.put("id_priority_group",idPriorityGroup);
                if (userName == null || numberStudent == null) {
                    error = 1;
                }
                String valueUser = mapper.writeValueAsString(commonData);
                instance.setValue(valueUser);
                instance.setError(error);
                instances.add(instance);

            }
            return instances;


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean hasDataInRow(XSSFRow row, int numCellsToCheck) {
        int limit = Math.min(numCellsToCheck, row.getLastCellNum());
        int countCheckExits = 0;
        for (int cellIndex = 0; cellIndex < limit; cellIndex++) {
            XSSFCell cell = row.getCell(cellIndex);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                ++countCheckExits;
            }
        }
        if (countCheckExits == numCellsToCheck) {
            return true;
        }
        return false;
    }

    private Map<String, Integer> convertToMapPriorityGroup(List<PriorityGroup> allTPriorityGroup) {
        Map<String, Integer> PriorityGroupMap = new HashMap<>();
        for (PriorityGroup priorityGroup : allTPriorityGroup){
            PriorityGroupMap.put(priorityGroup.getTitle(), priorityGroup.getIdPriorityGroup());
        }
        return PriorityGroupMap;
    }

    private Map<String, Integer> convertToMapYearGroup(List<YearGroup> allTYearGroup) {
        Map<String, Integer> YearGroupMap = new HashMap<>();
        for (YearGroup yearGroup : allTYearGroup){
            YearGroupMap.put(yearGroup.getTitle(), yearGroup.getIdYearGroup());
        }
        return YearGroupMap;
    }

    private KtxUser getCustomUserDetailsFromFile(XSSFRow row) throws JsonProcessingException {
        KtxUser ktxUserCurrent =  (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        KtxUser customUserDetails = new KtxUser();
        Map<String, Object> commonData = new HashMap<>();
        String numberStudent = ((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(1), CellType.STRING))).toLowerCase();
        commonData.put("number_student", numberStudent);
        String userName = ((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(2), CellType.STRING))).toLowerCase();
        commonData.put("user_name", userName);
        String SBD = ((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(3), CellType.STRING))).toLowerCase();
        commonData.put("sbd", SBD);
        String fullNameTmp = ((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(4), CellType.STRING))).toLowerCase();
        String fullName = fullNameTmp.substring(0,1).toUpperCase() + fullNameTmp.substring(1);
        commonData.put("full_name", fullName);
        String dateOfBirth = (String) ExcelUtil.convertValue(row.getCell(5), CellType.STRING);
        commonData.put("date_of_birth", dateOfBirth);
        String sex = (String) ExcelUtil.convertValue(row.getCell(6), CellType.STRING);
        commonData.put("sex",sex);
        String admissionCode = (String) ExcelUtil.convertValue(row.getCell(7), CellType.STRING);
        commonData.put("admission_code",admissionCode);
        String admissionName = (String) ExcelUtil.convertValue(row.getCell(8), CellType.STRING);
        commonData.put("admission_name",admissionName);
        String PTXTCode = (String) ExcelUtil.convertValue(row.getCell(9), CellType.STRING);
        commonData.put("PTXT_code",PTXTCode);
        String admissionCombinationCode = (String) ExcelUtil.convertValue(row.getCell(10), CellType.STRING);
        commonData.put("admission_combination_code",admissionCombinationCode);
        String numberOrderOfOrigin = (String) ExcelUtil.convertValue(row.getCell(11), CellType.STRING);
        commonData.put("number_order_of_origin",numberOrderOfOrigin);
        Double graduationScore = Double.valueOf((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(12), CellType.STRING)));
        commonData.put("graduation_score",graduationScore);
        Integer encourageScore = Integer.valueOf((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(13), CellType.STRING)));
        commonData.put("encourage_score",encourageScore);
        String firstLessonName = (String) ExcelUtil.convertValue(row.getCell(14), CellType.STRING);
        commonData.put("first_lesson_name",firstLessonName);
        Double firstLessonScore = Double.valueOf((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(15), CellType.STRING)));
        commonData.put("first_lesson_score",firstLessonScore);
        String secondLessonName = (String) ExcelUtil.convertValue(row.getCell(16), CellType.STRING);
        commonData.put("second_lesson_name",secondLessonName);
        Double secondLessonScore = Double.valueOf((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(17), CellType.STRING)));
        commonData.put("second_lesson_score",secondLessonScore);
        String thirdLessonName = (String) ExcelUtil.convertValue(row.getCell(18), CellType.STRING);
        commonData.put("third_lesson_name",thirdLessonName);
        Double thirdLessonScore =  Double.valueOf((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(19), CellType.STRING)));
        commonData.put("third_lesson_score",thirdLessonScore);
        String priorityObject = (String) ExcelUtil.convertValue(row.getCell(20), CellType.STRING);
        commonData.put("priority_object",priorityObject);
        Integer priorityArea =  Integer.valueOf((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(21), CellType.STRING)));
        commonData.put("priority_area",priorityArea);
        Integer yearGrade = Integer.valueOf((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(22), CellType.STRING)));
        commonData.put("year_grade",yearGrade);
        String academicPerformance = (String) ExcelUtil.convertValue(row.getCell(23), CellType.STRING);
        commonData.put("academic_performance",academicPerformance);
        String conduct = (String) ExcelUtil.convertValue(row.getCell(24), CellType.STRING);
        commonData.put("conduct",conduct);
        Double scoreAverageTwelve =  Double.valueOf((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(25), CellType.STRING)));
        commonData.put("score_average_twelve",scoreAverageTwelve);
        String collegeGraduate = (String) ExcelUtil.convertValue(row.getCell(26), CellType.STRING);
        commonData.put("college_graduate",collegeGraduate);
        String highSchoolGraduate = (String) ExcelUtil.convertValue(row.getCell(27), CellType.STRING);
        commonData.put("high_school_graduate",highSchoolGraduate);
        String codeProvince = (String) ExcelUtil.convertValue(row.getCell(28), CellType.STRING);
        commonData.put("code_province",codeProvince);
        String nameProvince = (String) ExcelUtil.convertValue(row.getCell(29), CellType.STRING);
        commonData.put("name_province",nameProvince);
        String codeDistrict = (String) ExcelUtil.convertValue(row.getCell(30), CellType.STRING);
        commonData.put("code_district",codeDistrict);
        String nameDistrict = (String) ExcelUtil.convertValue(row.getCell(31), CellType.STRING);
        commonData.put("name_district",nameDistrict);
        String codeWards = (String) ExcelUtil.convertValue(row.getCell(32), CellType.STRING);
        commonData.put("code_wards",codeWards);
        String nameWards = (String) ExcelUtil.convertValue(row.getCell(33), CellType.STRING);
        commonData.put("name_wards",nameWards);
        String codeProvinceTwelve = (String) ExcelUtil.convertValue(row.getCell(34), CellType.STRING);
        commonData.put("code_province_twelve",codeProvinceTwelve);
        String codeSchoolTwelve = (String) ExcelUtil.convertValue(row.getCell(35), CellType.STRING);
        commonData.put("code_school_twelve",codeSchoolTwelve);
        String numberPhone = (String) ExcelUtil.convertValue(row.getCell(36), CellType.STRING);
        commonData.put("number_phone",numberPhone);
        String emailOther = (String) ExcelUtil.convertValue(row.getCell(37), CellType.STRING);
        commonData.put("email_other",emailOther);
        String NotificationAddress = (String) ExcelUtil.convertValue(row.getCell(38), CellType.STRING);
        commonData.put("Notification_Address",NotificationAddress);
        String placeOfBirth = (String) ExcelUtil.convertValue(row.getCell(39), CellType.STRING);
        commonData.put("place_of_birth",placeOfBirth);
        Integer codeNation =  Integer.valueOf((String) Objects.requireNonNull(ExcelUtil.convertValue(row.getCell(40), CellType.STRING)));
        commonData.put("code_nation",codeNation);
        String nation = (String) ExcelUtil.convertValue(row.getCell(41), CellType.STRING);
        commonData.put("nation",nation);
        String cccd = (String) ExcelUtil.convertValue(row.getCell(42), CellType.STRING);
        commonData.put("cccd",cccd);


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
        customUserDetails.setIdUserCreated(ktxUserCurrent.getIdKtxUser());
        customUserDetails.setIdUserModified(ktxUserCurrent.getIdKtxUser());
        customUserDetails.setTypeLogin(OAuth2Factory.azure.name());
        customUserDetails.setCodeUser(String.valueOf(UUID.randomUUID()));
        ObjectMapper objectMapper = new ObjectMapper();
        String valueUser = objectMapper.writeValueAsString(commonData);
        customUserDetails.setValue(valueUser);
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
