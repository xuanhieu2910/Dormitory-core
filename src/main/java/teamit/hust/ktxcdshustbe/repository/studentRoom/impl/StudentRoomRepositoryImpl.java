package teamit.hust.ktxcdshustbe.repository.studentRoom.impl;

import com.azure.core.implementation.util.ObjectsUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.dto.studentRoom.FindAllStudentHiredRoomDto;
import teamit.hust.ktxcdshustbe.entity.StudentRoom;
import teamit.hust.ktxcdshustbe.repository.studentRoom.StudentRoomRepositoryCustom;
import teamit.hust.ktxcdshustbe.request.studentRoom.ListStudentHiredRoomRequest;
import teamit.hust.ktxcdshustbe.request.user.StudentListRoomHiredRequest;
import teamit.hust.ktxcdshustbe.response.registerRoom.UserFamilyDetailResponse;
import teamit.hust.ktxcdshustbe.response.studentRoom.ListStudentHiredRoomResponse;
import teamit.hust.ktxcdshustbe.response.studentRoom.StudentSearchAddNewRoomResponse;
import teamit.hust.ktxcdshustbe.response.user.HiredRoomsResponse;
import teamit.hust.ktxcdshustbe.response.user.ListHiredRoomStudentResponse;
import teamit.hust.ktxcdshustbe.response.user.UserHiredRoomDetailResponse;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.DateUtil;
import teamit.hust.ktxcdshustbe.utility.PageUtils;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class StudentRoomRepositoryImpl implements StudentRoomRepositoryCustom {


    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<HiredRoomsResponse> getRoomsHiredByUser(String codeUser) {
        StringBuilder sb = new StringBuilder();
        sb.append("select studentRoom.id_student_room, " +
                "       de.code_department,  " +
                "       de.title,  " +
                "       ro.code_room,  " +
                "       ro.title,  " +
                "       ktxUserModified.code_user,  " +
                "       ktxUserModified.full_name,  " +
                "       timeHired.time_started,  " +
                "       timeHired.time_ended,  " +
                "       studentRoom.status,  " +
                "       se.title  " +
                "from student_room studentRoom  " +
                "         inner join room ro on studentRoom.id_room = ro.id_room " +
                "         inner join department de on ro.id_department = de.id_department  " +
                "         inner join ktx_user ktxUser on studentRoom.id_user = ktxUser.id_ktx_user  " +
                "         left join ktx_user ktxUserModified on studentRoom.id_user_modified = ktxUserModified.id_ktx_user  " +
                "         inner join time_hired timeHired on studentRoom.id_time_hired = timeHired.id_time_hired  " +
                "         inner join semester se on timeHired.id_semester = se.id_semester  " +
                "where ktxUser.code_user = :codeUser ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeUser", codeUser);
        List<Object[]> result = query.getResultList();
        List<HiredRoomsResponse> responses = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj: result) {
                String timeStarted = DateUtil.formatToPattern(ValueUtil.getDateByObject(obj[7]),DateUtil.DATE_FORMAT_HH_MM);
                String timeEnded = DateUtil.formatToPattern(ValueUtil.getDateByObject(obj[8]), DateUtil.DATE_FORMAT_HH_MM);
                String titleSemester = ValueUtil.getStringByObject(obj[10]);
                String timeHired = titleSemester + " " + timeStarted + "-" + timeEnded;
                responses.add(HiredRoomsResponse.builder()
                        .idHiredUserRoom(ValueUtil.getIntegerByObject(obj[0]))
                        .codeDepartment(ValueUtil.getStringByObject(obj[1]))
                        .titleDepartment(ValueUtil.getStringByObject(obj[2]))
                        .codeRoom(ValueUtil.getStringByObject(obj[3]))
                        .titleRoom(ValueUtil.getStringByObject(obj[4]))
                        .codeUserModified(ValueUtil.getStringByObject(obj[5]))
                        .fullNameUserModified(ValueUtil.getStringByObject(obj[6]))
                        .timeHired(timeHired)
                        .status(ValueUtil.getIntegerByObject(obj[9]))
                        .titleSemester(titleSemester)
                        .build());
            }
        }
        return responses;
    }


    @Override
    public Page<ListHiredRoomStudentResponse> getListHiredRoomStudentResponse(StudentListRoomHiredRequest request, Pageable pageable) {
        StringBuilder sb = new StringBuilder();
        sb.append("select studentRoom.id_student_room studentRoomId,   " +
                "       ktxUser.code_user ,   " +
                "       de.code_department , de.title titleDepartment,   " +
                "       ro.code_room roomId, ro.title titleRoom,   " +
                "       se.title, timeHired.time_started, timeHired.time_ended,   " +
                "       studentRoom.status   " +
                "from student_room studentRoom   " +
                "    inner join room ro on studentRoom.id_room = ro.id_room   " +
                "    inner join department de on ro.id_department = de.id_department   " +
                "    inner join ktx_user ktxUser on studentRoom.id_user = ktxUser.id_ktx_user   " +
                "    inner join time_hired timeHired on studentRoom.id_time_hired = timeHired.id_time_hired  " +
                "    inner join batches_registration_room brr on brr.id_room = ro.id_room  " +
                "    inner join batches_registration br on br.id_batches_registration = brr.id_batches_registration   " +
                "    inner join semester se on se.id_semester = br.id_semester  " +
                "where ktxUser.code_user = :codeUser  ");
        setConditionListHiredRoomStudentResponse(sb, request);
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeUser", request.getCodeUser());
        setParametersListHiredRoomStudentResponse(query, request);
        PageUtils.buildQuery(pageable, query);
        List<Object[]> result = query.getResultList();
        List<ListHiredRoomStudentResponse> responses = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj: result){
                ListHiredRoomStudentResponse res = new ListHiredRoomStudentResponse();
                res.setStudentRoomId(ValueUtil.getIntegerByObject(obj[0]));
                res.setCodeUser(ValueUtil.getStringByObject(obj[1]));
                res.setCodeDepartment(ValueUtil.getStringByObject(obj[2]));
                res.setTitleDepartment(ValueUtil.getStringByObject(obj[3]));
                res.setCodeRoom(ValueUtil.getStringByObject(obj[4]));
                res.setTitleRoom(ValueUtil.getStringByObject(obj[5]));
                res.setHiredRoom(ValueUtil.getStringByObject(obj[6] + " - "
                        + DateUtil.formatToPattern(ValueUtil.getDateByObject(obj[7]),DateUtil.DATE_FORMAT_HH_MM) + " - "
                        + DateUtil.formatToPattern(ValueUtil.getDateByObject(obj[8]),DateUtil.DATE_FORMAT_HH_MM)));
                res.setStatus(ValueUtil.getIntegerByObject(obj[9]));
                responses.add(res);
            }
        }
        return new PageImpl<>(responses, pageable, countListHiredRoomStudentResponse(request));
    }

    @Override
    public Page<FindAllStudentHiredRoomDto> getListStudentHiredRoomResponse(ListStudentHiredRoomRequest request, Pageable pageable) {
        StringBuilder sb = new StringBuilder();
        sb.append("select studentRoom.id_student_room,ktxUser.code_user, ktxUser.value, " +
                "       timeHired.time_started, timeHired.time_ended,semester.title, " +
                "       de.code_department, de.title titleDepartment, ro.code_room,   " +
                "       ro.title roomTitle, userModified.code_user,userModified.value,studentRoom.status  " +
                "from student_room studentRoom    " +
                "    inner join ktx_user ktxUser on studentRoom.id_user = ktxUser.id_ktx_user   " +
                "    left join ktx_user userModified on studentRoom.id_user_modified = userModified.id_ktx_user   " +
                "    inner join room ro on studentRoom.id_room = ro.id_room   " +
                "    inner join department de on ro.id_department = de.id_department   " +
                "    inner join time_hired timeHired on studentRoom.id_time_hired = timeHired.id_time_hired " +
                "    inner join batches_registration_room brr on brr.id_room = ro.id_room " +
                "    inner join batches_registration br on br.id_batches_registration = brr.id_batches_registration " +
                "    inner join semester on semester.id_semester = br.id_semester " +
                "where 1 = 1   ");
        setConditionListStudentHiredRoomResponse(request,sb);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParametersListStudentHiredRoomResponse(request,query);
        PageUtils.buildQuery(pageable, query);
        List<Object[]> result = query.getResultList();
        List<FindAllStudentHiredRoomDto> responses = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj: result) {

                String dateStarted = ValueUtil.getStringByObject(obj[3]);
                String dateEnded = ValueUtil.getStringByObject(obj[4]);
                String titleSemester = ValueUtil.getStringByObject(obj[5]);
                FindAllStudentHiredRoomDto res = new FindAllStudentHiredRoomDto();
                res.setIdStudentRoom(ValueUtil.getIntegerByObject(obj[0]));
                res.setCodeUser(ValueUtil.getStringByObject(obj[1]));
                res.setValueUser(ValueUtil.getStringByObject(obj[2]));
                res.setTimeHired(titleSemester + " - " + dateStarted + " - " + dateEnded);
                res.setCodeDepartment(ValueUtil.getStringByObject(obj[6]));
                res.setTitleDepartment(ValueUtil.getStringByObject(obj[7]));
                res.setCodeRoom(ValueUtil.getStringByObject(obj[8]));
                res.setTitleRoom(ValueUtil.getStringByObject(obj[9]));
                res.setCodeUserModified(ValueUtil.getStringByObject(obj[10]));
                res.setValueUserModified(ValueUtil.getStringByObject(obj[11]));
                res.setStatus(ValueUtil.getIntegerByObject(obj[12]));
                responses.add(res);
            }
        }
        return new PageImpl<>(responses, pageable, countListStudentHiredRoomResponse(request));
    }

    @Override
    public List<FindAllStudentHiredRoomDto> findAllStudentsForExport(ListStudentHiredRoomRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("select studentRoom.id_student_room,ktxUser.code_user, ktxUser.value, " +
                "       timeHired.time_started, timeHired.time_ended,semester.title, " +
                "       de.code_department, de.title titleDepartment, ro.code_room,   " +
                "       ro.title roomTitle, userModified.code_user,userModified.value,studentRoom.status " +
                "from student_room studentRoom    " +
                "    inner join ktx_user ktxUser on studentRoom.id_user = ktxUser.id_ktx_user   " +
                "    left join ktx_user userModified on studentRoom.id_user_modified = userModified.id_ktx_user   " +
                "    inner join room ro on studentRoom.id_room = ro.id_room   " +
                "    inner join department de on ro.id_department = de.id_department   " +
                "    inner join time_hired timeHired on studentRoom.id_time_hired = timeHired.id_time_hired " +
                "    inner join batches_registration_room brr on brr.id_room = ro.id_room " +
                "    inner join batches_registration br on br.id_batches_registration = brr.id_batches_registration " +
                "    inner join semester on semester.id_semester = br.id_semester " +
                "where 1 = 1   ");

        setConditionListStudentHiredRoomResponse(request, sb);
        sb.append(" ORDER BY de.title, ro.title, ktxUser.code_user ASC");

        Query query = entityManager.createNativeQuery(sb.toString());
        setParametersListStudentHiredRoomResponse(request, query);

        List<Object[]> result = query.getResultList();
        List<FindAllStudentHiredRoomDto> responses = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj : result) {
                String dateStarted = ValueUtil.getStringByObject(obj[3]);
                String dateEnded = ValueUtil.getStringByObject(obj[4]);
                String titleSemester = ValueUtil.getStringByObject(obj[5]);
                FindAllStudentHiredRoomDto res = new FindAllStudentHiredRoomDto();
                res.setIdStudentRoom(ValueUtil.getIntegerByObject(obj[0]));
                res.setCodeUser(ValueUtil.getStringByObject(obj[1]));
                res.setValueUser(ValueUtil.getStringByObject(obj[2]));
                res.setTimeHired(titleSemester + " - " + dateStarted + " - " + dateEnded);
                res.setCodeDepartment(ValueUtil.getStringByObject(obj[6]));
                res.setTitleDepartment(ValueUtil.getStringByObject(obj[7]));
                res.setCodeRoom(ValueUtil.getStringByObject(obj[8]));
                res.setTitleRoom(ValueUtil.getStringByObject(obj[9]));
                res.setCodeUserModified(ValueUtil.getStringByObject(obj[10]));
                res.setValueUserModified(ValueUtil.getStringByObject(obj[11]));
                res.setStatus(ValueUtil.getIntegerByObject(obj[12]));
                responses.add(res);
            }
        }
        return responses;
    }

    @Override
    public Optional<StudentRoom> findStudentRoomByStudentRoomId(Integer studentRoomId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select id_student_room, id_user, id_room, " +
                "       time_created, time_modified,  " +
                "       id_time_hired, id_user_created ,id_user_modified, status " +
                "from student_room studentRoom  " +
                "where studentRoom.id = :studentRoomId ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("studentRoomId", studentRoomId);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj: result){
                StudentRoom studentRoom = new StudentRoom();
                studentRoom.setIdStudentRoom(ValueUtil.getIntegerByObject(obj[0]));
                studentRoom.setIdUser(ValueUtil.getIntegerByObject(obj[1]));
                studentRoom.setIdRoom(ValueUtil.getIntegerByObject(obj[2]));
                studentRoom.setTimeCreated(ValueUtil.getLongByObject(obj[3]));
                studentRoom.setTimeModified(ValueUtil.getLongByObject(obj[4]));
                studentRoom.setIdTimeHired(ValueUtil.getIntegerByObject(obj[5]));
                studentRoom.setIdUserCreated(ValueUtil.getIntegerByObject(obj[6]));
                studentRoom.setIdUserModified(ValueUtil.getIntegerByObject(obj[7]));
                studentRoom.setStatus(ValueUtil.getIntegerByObject(obj[8]));
                return Optional.of(studentRoom);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<StudentSearchAddNewRoomResponse> searchStudentAddNewRoom(String numberStudent) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select ktxUser.code_user, ktxUser.user_name, " +
                "       ktxUser.full_name, ktxUser.phone_number, " +
                "       ktxUser.number_student, ktxUser.title_major, " +
                "       ktxUser.path_avatar, ktxUser.sex, ktxUser.status_register_room " +
                "from ktx_user ktxUser " +
                "where ktxUser.number_student = :numberStudent " +
                "and ktxUser.is_actived = 1 ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("numberStudent", numberStudent);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj: result){
                StudentSearchAddNewRoomResponse response = new StudentSearchAddNewRoomResponse();
                response.setCodeUser(ValueUtil.getStringByObject(obj[0]));
                response.setUserName(ValueUtil.getStringByObject(obj[1]));
                response.setFullName(ValueUtil.getStringByObject(obj[2]));
                response.setPhoneNumber(ValueUtil.getStringByObject(obj[3]));
                response.setNumberStudent(ValueUtil.getStringByObject(obj[4]));
                response.setTitleMajor(ValueUtil.getStringByObject(obj[5]));
                response.setPathAvatar(ValueUtil.getStringByObject(obj[6]));
                response.setSex(ValueUtil.getIntegerByObject(obj[7]));
                response.setStatusRegisterRoom(ValueUtil.getIntegerByObject(obj[8]));
                return Optional.of(response);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<StudentRoom> findStudentHiringRoomByStudentId(Integer studentId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select id_student_room, " +
                "       id_user, " +
                "       id_room, " +
                "       time_created, " +
                "       time_modified, " +
                "       id_time_hired, " +
                "       id_user_created, " +
                "       id_user_modified, " +
                "       status " +
                "from student_room studentRoom " +
                "where studentRoom.id_user = :userId and studentRoom.status = 1 ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("userId", studentId);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj: result){
                StudentRoom studentRoom = new StudentRoom();
                studentRoom.setIdStudentRoom(ValueUtil.getIntegerByObject(obj[0]));
                studentRoom.setIdUser(ValueUtil.getIntegerByObject(obj[1]));
                studentRoom.setIdRoom(ValueUtil.getIntegerByObject(obj[2]));
                studentRoom.setTimeCreated(ValueUtil.getLongByObject(obj[3]));
                studentRoom.setTimeModified(ValueUtil.getLongByObject(obj[4]));
                studentRoom.setIdTimeHired(ValueUtil.getIntegerByObject(obj[5]));
                studentRoom.setIdUserCreated(ValueUtil.getIntegerByObject(obj[6]));
                studentRoom.setIdUserModified(ValueUtil.getIntegerByObject(obj[7]));
                studentRoom.setStatus(ValueUtil.getIntegerByObject(obj[8]));
                return Optional.of(studentRoom);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<StudentRoom> findStudentHiringRoomByCodeUser(String codeUser) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select studentRoom.id_student_room,    " +
                "       studentRoom.id_user,    " +
                "       studentRoom.id_room,    " +
                "       studentRoom.time_created,    " +
                "       studentRoom.time_modified,    " +
                "       studentRoom.id_time_hired,    " +
                "       studentRoom.id_user_created,    " +
                "       studentRoom.id_user_modified,    " +
                "       studentRoom.status       " +
                "       from student_room studentRoom    " +
                "       inner join ktx_user on studentRoom.id_user = ktx_user.id_ktx_user    " +
                "       where ktx_user.code_user = :codeUser and studentRoom.status = 1 ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeUser", codeUser);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj: result){
                StudentRoom studentRoom = new StudentRoom();
                studentRoom.setIdStudentRoom(ValueUtil.getIntegerByObject(obj[0]));
                studentRoom.setIdUser(ValueUtil.getIntegerByObject(obj[1]));
                studentRoom.setIdRoom(ValueUtil.getIntegerByObject(obj[2]));
                studentRoom.setTimeCreated(ValueUtil.getLongByObject(obj[3]));
                studentRoom.setTimeModified(ValueUtil.getLongByObject(obj[4]));
                studentRoom.setIdTimeHired(ValueUtil.getIntegerByObject(obj[5]));
                studentRoom.setIdUserCreated(ValueUtil.getIntegerByObject(obj[6]));
                studentRoom.setIdUserModified(ValueUtil.getIntegerByObject(obj[7]));
                studentRoom.setStatus(ValueUtil.getIntegerByObject(obj[8]));
                return Optional.of(studentRoom);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<StudentRoom> getStudentRoomIsActiveByCodeUserAndRoomIdRoom(String codeUser, Integer idRoom) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select studentRoom.id_student_room, studentRoom.id_user, studentRoom.id_room, " +
                "       studentRoom.time_created, studentRoom.time_modified, studentRoom.id_time_hired, " +
                "       studentRoom.id_user_created, studentRoom.id_user_modified, studentRoom.status " +
                "from student_room studentRoom " +
                "    inner join ktx_user ku on studentRoom.id_user = ku.id_ktx_user " +
                "    inner join room ro on studentRoom.id_room = ro.id_room " +
                "where ku.code_user = :codeUser " +
                "and ro.id_room = :idRoom " +
                "and studentRoom.status = :status ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeUser", codeUser);
        query.setParameter("idRoom", idRoom);
        query.setParameter("status", Constants.STATUS_STUDENT_HIRING_ROOM);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                return Optional.of(writeDataStudentRoom(obj));
            }
        }
        return Optional.empty();
    }

    private StudentRoom writeDataStudentRoom(Object[] obj) {
        StudentRoom studentRoom = new StudentRoom();
        studentRoom.setIdStudentRoom(ValueUtil.getIntegerByObject(obj[0]));
        studentRoom.setIdUser(ValueUtil.getIntegerByObject(obj[1]));
        studentRoom.setIdRoom(ValueUtil.getIntegerByObject(obj[2]));
        studentRoom.setTimeCreated(ValueUtil.getLongByObject(obj[3]));
        studentRoom.setTimeModified(ValueUtil.getLongByObject(obj[4]));
        studentRoom.setIdTimeHired(ValueUtil.getIntegerByObject(obj[5]));
        studentRoom.setIdUserCreated(ValueUtil.getIntegerByObject(obj[6]));
        studentRoom.setIdUserModified(ValueUtil.getIntegerByObject(obj[7]));
        studentRoom.setStatus(ValueUtil.getIntegerByObject(obj[8]));
        return studentRoom;
    }


    private long countListStudentHiredRoomResponse(ListStudentHiredRoomRequest request){
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(0) " +
                "from student_room studentRoom    " +
                "    inner join ktx_user ktxUser on studentRoom.id_user = ktxUser.id_ktx_user   " +
                "    left join ktx_user userModified on studentRoom.id_user_modified = userModified.id_ktx_user   " +
                "    inner join room ro on studentRoom.id_room = ro.id_room   " +
                "    inner join department de on ro.id_department = de.id_department   " +
                "    inner join time_hired timeHired on studentRoom.id_time_hired = timeHired.id_time_hired " +
                "    inner join batches_registration_room brr on brr.id_room = ro.id_room " +
                "    inner join batches_registration br on br.id_batches_registration = brr.id_batches_registration " +
                "    inner join semester on semester.id_semester = br.id_semester " +
                "where 1=1  ");
        setConditionListStudentHiredRoomResponse(request, sb);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParametersListStudentHiredRoomResponse(request,query);
        return ValueUtil.getLongByObject(query.getSingleResult()).longValue();
    }




    private void setParametersListStudentHiredRoomResponse(ListStudentHiredRoomRequest request, Query query) {

//        query.setParameter("listDepartmentOriginal", request.getListDepartmentOriginal());

        if (StringUtils.isNotBlank(request.getKeyword())) {
            query.setParameter("keyword", request.getKeyword());
        }
        if (StringUtils.isNotBlank(request.getCodeDepartment())) {
            query.setParameter("codeDepartment", request.getCodeDepartment());
        }
        if (StringUtils.isNotBlank(request.getCodeUser())) {
            query.setParameter("codeUser", request.getCodeUser());
        }
        if (StringUtils.isNotBlank(request.getCodeRoom())) {
            query.setParameter("codeRoom", request.getCodeRoom());
        }
        if (StringUtils.isNotBlank(request.getCodeSemester())){
            query.setParameter("codeSemester", request.getCodeSemester());
        }
        if (ObjectUtils.isNotEmpty(request.getStatus())){
            query.setParameter("statusStudentRoom", request.getStatus());
        }
        if (StringUtils.isNotBlank(request.getTimeStarted()) && StringUtils.isNotBlank(request.getTimeEnded())) {
            query.setParameter("timeStared", request.getTimeStarted());
            query.setParameter("timeEnded", request.getTimeEnded());
        }
    }

    private void setConditionListStudentHiredRoomResponse(ListStudentHiredRoomRequest request, StringBuilder sb) {
        if (StringUtils.isNotBlank(request.getKeyword())) {
            sb.append(" and ( (ktxUser.value REGEXP '[' + :keyword + ']' ) OR " +
                    "      (userModified.value REGEXP '[' + :keyword + ']' ) ");
        }
        if (StringUtils.isNotBlank(request.getCodeDepartment())) {
            sb.append(" and de.code_department = :codeDepartment ");
        }
        if (StringUtils.isNotBlank(request.getCodeUser())) {
            sb.append(" and ktxUser.code_user= :codeUser ");
        }
        if (StringUtils.isNotBlank(request.getCodeRoom())) {
            sb.append(" and ro.code_room = :codeRoom ");
        }
        if (StringUtils.isNotBlank(request.getCodeSemester())){
            sb.append(" and se.code_semester = :codeSemester ");
        }
        if (ObjectUtils.isNotEmpty(request.getStatus())){
            sb.append(" and studentRoom.status = :statusStudentRoom ");
        }
        if (StringUtils.isNotBlank(request.getTimeStarted()) && StringUtils.isNotBlank(request.getTimeEnded())) {
            sb.append(" and ( " +
                    "   (DATE_FORMAT(timeHired.time_started,'%d/%m/%Y') BETWEEN " +
                    "   DATE_FORMAT(STR_TO_DATE(:timeStared, '%d/%m/%Y'),'%d/%m/%Y') AND " +
                    "   DATE_FORMAT(STR_TO_DATE(:timeEnded,'%d/%m/%Y'),'%d/%m/%Y')) " +
                    "      OR " +
                    "   (DATE_FORMAT(timeHired.time_ended,'%d/%m/%Y') BETWEEN " +
                    "   DATE_FORMAT(STR_TO_DATE(:timeStared, '%d/%m/%Y'),'%d/%m/%Y') AND " +
                    "   DATE_FORMAT(STR_TO_DATE(:timeEnded,'%d/%m/%Y'),'%d/%m/%Y')) " +
                    "      )  ");
        }
    }

    private long countListHiredRoomStudentResponse(StudentListRoomHiredRequest request){
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(0) ct " +
                "from student_room studentRoom   " +
                "    inner join room ro on studentRoom.id_room = ro.id_room   " +
                "    inner join department de on ro.id_department = de.id_department   " +
                "    inner join ktx_user ktxUser on studentRoom.id_user = ktxUser.id_ktx_user   " +
                "    inner join time_hired timeHired on studentRoom.id_time_hired = timeHired.id_time_hired  " +
                "    inner join batches_registration_room brr on brr.id_room = ro.id_room  " +
                "    inner join batches_registration br on br.id_batches_registration = brr.id_batches_registration   " +
                "    inner join semester se on se.id_semester = br.id_semester  " +
                "where ktxUser.code_user = :codeUser  ");
        setConditionListHiredRoomStudentResponse(sb,request);
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeUser", request.getCodeUser());
        setParametersListHiredRoomStudentResponse(query,request);
        return ValueUtil.getLongByObject(query.getSingleResult()).longValue();
    }

    private void setParametersListHiredRoomStudentResponse(Query query, StudentListRoomHiredRequest request) {
        if (!Objects.isNull(request.getDepartmentId())){
            query.setParameter("departmentId", request.getDepartmentId());
        }
        if (!Objects.isNull(request.getRoomId())) {
            query.setParameter("roomId", request.getRoomId());
        }
        if (StringUtils.isNotBlank(request.getTimeStarted()) && StringUtils.isNotBlank(request.getTimeEnded())){
            query.setParameter("timeStared", request.getTimeStarted());
            query.setParameter("timeEnded", request.getTimeEnded());
        }
    }

    private void setConditionListHiredRoomStudentResponse(StringBuilder sb, StudentListRoomHiredRequest request) {
        if (!Objects.isNull(request.getDepartmentId())){
            sb.append(" and de.id_department = :departmentId ");
        }
        if (!Objects.isNull(request.getRoomId())) {
            sb.append(" and ro.id_room = :roomId ");
        }
        if (StringUtils.isNotBlank(request.getTimeStarted()) && StringUtils.isNotBlank(request.getTimeEnded())) {
            sb.append(" and ( " +
                    "      (DATE_FORMAT(timeHired.time_started,'%d/%m/%Y') BETWEEN " +
                    "       DATE_FORMAT(STR_TO_DATE(:timeStared, '%d/%m/%Y'),'%d/%m/%Y') AND " +
                    "       DATE_FORMAT(STR_TO_DATE(:timeEnded,'%d/%m/%Y'),'%d/%m/%Y')) " +
                    "      OR " +
                    "      (DATE_FORMAT(timeHired.time_ended,'%d/%m/%Y') BETWEEN " +
                    "          DATE_FORMAT(STR_TO_DATE(:timeStared, '%d/%m/%Y'),'%d/%m/%Y') AND " +
                    "          DATE_FORMAT(STR_TO_DATE(:timeEnded,'%d/%m/%Y'),'%d/%m/%Y')) " +
                    "    ) ");
        }

    }

}
