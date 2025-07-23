package teamit.hust.ktxcdshustbe.repository.studentRegisterRoom.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.dto.registerRoom.AcceptStudentRegisterRoomDto;
import teamit.hust.ktxcdshustbe.dto.registerRoom.StatisticStudentRegisterRoomDto;
import teamit.hust.ktxcdshustbe.dto.user.UserRegisterRoomDto;
import teamit.hust.ktxcdshustbe.entity.StudentRegisterRoom;
import teamit.hust.ktxcdshustbe.repository.studentRegisterRoom.StudentRegisterRoomRepositoryCustom;
import teamit.hust.ktxcdshustbe.request.user.UserRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.response.registerRoom.InformationRoomRegisterResponse;
import teamit.hust.ktxcdshustbe.response.registerRoom.StudentRegisterRoomDetailResponse;
import teamit.hust.ktxcdshustbe.response.studentRegister.StudentRegisterRoomResponse;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.DateUtil;
import teamit.hust.ktxcdshustbe.utility.PageUtils;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentRegisterRoomRepositoryImpl implements StudentRegisterRoomRepositoryCustom {
    
    @PersistenceContext
    EntityManager entityManager;


    @Override
    public StudentRegisterRoomDetailResponse getStudentDetailRegisterRoomByRegisterId(Integer registerId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select   studentRegisterRoom.id_student_register_room studentRegisterId, " +
                "         ktxUser.code_user, " +
                "         ktxUser.full_name,  " +
                "         ktxUser.number_student,    " +
                "         ktxUser.date_of_birth,    " +
                "         ktxUser.sex,    " +
                "         ktxUser.religion,    " +
                "         ktxUser.cccd,    " +
                "         ktxUser.nation,    " +
                "         ktxUser.area,    " +
                "         ktxUser.phone_number,    " +
                "         ktxUser.email_contact,    " +
                "         ktxUser.class_user,    " +
                "         ktxUser.faculty,    " +
                "         ktxUser.year_grade,    " +
                "         ktxUser.province,    " +
                "         ktxUser.district,    " +
                "         ktxUser.wards,    " +
                "         ktxUser.school,    " +
                "         ktxUser.address,    " +
                "         ktxUser.code_major,    " +
                "         ktxUser.title_major,    " +
                "         ktxUser.type_login,    " +
                "         ktxUser.name_father,    " +
                "         ktxUser.year_father,    " +
                "         ktxUser.phone_number_father,    " +
                "         ktxUser.address_father,    " +
                "         ktxUser.name_mother,    " +
                "         ktxUser.year_mother,    " +
                "         ktxUser.phone_number_mother,    " +
                "         ktxUser.address_mother,    " +
                "         ktxUser.path_avatar,    " +
                "         ktxUser.address_contact, " +
                "         ro.title titleRoom, " +
                "         de.title titleDepartment, " +
                "         se.title semester, " +
                "         studentRegisterRoom.time_created, " +
                "         studentRegisterRoom.status statusRegisterRoom, " +
                "         studentRegisterRoom.path_payment pathPayment " +
                "from student_register_room studentRegisterRoom " +
                "           inner join ktx_user ktxUser on studentRegisterRoom.id_user = ktxUser.id_ktx_user " +
                "           inner join room ro on studentRegisterRoom.id_room = ro.id_room " +
                "           inner join department de on ro.id_department = de.id_department " +
                "           inner join time_hired timeHired on studentRegisterRoom.id_time_hired = timeHired.id_time_hired " +
                "           inner join semester se on timeHired.id_semester = se.id_semester " +
                "where studentRegisterRoom.id_student_register_room = :registerId ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("registerId", registerId);
        List<Object[]> result = query.getResultList();
        StudentRegisterRoomDetailResponse response = new StudentRegisterRoomDetailResponse();
        if (!CollectionUtils.isEmpty(result)){
           for (Object[] obj: result){
               response.setStudentRegisterId(ValueUtil.getIntegerByObject(obj[0]));
               response.setCodeUser(ValueUtil.getStringByObject(obj[1]));
               response.setFullName(ValueUtil.getStringByObject(obj[2]));
               response.setNumberStudent(ValueUtil.getStringByObject(obj[3]));
               response.setDateOfBirth(DateUtil.formatToPattern(ValueUtil.getDateByObject(obj[4]),DateUtil.DDMMYYYY));
               response.setSex(ValueUtil.getStringByObject(obj[5]));
               response.setReligion(ValueUtil.getStringByObject(obj[6]));
               response.setCccd(ValueUtil.getStringByObject(obj[7]));
               response.setNation(ValueUtil.getStringByObject(obj[8]));
               response.setArea(ValueUtil.getStringByObject(obj[9]));
               response.setPhoneNumber(ValueUtil.getStringByObject(obj[10]));
               response.setEmail(ValueUtil.getStringByObject(obj[11]));
               response.setClassUser(ValueUtil.getStringByObject(obj[12]));
               response.setFaculty(ValueUtil.getStringByObject(obj[13]));
               response.setYearGrade(ValueUtil.getStringByObject(obj[14]));
               response.setProvince(ValueUtil.getStringByObject(obj[15]));
               response.setDistrict(ValueUtil.getStringByObject(obj[16]));
               response.setWards(ValueUtil.getStringByObject(obj[17]));
               response.setSchool(ValueUtil.getStringByObject(obj[18]));
               response.setAddress(ValueUtil.getStringByObject(obj[19]));
               response.setCodeMajor(ValueUtil.getStringByObject(obj[20]));
               response.setTitleMajor(ValueUtil.getStringByObject(obj[21]));
               response.setTypeLogin(ValueUtil.getStringByObject(obj[22]));
               response.setNameFather(ValueUtil.getStringByObject(obj[23]));
               response.setYearFather(ValueUtil.getIntegerByObject(obj[24]));
               response.setPhoneNumberFather(ValueUtil.getStringByObject(obj[25]));
               response.setAddressFather(ValueUtil.getStringByObject(obj[26]));
               response.setNameMother(ValueUtil.getStringByObject(obj[27]));
               response.setYearMother(ValueUtil.getIntegerByObject(obj[28]));
               response.setPhoneNumberMother(ValueUtil.getStringByObject(obj[29]));
               response.setAddressMother(ValueUtil.getStringByObject(obj[30]));
               response.setPathAvatar(ValueUtil.getStringByObject(obj[31]));
               response.setAddressContact(ValueUtil.getStringByObject(obj[32]));
               InformationRoomRegisterResponse informationRoomRegisterResponse = new InformationRoomRegisterResponse();
               informationRoomRegisterResponse.setRoom(ValueUtil.getStringByObject(obj[33]));
               informationRoomRegisterResponse.setDepartment(ValueUtil.getStringByObject(obj[34]));
               informationRoomRegisterResponse.setSemester(ValueUtil.getStringByObject(obj[35]));
               informationRoomRegisterResponse.setTimeRegister(DateUtil.formatToPattern(ValueUtil.getDateByObject(obj[36]),DateUtil.DATE_FORMAT_HH_MM));
               informationRoomRegisterResponse.setStatusRegisterRoom(ValueUtil.getIntegerByObject(obj[37]));
               informationRoomRegisterResponse.setPathPayment(ValueUtil.getStringByObject(obj[38]));
               response.setInformationRoomRegisterResponse(informationRoomRegisterResponse);
           }
        }
        return response;
    }


    @Override
    public int getBadgeRegisterRoomByUserId(Integer userId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(0) badgeStatusRegister " +
                "from student_register_room st " +
                "where st.id_user = :userId " +
                "and (st.status = 2 or st.status = 3) ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("userId", userId);
        return ValueUtil.getIntegerByObject(query.getSingleResult());
    }

    @Override
    public StudentRegisterRoomResponse getInformationRegisterRoom(Integer userId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select st.id_student_register_room, ro.code_room, ro.title, de.code_department, de.title, " +
                "       timeHired.time_started, timeHired.time_ended, " +
                "       se.title, st.time_created, " +
                "       ro.price, st.status " +
                "from student_register_room st " +
                "    inner join room ro on st.id_room = ro.id_room " +
                "    inner join department de on ro.id_department = de.id_department " +
                "    inner join time_hired timeHired on st.id_time_hired = timeHired.id_time_hired " +
                "    inner join semester se on timeHired.id_semester = se.id_semester " +
                "where st.id_user = :userId " +
                "and (st.status = 2 or st.status = 3 or st.status = -1) " +
                "and timeHired.status = 1 " +
                "and de.status = 1 and ro.is_actived = 1 ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("userId", userId);
        List<Object[]> result = query.getResultList();
        StudentRegisterRoomResponse response = new StudentRegisterRoomResponse();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj: result){
                response.setStudentRegisterRoomId(ValueUtil.getIntegerByObject(obj[0]));
                response.setCodeRoom(ValueUtil.getStringByObject(obj[1]));
                response.setTitleRoom(ValueUtil.getStringByObject(obj[2]));
                response.setCodeDepartment(ValueUtil.getStringByObject(obj[3]));
                response.setTitleDepartment(ValueUtil.getStringByObject(obj[4]));
                String timeStarted = DateUtil.formatToPattern(ValueUtil.getDateByObject(obj[5]), DateUtil.DDMMYYYY);
                String timeEnded = DateUtil.formatToPattern(ValueUtil.getDateByObject(obj[6]), DateUtil.DDMMYYYY);
                String semester = ValueUtil.getStringByObject(obj[7]);
                response.setTimeHired(semester + " - " + timeStarted + " - " + timeEnded);
                response.setTimeCreated(DateUtil.formatToPattern(ValueUtil.getDateByObject(obj[8]), DateUtil.DDMMYYYY));
                response.setPrice(ValueUtil.getStringByObject(obj[9]));
                response.setStatusStudentRegisterRoom(ValueUtil.getIntegerByObject(obj[10]));
            }
        }
        return response;
    }

    @Override
    public Optional<StudentRegisterRoomResponse> findStudentRegisterRoomByUserId(Integer userId) {
        StringBuilder sb = new StringBuilder();
        sb.append("select st.id_student_register_room, ro.code_room, ro.title, de.code_department, de.title, " +
                "          timeHired.time_started, timeHired.time_ended,    " +
                "          se.title, st.time_created,    " +
                "          ro.price, st.status    " +
                "       from student_register_room st    " +
                "       inner join room ro on st.id_room = ro.id_room    " +
                "       inner join department de on ro.id_department = de.id_department " +
                "       inner join time_hired timeHired on st.id_time_hired = timeHired.id_time_hired   " +
                "       inner join semester se on timeHired.id_semester = se.id_semester " +
                "       where st.id_user = :userId    " +
                "       and (st.status = 2 or st.status = 3)    " +
                "       and timeHired.status = 1    " +
                "       and de.status = 1 and ro.is_active = 1  ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("userId", userId);
        List<Object[]> result = query.getResultList();
        StudentRegisterRoomResponse response = new StudentRegisterRoomResponse();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj: result){
                response.setStudentRegisterRoomId(ValueUtil.getIntegerByObject(obj[0]));
                response.setCodeRoom(ValueUtil.getStringByObject(obj[1]));
                response.setTitleRoom(ValueUtil.getStringByObject(obj[2]));
                response.setCodeDepartment(ValueUtil.getStringByObject(obj[3]));
                response.setTitleDepartment(ValueUtil.getStringByObject(obj[4]));
                String timeStarted = DateUtil.formatToPattern(ValueUtil.getDateByObject(obj[5]), DateUtil.DDMMYYYY);
                String timeEnded = DateUtil.formatToPattern(ValueUtil.getDateByObject(obj[6]), DateUtil.DDMMYYYY);
                String semester = ValueUtil.getStringByObject(obj[7]);
                response.setTimeHired(semester + " - " + timeStarted + " - " + timeEnded);
                response.setTimeCreated(DateUtil.formatToPattern(ValueUtil.getDateByObject(obj[8]), DateUtil.DDMMYYYY));
                response.setPrice(ValueUtil.getStringByObject(obj[9]));
                response.setStatusStudentRegisterRoom(ValueUtil.getIntegerByObject(obj[10]));
                return Optional.of(response);
            }
        }
        return Optional.empty();
    }


    @Override
    public AcceptStudentRegisterRoomDto getAcceptStudentRegisterRoomDtoById(Integer studentRegisterRoomId) {
        StringBuilder sb = new StringBuilder();
        sb.append("select de.title, ro.title, studentRegisterRoom.time_created, " +
                "       timeHired.time_started, timeHired.time_ended, " +
                "       se.title, ktxUser.user_name, ro.price " +
                "from student_register_room studentRegisterRoom " +
                "    inner join ktx_user ktxUser on studentRegisterRoom.id_user = ktxUser.id_ktx_user " +
                "    inner join room ro on studentRegisterRoom.id_room = ro.id_room " +
                "    inner join department de on ro.id_department = de.id_department " +
                "    inner join time_hired timeHired on studentRegisterRoom.id_time_hired = timeHired.id_time_hired " +
                "    inner join semester se on timeHired.id_semester = se.id_semester " +
                "where studentRegisterRoom.id_student_register_room = :idStudentRegisterRoom ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("idStudentRegisterRoom", studentRegisterRoomId);
        List<Object[]> result = query.getResultList();
        AcceptStudentRegisterRoomDto response = new AcceptStudentRegisterRoomDto();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj: result){
                response.setTitleDepartment(ValueUtil.getStringByObject(obj[0]));
                response.setTitleRoom(ValueUtil.getStringByObject(obj[1]));
                response.setTimeCreated(DateUtil.formatToPattern(ValueUtil.getDateByObject(obj[2]), DateUtil.DDMMYYYY));
                String timeStarted = DateUtil.formatToPattern(ValueUtil.getDateByObject(obj[3]), DateUtil.DDMMYYYY);
                String timeEnded = DateUtil.formatToPattern(ValueUtil.getDateByObject(obj[4]), DateUtil.DDMMYYYY);
                String semester = ValueUtil.getStringByObject(obj[5]);
                response.setTimeHired(semester + " - " + timeStarted + " - " + timeEnded);
                response.setUserName(ValueUtil.getStringByObject(obj[6]));
                response.setPrice(ValueUtil.getStringByObject(obj[7]));
            }
        }
        return response;
    }


    @Override
    public Optional<Integer> findDepartmentIdByRegisterId(Integer registerId) {
        return Optional.empty();
    }



    @Override
    public Optional<StudentRegisterRoomResponse> findStudentRegisterRoomByCodeUser(String codeUser) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select st.id_student_register_room, ro.code_room, ro.title, de.code_department, de.title       " +
                "   timeHired.time_started, timeHired.time_ended,                  " +
                "   se.title, st.time_created,                  " +
                "   ro.price, st.status                  " +
                "   from student_register_room st                  " +
                "   inner join room ro on st.id_room = ro.id_room             " +
                "   inner join department de on ro.id_department = de.id_department             " +
                "   inner join time_hired timeHired on st.id_time_hired = timeHired.id_time_hired             " +
                "   inner join semester se on timeHired.id_semester = se.id_semester             " +
                "   inner join ktx_user on ktx_user.id_ktx_user = st.id_user             " +
                "   where ktx_user.code_user = :codeUser             " +
                "   and (st.status = 2 or st.status = 3)                  " +
                "   and timeHired.status = 1                  " +
                "   and de.status = 1 and ro.is_active = 1  ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeUser", codeUser);
        List<Object[]> result = query.getResultList();
        StudentRegisterRoomResponse response = new StudentRegisterRoomResponse();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj: result){
                response.setStudentRegisterRoomId(ValueUtil.getIntegerByObject(obj[0]));
                response.setCodeRoom(ValueUtil.getStringByObject(obj[1]));
                response.setTitleRoom(ValueUtil.getStringByObject(obj[2]));
                response.setCodeDepartment(ValueUtil.getStringByObject(obj[3]));
                response.setTitleDepartment(ValueUtil.getStringByObject(obj[4]));
                String timeStarted = DateUtil.formatToPattern(ValueUtil.getDateByObject(obj[5]), DateUtil.DDMMYYYY);
                String timeEnded = DateUtil.formatToPattern(ValueUtil.getDateByObject(obj[6]), DateUtil.DDMMYYYY);
                String semester = ValueUtil.getStringByObject(obj[7]);
                response.setTimeHired(semester + " - " + timeStarted + " - " + timeEnded);
                response.setTimeCreated(DateUtil.formatToPattern(ValueUtil.getDateByObject(obj[8]), DateUtil.DDMMYYYY));
                response.setPrice(ValueUtil.getStringByObject(obj[9]));
                response.setStatusStudentRegisterRoom(ValueUtil.getIntegerByObject(obj[10]));
                return Optional.of(response);
            }
        }
        return Optional.empty();
    }

    @Override
    public Page<UserRegisterRoomDto> findAllUserRegisterRoomDto(UserRegisterRoomRequest request, Pageable pageable) {
        StringBuilder sb = new StringBuilder();
        sb.append("select ktxUser.code_user, ktxUser.value,  " +
                "       studentRegisterRoom.time_created,  " +
                "       de.code_department, de.title, ro.code_room, ro.title,  " +
                "       se.code_semester, se.title, timeHired.id_time_hired,  " +
                "       timeHired.time_started, timeHired.time_ended " +
                "from student_register_room studentRegisterRoom " +
                "       inner join ktx_user ktxUser on ktxUser.id_ktx_user = studentRegisterRoom.id_user  " +
                "       inner join room ro on studentRegisterRoom.id_room = ro.id_room  " +
                "       inner join department de on ro.id_department = de.id_department  " +
                "       inner join time_hired timeHired on studentRegisterRoom.id_time_hired = timeHired.id_time_hired  " +
                "       inner join batches_registration_room brr on brr.id_room = ro.id_room " +
                "       inner join batches_registration br on br.id_batches_registration = brr.id_batches_registration " +
                "       inner join semester se on se.id_semester = br.id_semester " +
                "       where 1 = 1   ");
        setConditionFindAllUserRegisterRoom(sb, request);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllUserRegisterRoom(query, request);
        PageUtils.buildQuery(pageable, query);
        List<Object[]> result = query.getResultList();
        List<UserRegisterRoomDto> userRegisterRoomDtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj: result){
                UserRegisterRoomDto dto = new UserRegisterRoomDto();
                dto.setCodeUser(ValueUtil.getStringByObject(obj[0]));
                dto.setValue(ValueUtil.getStringByObject(obj[1]));
                dto.setTimeRegister(ValueUtil.getLongByObject(obj[2]));
                dto.setCodeDepartment(ValueUtil.getStringByObject(obj[3]));
                dto.setTitleDepartment(ValueUtil.getStringByObject(obj[4]));
                dto.setCodeRoom(ValueUtil.getStringByObject(obj[5]));
                dto.setTitleRoom(ValueUtil.getStringByObject(obj[6]));
                dto.setCodeSemester(ValueUtil.getStringByObject(obj[7]));
                dto.setTitleSemester(ValueUtil.getStringByObject(obj[8]));
                dto.setIdTimeHired(ValueUtil.getIntegerByObject(obj[9]));
                dto.setTimeHiredStarted(ValueUtil.getLongByObject(obj[10]));
                dto.setTimeHiredEnded(ValueUtil.getLongByObject(obj[11]));
                userRegisterRoomDtos.add(dto);
            }
        }
        return new PageImpl<>(userRegisterRoomDtos, pageable, countFindAllUserRegisterRoomDto(request));
    }

    private void setConditionFindAllUserRegisterRoom(StringBuilder sb, UserRegisterRoomRequest request) {
        if (StringUtils.isNotBlank(request.getKeyword())){
            sb.append(" and ( (ktxUser.value REGEXP '[' + :keyword + ']' ) OR " +
                    "      (de.title REGEXP '[' + :keyword + ']' ) OR " +
                    "      (ro.title REGEXP '[' + :keyword + ']' ) ) ");
        }
        if (StringUtils.isNotBlank(request.getCodeDepartment())) {
            sb.append(" and de.code_department = :codeDepartment ");
        }
        if (StringUtils.isNotBlank(request.getCodeUser())) {
            sb.append(" and ktxUser.code_user = :codeUser ");
        }
        if (StringUtils.isNotBlank(request.getCodeRoom())){
            sb.append(" and ro.code_room = :codeRoom ");
        }
        if (StringUtils.isNotBlank(request.getCodeSemester())) {
            sb.append(" and se.codeSemester  = :codeSemester ");
        }
        if (StringUtils.isNotBlank(request.getTimeStarted()) && StringUtils.isNotBlank(request.getTimeEnded())){
            sb.append(" and (DATE_FORMAT(studentRegisterRoom.time_created,'%d/%m/%Y') BETWEEN " +
                    "    DATE_FORMAT(STR_TO_DATE(:timeStared, '%d/%m/%Y'),'%d/%m/%Y') AND " +
                    "    DATE_FORMAT(STR_TO_DATE(:timeEnded,'%d/%m/%Y'),'%d/%m/%Y')) ");
        }


        if (StringUtils.isNotBlank(request.getSortBy())
                && request.getSortBy().equals("timeRegister")) {
            sb.append(" ORDER BY studentRegisterRoom.time_created ");
            if (StringUtils.isNotBlank(request.getSortOrder()) && request.getSortOrder().equals(Constants.SORT_ASC)) {
                sb.append(Constants.SORT_ASC);
            } else {
                sb.append(Constants.SORT_DESC);
            }
        }
    }

    private void setParameterFindAllUserRegisterRoom(Query query, UserRegisterRoomRequest request) {
        if(StringUtils.isNotBlank(request.getKeyword())){
            query.setParameter("keyword", request.getKeyword());
        }
        if (StringUtils.isNotBlank(request.getCodeDepartment())){
            query.setParameter("codeDepartment", request.getCodeDepartment());
        }
        if (StringUtils.isNotBlank(request.getCodeUser())){
            query.setParameter("codeUser", request.getCodeUser());
        }
        if (StringUtils.isNotBlank(request.getCodeRoom())){
            query.setParameter("codeRoom", request.getCodeRoom());
        }

        if (StringUtils.isNotBlank(request.getCodeSemester())) {
            query.setParameter("codeSemester", request.getCodeSemester());
        }
        if (StringUtils.isNotBlank(request.getTimeStarted()) && StringUtils.isNotBlank(request.getTimeEnded())){
            query.setParameter("timeStared", request.getTimeStarted());
            query.setParameter("timeEnded", request.getTimeEnded());
        }
        if (null != request.getStatus()) {
            query.setParameter("status", request.getStatus());
        }
    }

    private long countFindAllUserRegisterRoomDto(UserRegisterRoomRequest request){
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(0) " +
                "from student_register_room studentRegisterRoom " +
                "       inner join ktx_user ktxUser on ktxUser.id_ktx_user = studentRegisterRoom.id_user  " +
                "       inner join room ro on studentRegisterRoom.id_room = ro.id_room  " +
                "       inner join department de on ro.id_department = de.id_department  " +
                "       inner join time_hired timeHired on studentRegisterRoom.id_time_hired = timeHired.id_time_hired  " +
                "       inner join batches_registration_room brr on brr.id_room = ro.id_room " +
                "       inner join batches_registration br on br.id_batches_registration = brr.id_batches_registration " +
                "       inner join semester se on se.id_semester = br.id_semester " +
                "       where 1 = 1   ");
        setConditionFindAllUserRegisterRoom(sb,request);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllUserRegisterRoom(query,request);
        return ValueUtil.getLongByObject(query.getSingleResult());
    }

    @Override
    public Optional<StudentRegisterRoom> findByCode(String codeUser) {
        StringBuilder sb = new StringBuilder();
        sb.append("select srr.id_student_register_room,srr.id_user,srr.time_created,  " +
                "       srr.time_modified,srr.id_room,srr.id_time_hired,  " +
                "       srr.status,srr.id_user_modified,srr.id_user_created,srr.is_payment  " +
                "from student_register_room srr  " +
                "inner join ktx_user on srr.id_user = ktx_user.id_ktx_user  " +
                "where ktx_user.code_user = :codeUser ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeUser", codeUser);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj: result){
                StudentRegisterRoom srr = new StudentRegisterRoom();
                srr.setIdStudentRegisterRoom(ValueUtil.getIntegerByObject(obj[0]));
                srr.setIdUser(ValueUtil.getIntegerByObject(obj[1]));
                srr.setTimeCreated(ValueUtil.getLongByObject(obj[2]));
                srr.setTimeModified(ValueUtil.getLongByObject(obj[3]));
                srr.setIdRoom(ValueUtil.getIntegerByObject(obj[4]));
                srr.setIdTimeHired(ValueUtil.getIntegerByObject(obj[5]));
                srr.setStatus(ValueUtil.getIntegerByObject(obj[6]));
                srr.setIdUserModified(ValueUtil.getIntegerByObject(obj[7]));
                srr.setIdUserCreated(ValueUtil.getIntegerByObject(obj[8]));
                return Optional.of(srr);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<StudentRegisterRoom> findByUserIdAndTimeIdHired(Integer userId, Integer timeIdHired) {
        StringBuilder sb = new StringBuilder();
        sb.append("select srr.id_student_register_room,srr.id_user,srr.time_created,  " +
                "       srr.time_modified,srr.id_room,srr.id_time_hired,  " +
                "       srr.status,srr.id_user_modified,srr.id_user_created,srr.is_payment  " +
                "from student_register_room srr  " +
                "inner join ktx_user on srr.id_user = ktx_user.id_ktx_user  " +
                "where srr.id_user = :userId and srr.id_time_hired = :timeIdHired ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("userId", userId);
        query.setParameter("timeIdHired", timeIdHired);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj: result){
                StudentRegisterRoom srr = new StudentRegisterRoom();
                srr.setIdStudentRegisterRoom(ValueUtil.getIntegerByObject(obj[0]));
                srr.setIdUser(ValueUtil.getIntegerByObject(obj[1]));
                srr.setTimeCreated(ValueUtil.getLongByObject(obj[2]));
                srr.setTimeModified(ValueUtil.getLongByObject(obj[3]));
                srr.setIdRoom(ValueUtil.getIntegerByObject(obj[4]));
                srr.setIdTimeHired(ValueUtil.getIntegerByObject(obj[5]));
                srr.setStatus(ValueUtil.getIntegerByObject(obj[6]));
                srr.setIdUserModified(ValueUtil.getIntegerByObject(obj[7]));
                srr.setIdUserCreated(ValueUtil.getIntegerByObject(obj[8]));
                return Optional.of(srr);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean existsBySemesterId(Integer semesterId) {
        return false;
    }

    @Override
    public Optional<StudentRegisterRoom> findStudentRegisterRoomById(Integer studentRegisterRoomId) {
        StringBuilder sb = new StringBuilder();
        sb.append("select srr.id_student_register_room,srr.id_user,srr.time_created,  " +
                "       srr.time_modified,srr.id_room,srr.id_time_hired,  " +
                "       srr.status,srr.id_user_modified,srr.id_user_created,srr.is_payment  " +
                "from student_register_room srr  " +
                "where ktx_user.id_student_register_room = :studentRegisterRoomId ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("studentRegisterRoomId", studentRegisterRoomId);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj: result){
                StudentRegisterRoom srr = new StudentRegisterRoom();
                srr.setIdStudentRegisterRoom(ValueUtil.getIntegerByObject(obj[0]));
                srr.setIdUser(ValueUtil.getIntegerByObject(obj[1]));
                srr.setTimeCreated(ValueUtil.getLongByObject(obj[2]));
                srr.setTimeModified(ValueUtil.getLongByObject(obj[3]));
                srr.setIdRoom(ValueUtil.getIntegerByObject(obj[4]));
                srr.setIdTimeHired(ValueUtil.getIntegerByObject(obj[5]));
                srr.setStatus(ValueUtil.getIntegerByObject(obj[6]));
                srr.setIdUserModified(ValueUtil.getIntegerByObject(obj[7]));
                srr.setIdUserCreated(ValueUtil.getIntegerByObject(obj[8]));
                return Optional.of(srr);
            }
        }
        return Optional.empty();
    }
}
