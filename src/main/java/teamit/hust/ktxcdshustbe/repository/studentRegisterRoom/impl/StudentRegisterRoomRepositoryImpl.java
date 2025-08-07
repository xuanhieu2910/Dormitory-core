package teamit.hust.ktxcdshustbe.repository.studentRegisterRoom.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.dto.registerRoom.StudentRegisterRoomDto;
import teamit.hust.ktxcdshustbe.dto.room.FindAllRoomsDto;
import teamit.hust.ktxcdshustbe.dto.studentRoom.DataStudentRegisterRoomDto;
import teamit.hust.ktxcdshustbe.dto.user.UserRegisterRoomDto;
import teamit.hust.ktxcdshustbe.entity.KtxUser;
import teamit.hust.ktxcdshustbe.entity.StudentRegisterRoom;
import teamit.hust.ktxcdshustbe.repository.studentRegisterRoom.StudentRegisterRoomRepositoryCustom;
import teamit.hust.ktxcdshustbe.request.user.UserRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.response.studentRegister.StudentRegisterRoomResponse;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.DateUtil;
import teamit.hust.ktxcdshustbe.utility.PageUtils;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class StudentRegisterRoomRepositoryImpl implements StudentRegisterRoomRepositoryCustom {
    
    @PersistenceContext
    EntityManager entityManager;




    @Override
    public int getBadgeRegisterRoomByUserId(Integer userId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(0) badgeStatusRegister " +
                "from student_register_room st " +
                "where st.id_user = :userId " +
                "and (st.status = 2 or st.status = 3) ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("userId", userId);
        return ValueUtil.getIntegerByObject(query.getFirstResult());
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
                response.setCodeRoom(ValueUtil.getStringByObject(obj[1]));
                response.setTitleRoom(ValueUtil.getStringByObject(obj[2]));
                response.setCodeDepartment(ValueUtil.getStringByObject(obj[3]));
                response.setTitleDepartment(ValueUtil.getStringByObject(obj[4]));
                String timeStarted = DateUtil.formatToPattern(ValueUtil.getDateByObject(obj[5]), DateUtil.DDMMYYYY);
                String timeEnded = DateUtil.formatToPattern(ValueUtil.getDateByObject(obj[6]), DateUtil.DDMMYYYY);
                String semester = ValueUtil.getStringByObject(obj[7]);
                response.setPrice(ValueUtil.getStringByObject(obj[9]));
                response.setStatusStudentRegisterRoom(ValueUtil.getIntegerByObject(obj[10]));
            }
        }
        return response;
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
                response.setCodeRoom(ValueUtil.getStringByObject(obj[1]));
                response.setTitleRoom(ValueUtil.getStringByObject(obj[2]));
                response.setCodeDepartment(ValueUtil.getStringByObject(obj[3]));
                response.setTitleDepartment(ValueUtil.getStringByObject(obj[4]));
                String timeStarted = DateUtil.formatToPattern(ValueUtil.getDateByObject(obj[5]), DateUtil.DDMMYYYY);
                String timeEnded = DateUtil.formatToPattern(ValueUtil.getDateByObject(obj[6]), DateUtil.DDMMYYYY);
                String semester = ValueUtil.getStringByObject(obj[7]);
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
        sb.append(" select ktxUser.code_user, ktxUser.value,    " +
                "        studentRegisterRoom.time_created,    " +
                "        de.code_department, de.title, ro.code_room, ro.title,    " +
                "        se.code_semester, se.title, timeHired.id_time_hired,    " +
                "        timeHired.time_started, timeHired.time_ended,studentRegisterRoom.status    " +
                "from student_register_room studentRegisterRoom " +
                "        inner join ktx_user ktxUser on ktxUser.id_ktx_user = studentRegisterRoom.id_user    " +
                "        inner join room ro on studentRegisterRoom.id_room = ro.id_room     " +
                "        inner join department de on ro.id_department = de.id_department    " +
                "        inner join time_hired timeHired on studentRegisterRoom.id_time_hired = timeHired.id_time_hired " +
                "        inner join batches_registration_schedule brs on studentRegisterRoom.id_batches_registration_schedule = brs.id_batches_registration_schedule " +
                "        inner join batches_registration br on br.id_batches_registration = brs.id_batches_registration " +
                "                and br.id_time_hired = timeHired.id_time_hired " +
                "        inner join batches_registration_room brr on brr.id_room = ro.id_room and br.id_batches_registration = brr.id_batches_registration " +
                "        inner join semester se on se.id_semester = br.id_semester " +
                "where 1 = 1 ");
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
                dto.setStatusInformationRegister(ValueUtil.getIntegerByObject(obj[12]));
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
        if(ObjectUtils.isNotEmpty(request.getStatus())){
            sb.append(" and studentRegisterRoom.status = :status ");
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
        if(ObjectUtils.isNotEmpty(request.getStatus())){
            query.setParameter("status",request.getStatus());
        }
        if (StringUtils.isNotBlank(request.getCodeSemester())) {
            query.setParameter("codeSemester", request.getCodeSemester());
        }
        if (StringUtils.isNotBlank(request.getTimeStarted()) && StringUtils.isNotBlank(request.getTimeEnded())){
            query.setParameter("timeStared", request.getTimeStarted());
            query.setParameter("timeEnded", request.getTimeEnded());
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
                "       srr.status,srr.id_user_modified,srr.id_user_created," +
                "       srr.id_order,srr.id_batches_registration_schedule,srr.expires_at  " +
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
                srr.setIdOrder(ValueUtil.getIntegerByObject(obj[9]));
                srr.setIdBatchesRegistrationSchedule(ValueUtil.getIntegerByObject(obj[10]));
                srr.setExpiresAt(ValueUtil.getLongByObject(obj[11]));
                return Optional.of(srr);
            }
        }
        return Optional.empty();
    }


    @Override
    public Optional<StudentRegisterRoom> findStudentRegisterRoomById(Integer studentRegisterRoomId) {
        StringBuilder sb = new StringBuilder();
        sb.append("select srr.id_student_register_room,srr.id_user,srr.time_created,  " +
                "       srr.time_modified,srr.id_room,srr.id_time_hired,  " +
                "       srr.status,srr.id_user_modified,srr.id_user_created, " +
                "       srr.id_order,srr.id_batches_registration_schedule,srr.expires_at  " +
                "from student_register_room srr  " +
                "where srr.id_student_register_room = :studentRegisterRoomId ");
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
                srr.setIdOrder(ValueUtil.getIntegerByObject(obj[9]));
                srr.setIdBatchesRegistrationSchedule(ValueUtil.getIntegerByObject(obj[10]));
                srr.setExpiresAt(ValueUtil.getLongByObject(obj[11]));
                return Optional.of(srr);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean isAllowRegisterBatchesRegistration() {
        StringBuilder sb = new StringBuilder();
        sb.append(" select case when exists( " +
                "select 1 " +
                "from batches_registration br " +
                "    inner join batches_year_group_registration bygr " +
                "        on br.id_batches_registration = bygr.id_batches_registration " +
                "    inner join year_group yg on bygr.id_year_group = yg.id_year_group " +
                "    inner join batches_registration_schedule brs on br.id_batches_registration = brs.id_batches_registration " +
                "    inner join priority_group pg on brs.id_priority_group = pg.id_priority_group " +
                "where :currentTime  between brs.registration_start_time and brs.registration_end_time " +
                "and pg.id_priority_group = :idPriorityGroup " +
                "and yg.id_year_group = :idYearGroup) then 1 else 0 end result ");
        Long timeCurrent =new Date().getTime();
        Query query = entityManager.createNativeQuery(sb.toString());
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        query.setParameter("idPriorityGroup", ktxUser.getIdPriorityGroup());
        query.setParameter("idYearGroup", ktxUser.getIdYearGroup());
        query.setParameter("currentTime", timeCurrent);
        return ValueUtil.getIntegerByObject(query.getSingleResult()).equals(1);
    }

    @Override
    public boolean isExistsRegisteredRoomInBatchesRegistrationCurrent() {
        StringBuilder sb = new StringBuilder();
        sb.append(" select case when exists( " +
                "    select 1 " +
                "from batches_registration br " +
                "         inner join batches_year_group_registration bygr " +
                "                    on br.id_batches_registration = bygr.id_batches_registration " +
                "         inner join year_group yg on bygr.id_year_group = yg.id_year_group " +
                "         inner join batches_registration_schedule brs on br.id_batches_registration = brs.id_batches_registration " +
                "         inner join priority_group pg on brs.id_priority_group = pg.id_priority_group " +
                "         inner join student_register_room srr on brs.id_batches_registration_schedule = srr.id_batches_registration_schedule " +
                "         inner join ktx_user ktu on srr.id_user = ktu.id_ktx_user " +
                "where :currentTime between brs.registration_start_time and brs.registration_end_time " +
                "  and pg.id_priority_group = :idPriorityGroup " +
                "  and yg.id_year_group = :idYearGroup " +
                "  and srr.id_order is not null " +
                "  and srr.status = :statusRegisterRoom " +
                "  and ktu.id_ktx_user = :idKtxUser " +
                ") then 1 else 0 end result ");
        Query query = entityManager.createNativeQuery(sb.toString());
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        query.setParameter("idPriorityGroup", ktxUser.getIdPriorityGroup());
        query.setParameter("idYearGroup", ktxUser.getIdYearGroup());
        query.setParameter("currentTime", new Date().getTime());
        query.setParameter("statusRegisterRoom", Constants.STATUS_SUCCESS_PAYMENT_STUDENT_ROOM_REGISTER);
        query.setParameter("idKtxUser", ktxUser.getIdKtxUser());
        return ValueUtil.getIntegerByObject(query.getSingleResult()).equals(1);
    }

    @Override
    public Optional<StudentRegisterRoomDto> getInformationRegisterRoomCurrent() {
        StringBuilder sb = new StringBuilder();
        sb.append(" select srr.id_student_register_room,  " +
                "        ro.code_room, ro.title,  " +
                "        de.code_department, de.title,  " +
                "        srr.time_created, th.time_started,  " +
                "        th.time_ended, ro.price,  " +
                "        srr.status, srr.expires_at, " +
                "        ord.code_order" +
                " from batches_registration br  " +
                "          inner join batches_year_group_registration bygr  " +
                "                on br.id_batches_registration = bygr.id_batches_registration " +
                "          inner join year_group yg on bygr.id_year_group = yg.id_year_group  " +
                "          inner join batches_registration_schedule brs on br.id_batches_registration = brs.id_batches_registration  " +
                "          inner join priority_group pg on brs.id_priority_group = pg.id_priority_group  " +
                "          inner join student_register_room srr  " +
                "                on brs.id_batches_registration_schedule = srr.id_batches_registration_schedule" +
                "          inner join room ro on srr.id_room = ro.id_room  " +
                "          inner join department de on ro.id_department = de.id_department  " +
                "          inner join ktx_user ktu on srr.id_user = ktu.id_ktx_user  " +
                "          inner join time_hired th on srr.id_time_hired = th.id_time_hired" +
                "          left join orders ord on srr.id_order = ord.id_order " +
                " where :currentTime between brs.registration_start_time and brs.registration_end_time  " +
                "    and :currentTime <= srr.expires_at " +
                "   and pg.id_priority_group = :idPriorityGroup  " +
                "   and yg.id_year_group = :idYearGroup  " +
                "   and ktu.id_ktx_user = :idKtxUser  ");
        Query query = entityManager.createNativeQuery(sb.toString());
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        query.setParameter("idPriorityGroup", ktxUser.getIdPriorityGroup());
        query.setParameter("idYearGroup", ktxUser.getIdYearGroup());
        query.setParameter("currentTime", new Date().getTime());
        query.setParameter("idKtxUser", ktxUser.getIdKtxUser());
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj : result) {
                StudentRegisterRoomDto dto = new StudentRegisterRoomDto();
                dto.setIdStudentRegisterRoom(ValueUtil.getIntegerByObject(obj[0]));
                dto.setCodeRoom(ValueUtil.getStringByObject(obj[1]));
                dto.setTitleRoom(ValueUtil.getStringByObject(obj[2]));
                dto.setCodeDepartment(ValueUtil.getStringByObject(obj[3]));
                dto.setTitleDepartment(ValueUtil.getStringByObject(obj[4]));
                dto.setTimeCreated(ValueUtil.getLongByObject(obj[5]));
                dto.setTimeHiredStarted(ValueUtil.getLongByObject(obj[6]));
                dto.setTimeHiredEnded(ValueUtil.getLongByObject(obj[7]));
                dto.setPrice(ValueUtil.getStringByObject(obj[8]));
                dto.setStatusStudentRegisterRoom(ValueUtil.getIntegerByObject(obj[9]));
                dto.setExpiresAt(ValueUtil.getLongByObject(obj[10]));
                dto.setCodeOrders(ValueUtil.getStringByObject(obj[11]));
                return Optional.of(dto);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean isAllowRegisterRoomByCodeRoom(String codeRoom) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select  " +
                "case when ( " +
                "select 1 " +
                "from batches_registration br  " +
                "         inner join batches_year_group_registration bygr  " +
                "                on br.id_batches_registration = bygr.id_batches_registration  " +
                "         inner join year_group yg on bygr.id_year_group = yg.id_year_group  " +
                "         inner join batches_registration_schedule brs on br.id_batches_registration = brs.id_batches_registration  " +
                "         inner join priority_group pg on brs.id_priority_group = pg.id_priority_group  " +
                "         inner join batches_registration_room brr  " +
                "                on br.id_batches_registration = brr.id_batches_registration  " +
                "         inner join room ro on brr.id_room = ro.id_room  " +
                "         inner join department de on ro.id_department = de.id_department  " +
                "where :currentTime between brs.registration_start_time and brs.registration_end_time  " +
                "  and pg.id_priority_group = :idPriorityGroup  " +
                "  and yg.id_year_group = :idYearGroup  " +
                "  and ro.code_room = :codeRoom " +
                "  and ro.remain_amount_register > 0) then 1 else 0 end ");
        Query query = entityManager.createNativeQuery(sb.toString());
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        query.setParameter("idPriorityGroup", ktxUser.getIdPriorityGroup());
        query.setParameter("idYearGroup", ktxUser.getIdYearGroup());
        query.setParameter("currentTime", new Date().getTime());
        query.setParameter("codeRoom", codeRoom);
        return ValueUtil.getIntegerByObject(query.getSingleResult()).equals(1);
    }

    @Override
    public Optional<DataStudentRegisterRoomDto> getDataStudentToRegisterRoomByCodeRoom(String codeRoom) {
        StringBuilder sb = new StringBuilder();
        sb.append("select br.id_batches_registration,  " +
                "       brs.id_batches_registration_schedule,  " +
                "       ro.id_room,  " +
                "       br.id_time_hired  " +
                "from batches_registration br   " +
                "         inner join batches_year_group_registration bygr   " +
                "                on br.id_batches_registration = bygr.id_batches_registration  " +
                "         inner join year_group yg on bygr.id_year_group = yg.id_year_group   " +
                "         inner join batches_registration_schedule brs on br.id_batches_registration = brs.id_batches_registration   " +
                "         inner join priority_group pg on brs.id_priority_group = pg.id_priority_group  " +
                "         inner join batches_registration_room brr  " +
                "                on br.id_batches_registration = brr.id_batches_registration  " +
                "         inner join room ro on brr.id_room = ro.id_room  " +
                "         inner join department de on ro.id_department = de.id_department  " +
                "where :currentTime between brs.registration_start_time and brs.registration_end_time   " +
                "  and pg.id_priority_group = :idPriorityGroup   " +
                "  and yg.id_year_group = :idYearGroup  " +
                "  and ro.code_room = :codeRoom  " +
                "  and ro.remain_amount_register > 0   ");
        Query query = entityManager.createNativeQuery(sb.toString());
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        query.setParameter("idPriorityGroup", ktxUser.getIdPriorityGroup());
        query.setParameter("idYearGroup", ktxUser.getIdYearGroup());
        query.setParameter("currentTime", new Date().getTime());
        query.setParameter("codeRoom", codeRoom);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                DataStudentRegisterRoomDto dataStudentRegisterRoomDto = new DataStudentRegisterRoomDto();
                dataStudentRegisterRoomDto.setIdBatchesRegister(ValueUtil.getIntegerByObject(obj[0]));
                dataStudentRegisterRoomDto.setIdBatchesRegisterSchedule(ValueUtil.getIntegerByObject(obj[1]));
                dataStudentRegisterRoomDto.setIdRoom(ValueUtil.getIntegerByObject(obj[2]));
                dataStudentRegisterRoomDto.setIdTimeHired(ValueUtil.getIntegerByObject(obj[3]));
                return Optional.of(dataStudentRegisterRoomDto);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<StudentRegisterRoom> findStudentRegisterRoomByIdStudentRegisterRoom(Integer idStudentRegisterRoom) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select srr.id_student_register_room, srr.id_user, srr.time_created,  " +
                "       srr.time_modified, srr.id_room, srr.id_time_hired, srr.status,  " +
                "       srr.id_user_modified, srr.id_user_created, srr.id_order,  " +
                "       srr.id_batches_registration_schedule, srr.expires_at  " +
                "from student_register_room srr   " +
                "where srr.id_student_register_room = :idStudentRegisterRoom ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("idStudentRegisterRoom", idStudentRegisterRoom);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                return Optional.of(writeStudentRegisterRoom(obj));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<StudentRegisterRoom> findStudentRegisterRoomByIdOrder(Integer idOrder) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select srr.id_student_register_room, srr.id_user, srr.time_created,   " +
                "       srr.time_modified, srr.id_room, srr.id_time_hired, srr.status,   " +
                "       srr.id_user_modified, srr.id_user_created, srr.id_order,   " +
                "       srr.id_batches_registration_schedule, srr.expires_at   " +
                "from student_register_room srr   " +
                "where srr.id_order = :idOrder  ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("idOrder", idOrder);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                return Optional.of(writeStudentRegisterRoom(obj));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<StudentRegisterRoom> findListStudentRegisterRoomByCodeRoomAndStatus(String codeRoom, Integer status) {
        StringBuilder sb = new StringBuilder();
        sb.append("select  srr.id_student_register_room,srr.id_user,  " +
                "        srr.time_created,srr.time_modified,srr.id_room,  " +
                "        srr.id_time_hired,srr.status,srr.id_user_created,  " +
                "        srr.id_user_modified,srr.id_order,  " +
                "        srr.id_batches_registration_schedule,srr.expires_at  " +
                "    from student_register_room srr  " +
                "    inner join room on srr.id_room = room.id_room  " +
                "    where room.code_room = :codeRoom  " +
                "    and srr.status = :status");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeRoom",codeRoom);
        query.setParameter("status", status);
        List<Object[]> result = query.getResultList();
        List<StudentRegisterRoom> studentRegisterRoomArrayList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                StudentRegisterRoom studentRegisterRoom = new StudentRegisterRoom();
                studentRegisterRoom.setIdStudentRegisterRoom(ValueUtil.getIntegerByObject(obj[0]));
                studentRegisterRoom.setIdUser(ValueUtil.getIntegerByObject(obj[1]));
                studentRegisterRoom.setTimeCreated(ValueUtil.getLongByObject(obj[2]));
                studentRegisterRoom.setTimeModified(ValueUtil.getLongByObject(obj[3]));
                studentRegisterRoom.setIdRoom(ValueUtil.getIntegerByObject(obj[4]));
                studentRegisterRoom.setIdTimeHired(ValueUtil.getIntegerByObject(obj[5]));
                studentRegisterRoom.setStatus(ValueUtil.getIntegerByObject(obj[6]));
                studentRegisterRoom.setIdUserCreated(ValueUtil.getIntegerByObject(obj[7]));
                studentRegisterRoom.setIdUserModified(ValueUtil.getIntegerByObject(obj[8]));
                studentRegisterRoom.setIdOrder(ValueUtil.getIntegerByObject(obj[9]));
                studentRegisterRoom.setIdBatchesRegistrationSchedule(ValueUtil.getIntegerByObject(obj[10]));
                studentRegisterRoom.setExpiresAt(ValueUtil.getLongByObject(obj[11]));
                studentRegisterRoomArrayList.add(studentRegisterRoom);
            }
        }
        return studentRegisterRoomArrayList;
    }

    @Override
    public Page<UserRegisterRoomDto> findAllInfoAnUserRegisterRoomDto(UserRegisterRoomRequest request, Pageable pageable) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select ktxUser.code_user, ktxUser.value, " +
                "        studentRegisterRoom.time_created, " +
                "        de.code_department, de.title, ro.code_room, ro.title, " +
                "        se.code_semester, se.title, timeHired.id_time_hired, " +
                "        timeHired.time_started, timeHired.time_ended,studentRegisterRoom.status " +
                " from student_register_room studentRegisterRoom    " +
                "        inner join ktx_user ktxUser on ktxUser.id_ktx_user = studentRegisterRoom.id_user " +
                "        inner join room ro on studentRegisterRoom.id_room = ro.id_room " +
                "        inner join department de on ro.id_department = de.id_department " +
                "        inner join time_hired timeHired on studentRegisterRoom.id_time_hired = timeHired.id_time_hired " +
                "        inner join batches_registration_schedule brs on studentRegisterRoom.id_batches_registration_schedule = brs.id_batches_registration_schedule " +
                "        inner join batches_registration br on br.id_batches_registration = brs.id_batches_registration " +
                "                  and br.id_time_hired = timeHired.id_time_hired " +
                "        inner join batches_registration_room brr on brr.id_room = ro.id_room and br.id_batches_registration = brr.id_batches_registration " +
                "        inner join semester se on se.id_semester = br.id_semester " +
                " where 1 = 1  and ktxUser.code_user = :codeUser ");
        setConditionFindAllInfoAnUserRegisterRoom(sb, request);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllInfoAnUserRegisterRoom(query, request);
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
                dto.setStatusInformationRegister(ValueUtil.getIntegerByObject(obj[12]));
                userRegisterRoomDtos.add(dto);
            }
        }
        return new PageImpl<>(userRegisterRoomDtos, pageable, countFindAllInfoAnUserRegisterRoomDto(request));
    }

    @Override
    public List<UserRegisterRoomDto> findListUserRegisterInRoom(Integer status) {
        StringBuilder sb = new StringBuilder();
        sb.append("select ktxUser.code_user, ktxUser.value,  " +
                "       studentRegisterRoom.time_created,  " +
                "       de.code_department, de.title, ro.code_room, ro.title,  " +
                "       se.code_semester, se.title, timeHired.id_time_hired,  " +
                "       timeHired.time_started, timeHired.time_ended,studentRegisterRoom.status  " +
                "from student_register_room studentRegisterRoom " +
                "       inner join ktx_user ktxUser on ktxUser.id_ktx_user = studentRegisterRoom.id_user  " +
                "       inner join room ro on studentRegisterRoom.id_room = ro.id_room  " +
                "       inner join department de on ro.id_department = de.id_department  " +
                "       inner join time_hired timeHired on studentRegisterRoom.id_time_hired = timeHired.id_time_hired  " +
                "       inner join batches_registration_room brr on brr.id_room = ro.id_room " +
                "       inner join batches_registration br on br.id_batches_registration = brr.id_batches_registration " +
                "       inner join semester se on se.id_semester = br.id_semester " +
                "       where 1 = 1  and studentRegisterRoom.status = :status " +
                "       ORDER BY studentRegisterRoom.time_created DESC  ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("status", status);
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
                dto.setStatusInformationRegister(ValueUtil.getIntegerByObject(obj[12]));
                userRegisterRoomDtos.add(dto);
            }
        }
        return userRegisterRoomDtos;
    }

    private long countFindAllInfoAnUserRegisterRoomDto(UserRegisterRoomRequest request) {
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
                "       where 1 = 1   and ktxUser.code_user = :codeUser ");
        setConditionFindAllInfoAnUserRegisterRoom(sb,request);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllInfoAnUserRegisterRoom(query,request);
        return ValueUtil.getLongByObject(query.getSingleResult());
    }

    private void setParameterFindAllInfoAnUserRegisterRoom(Query query, UserRegisterRoomRequest request) {
        query.setParameter("codeUser",request.getCodeUser());
        if(StringUtils.isNotBlank(request.getKeyword())){
            query.setParameter("keyword", request.getKeyword());
        }
        if (StringUtils.isNotBlank(request.getCodeDepartment())){
            query.setParameter("codeDepartment", request.getCodeDepartment());
        }
        if (StringUtils.isNotBlank(request.getCodeRoom())){
            query.setParameter("codeRoom", request.getCodeRoom());
        }
        if(ObjectUtils.isNotEmpty(request.getStatus())){
            query.setParameter("status",request.getStatus());
        }
        if (StringUtils.isNotBlank(request.getCodeSemester())) {
            query.setParameter("codeSemester", request.getCodeSemester());
        }
        if (StringUtils.isNotBlank(request.getTimeStarted()) && StringUtils.isNotBlank(request.getTimeEnded())){
            query.setParameter("timeStared", request.getTimeStarted());
            query.setParameter("timeEnded", request.getTimeEnded());
        }
    }

    private void setConditionFindAllInfoAnUserRegisterRoom(StringBuilder sb, UserRegisterRoomRequest request) {
        if (StringUtils.isNotBlank(request.getKeyword())){
            sb.append(" and ( (ktxUser.value REGEXP '[' + :keyword + ']' ) OR " +
                    "      (de.title REGEXP '[' + :keyword + ']' ) OR " +
                    "      (ro.title REGEXP '[' + :keyword + ']' ) ) ");
        }
        if (StringUtils.isNotBlank(request.getCodeDepartment())) {
            sb.append(" and de.code_department = :codeDepartment ");
        }
        if (StringUtils.isNotBlank(request.getCodeRoom())){
            sb.append(" and ro.code_room = :codeRoom ");
        }
        if(ObjectUtils.isNotEmpty(request.getStatus())){
            sb.append(" and studentRegisterRoom.status = :status ");
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

    private StudentRegisterRoom writeStudentRegisterRoom(Object[] obj) {
        StudentRegisterRoom studentRegisterRoom = new StudentRegisterRoom();
        studentRegisterRoom.setIdStudentRegisterRoom(ValueUtil.getIntegerByObject(obj[0]));
        studentRegisterRoom.setIdUser(ValueUtil.getIntegerByObject(obj[1]));
        studentRegisterRoom.setTimeCreated(ValueUtil.getLongByObject(obj[2]));
        studentRegisterRoom.setTimeModified(ValueUtil.getLongByObject(obj[3]));
        studentRegisterRoom.setIdRoom(ValueUtil.getIntegerByObject(obj[4]));
        studentRegisterRoom.setIdTimeHired(ValueUtil.getIntegerByObject(obj[5]));
        studentRegisterRoom.setStatus(ValueUtil.getIntegerByObject(obj[6]));
        studentRegisterRoom.setIdUserModified(ValueUtil.getIntegerByObject(obj[7]));
        studentRegisterRoom.setIdUserCreated(ValueUtil.getIntegerByObject(obj[8]));
        studentRegisterRoom.setIdOrder(ValueUtil.getIntegerByObject(obj[9]));
        studentRegisterRoom.setIdBatchesRegistrationSchedule(ValueUtil.getIntegerByObject(obj[10]));
        studentRegisterRoom.setExpiresAt(ValueUtil.getLongByObject(obj[11]));
        return studentRegisterRoom;
    }

    @Override
    public List<UserRegisterRoomDto> downloadListStudentRegisterRoom(UserRegisterRoomRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("select ktxUser.code_user, ktxUser.value,  " +
                "       studentRegisterRoom.time_created,  " +
                "       de.code_department, de.title, ro.code_room, ro.title,  " +
                "       se.code_semester, se.title, timeHired.id_time_hired,  " +
                "       timeHired.time_started, timeHired.time_ended,studentRegisterRoom.status  " +
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
                dto.setStatusInformationRegister(ValueUtil.getIntegerByObject(obj[12]));
                userRegisterRoomDtos.add(dto);
            }
        }
        return userRegisterRoomDtos;
    }
}
