package teamit.hust.ktxcdshustbe.repository.room.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.dto.room.FindAllRoomsDto;
import teamit.hust.ktxcdshustbe.dto.room.StudentHiredRoomDto;
import teamit.hust.ktxcdshustbe.entity.Room;
import teamit.hust.ktxcdshustbe.repository.room.RoomRepositoryCustom;
import teamit.hust.ktxcdshustbe.request.department.FindAllDepartmentRequest;
import teamit.hust.ktxcdshustbe.request.room.FindAllRoomsForRentRequest;
import teamit.hust.ktxcdshustbe.request.room.FindAllRoomsRequest;
import teamit.hust.ktxcdshustbe.request.room.StudentsHiredRoomRequest;
import teamit.hust.ktxcdshustbe.request.studentRegister.StudentRegisterRoomRequest;
import teamit.hust.ktxcdshustbe.response.room.RoomsForStudentRentResponse;
import teamit.hust.ktxcdshustbe.response.room.SearchRoomResponse;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.PageUtils;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class RoomRepositoryImpl implements RoomRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<StudentHiredRoomDto> findStudentsHiredRoom(Integer roomId,StudentsHiredRoomRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select ktxUser.id, ktxUser.full_name fullName, ktxUser.number_student numberStudent, ktxUser.phone_number, " +
                "       ktxUser.year_grade yearGrade, ktxUser.class_user classUser, timeHired.id timeHiredId, " +
                "       timeHired.time_started timeStared, timeHired.time_ended timeEnded, " +
                "       timeHired.status, se.id semesterId, se.title titleSemester " +
                "from room ro " +
                "    inner join student_room studentRoom on studentRoom.room_id = ro.id " +
                "    inner join ktx_user ktxUser on ktxUser.id = studentRoom.user_id " +
                "    inner join time_hired timeHired on studentRoom.time_id_hired = timeHired.id " +
                "    inner join semester se on timeHired.semester_id = se.id " +
                "where ro.id = :roomId " +
                "and ro.is_actived = 1 ");
        setConditionFindStudentsHiredRoom(sb,request);
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("roomId", roomId);
        setParamsFindStudentsHiredRoom(query,request);
        List<Object[]> results = query.getResultList();
        List<StudentHiredRoomDto> responses = new ArrayList<>();
        if (!CollectionUtils.isEmpty(results)) {
            for (Object[] obj: results){
                responses.add(StudentHiredRoomDto.builder()
                        .userId(ValueUtil.getIntegerByObject(obj[0]))
                        .fullName(ValueUtil.getStringByObject(obj[1]))
                        .numberStudent(ValueUtil.getStringByObject(obj[2]))
                        .phoneNumber(ValueUtil.getStringByObject(obj[3]))
                        .yearGrade(ValueUtil.getIntegerByObject(obj[4]))
                        .classUser(ValueUtil.getStringByObject(obj[5]))
                        .timeHiredId(ValueUtil.getIntegerByObject(obj[6]))
                        .timeStared(ValueUtil.getTimestampByObject(obj[7]))
                        .timeEnded(ValueUtil.getTimestampByObject(obj[8]))
                        .status(ValueUtil.getIntegerByObject(obj[9]))
                        .semesterId(ValueUtil.getIntegerByObject(obj[10]))
                        .titleSemester(ValueUtil.getStringByObject(obj[11]))
                        .build());
            }
        }
        return responses;
    }


    @Modifying
    @Transactional
    @Override
    public int updateQuantityAndRemainAmountCancelRegisterRoom(Integer idRoom, Integer quantity, Integer userIdModified){
        StringBuilder sb = new StringBuilder();
        sb.append("update room ro " +
                "set ro.remain_amount_register = ro.remain_amount_register + if (ro.quantity_registered > ro.limit_amount_people_register,0,:quantity), " +
                "    ro.quantity_registered    = ro.quantity_registered - :quantity, " +
                "    ro.time_modified          = CURRENT_TIMESTAMP(), " +
                "    ro.id_user_modified       = :userIdModified " +
                "where ro.id_room = :roomId " +
                "  and (ro.quantity_registered > 0) ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("quantity",quantity);
        query.setParameter("roomId",idRoom);
        query.setParameter("userIdModified",userIdModified);
        return query.executeUpdate();
    }

    @Modifying
    @Transactional
    @Override
    public int updateQuantityAndRemainAmountAcceptRegisterAndHiredRoom(Integer idRoom, Integer quantity, Integer userIdModified){
        StringBuilder sb = new StringBuilder();
        sb.append("update room ro  " +
                "set ro.quantity_hired               = ro.quantity_hired + :quantity,  " +
                "    ro.remain_amount                = ro.remain_amount - :quantity,  " +
                "    ro.limit_amount_people_register = ro.limit_amount_people_register - :quantity,  " +
                "    ro.quantity_registered          = ro.quantity_registered - :quantity,  " +
                "    ro.time_modified                = CURRENT_TIMESTAMP(),  " +
                "    ro.id_user_modified             = :userIdModified  " +
                "where ro.id_room = :roomId  " +
                "  and ro.remain_amount > 0  " +
                "  and (ro.quantity_hired < ro.limit_amount_people) ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("quantity", quantity);
        query.setParameter("userIdModified",userIdModified);
        query.setParameter("roomId",idRoom);
        return query.executeUpdate();
    }

    @Modifying
    @Transactional
    @Override
    public int updateQuantityAndRemainAmountToAddNewStudent(Integer idRoom, Integer userIdModified) {
        StringBuilder sb = new StringBuilder();
        sb.append("update room ro " +
                "set ro.quantity_hired               = ro.quantity_hired + :quantity, " +
                "    ro.remain_amount                = ro.remain_amount - :quantity, " +
                "    ro.limit_amount_people_register = ro.limit_amount_people_register - :quantity, " +
                "    ro.remain_amount_register       = ro.remain_amount_register - :quantity, " +
                "    ro.time_modified                = CURRENT_TIMESTAMP(), " +
                "    ro.user_id_modified             = :userIdModified " +
                "where ro.id = :roomId " +
                "  and ro.remain_amount > 0 " +
                "  and (ro.quantity_hired < ro.limit_amount_people)  ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("quantity", Constants.QUANTITY_UPDATE_HIRED_ROOM);
        query.setParameter("userIdModified",userIdModified);
        query.setParameter("roomId",idRoom);
        return query.executeUpdate();
    }

    @Transactional
    @Modifying
    @Override
    public int updateQuantityStudentRegisterRoom(String codeRoom) {
        StringBuilder sb = new StringBuilder();
        sb.append("update room ro " +
                "set ro.quantity_registered    = ro.quantity_registered + 1, " +
                "    ro.remain_amount_register = ro.remain_amount_register - 1 " +
                "where ro.code_room = :codeRoom " +
                "  and (ro.remain_amount_register > 0 and ro.remain_amount_register <= ro.limit_amount_people_register) " +
                "  and (ro.quantity_registered < ro.limit_amount_people_register) ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeRoom", codeRoom);
        return query.executeUpdate();
    }

    @Override
    public Optional<SearchRoomResponse> searchRoomToTranfer(String titleDepartment, String titleRoom, Integer sex) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select de.id departmentId, de.title titleDepartment, " +
                "       ro.id roomId, ro.title titleRoom " +
                "from room ro " +
                "    inner join department de on ro.department_id = de.id " +
                "where ro.title = :titleRoom " +
                "  and de.title = :titleDepartment " +
                "  and ro.is_actived = 1 " +
                "  and ro.remain_amount > 0 " +
                "  and ro.sex_room = :sexUser ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("titleRoom", titleRoom);
        query.setParameter("titleDepartment", titleDepartment);
        query.setParameter("sexUser", sex);
        List<Object[]> result = query.getResultList();
        SearchRoomResponse response = new SearchRoomResponse();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj: result){
                response.setDepartmentId(ValueUtil.getIntegerByObject(obj[0]));
                response.setTitleDepartment(ValueUtil.getStringByObject(obj[1]));
                response.setRoomId(ValueUtil.getIntegerByObject(obj[2]));
                response.setTitleRoom(ValueUtil.getStringByObject(obj[3]));
                return Optional.of(response);
            }
        }
        return Optional.empty();
    }


    @Transactional
    @Modifying
    @Override
    public int updateQuantityRegisterOriginRoom(Integer originRoomId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" update room ro " +
                "set ro.quantity_registered    = ro.quantity_registered - 1, " +
                "    ro.remain_amount_register = ro.remain_amount_register + 1 " +
                "where ro.id = :roomId " +
                "  and (ro.remain_amount_register < ro.limit_amount_people_register) " +
                "  and (ro.quantity_registered > 0 and ro.quantity_registered <= ro.limit_amount_people_register) ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("roomId", originRoomId);
        return query.executeUpdate();
    }

    @Override
    public int updateQuantityStudentRegisterDestinationRoom(Integer roomId) {
        StringBuilder sb = new StringBuilder();
        sb.append("update room ro " +
                "set ro.quantity_registered    = ro.quantity_registered + 1, " +
                "    ro.remain_amount_register = ro.remain_amount_register - 1 " +
                "where ro.id = :roomId " +
                "  and (ro.remain_amount_register > 0 and ro.remain_amount_register <= ro.limit_amount_people_register) " +
                "  and (ro.quantity_registered < ro.limit_amount_people_register) ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("roomId", roomId);
        return query.executeUpdate();
    }

    @Override
    public int updateRemainQuantiyRoomWhenToRemoveStudent(Integer roomId, Integer id) {
        return 0;
    }


    @Transactional
    @Modifying
    @Override
    public int updateRemainQuantityRoomWhenToRemoveStudent(String codeRoom, Integer idUserModified) {
        StringBuilder sb = new StringBuilder();
        sb.append("update room ro " +
                "set ro.quantity_hired    = ro.quantity_hired - 1, " +
                "    ro.remain_amount = ro.remain_amount + 1, " +
                "    ro.limit_amount_people_register = ro.limit_amount_people_register + 1, " +
                "    ro.remain_amount_register = ro.remain_amount_register + 1, " +
                "    ro.time_modified          = CURRENT_TIMESTAMP(), " +
                "    ro.user_id_modified       = :userIdModified " +
                "where ro.code_room = :codeRoom " +
                "  and (ro.quantity_hired > 0 and ro.remain_amount < ro.limit_amount_people) ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("userIdModified", idUserModified);
        query.setParameter("codeRoom", codeRoom);
        return query.executeUpdate();
    }

    @Override
    public Optional<Room> findByIdWithDepartment(Integer roomId) {
        return Optional.empty();
    }

    @Override
    public Optional<Room> findRoomByCodeRoom(String codeRoom) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select ro.id_room, ro.title, ro.id_department, " +
                "       ro.sex_room, ro.price, ro.time_created, " +
                "       ro.time_modified, ro.id_user_created, " +
                "       ro.id_user_modified, ro.is_active, " +
                "       ro.limit_amount_people, ro.quantity_hired, " +
                "       ro.remain_amount, ro.limit_amount_people_register, " +
                "       ro.quantity_registered, ro.remain_amount_register, " +
                "       ro.code_room " +
                "from room ro  " +
                "where ro.code_room = :codeRoom ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeRoom", codeRoom);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                return Optional.of(writeDataRoom(obj));
            }
        }
        return Optional.empty();
    }

    private Room writeDataRoom(Object[] obj) {
        Room room = new Room();
        room.setIdRoom(ValueUtil.getIntegerByObject(obj[0]));
        room.setTitle(ValueUtil.getStringByObject(obj[1]));
        room.setIdDepartment(ValueUtil.getIntegerByObject(obj[2]));
        room.setSexRoom(ValueUtil.getIntegerByObject(obj[3]));
        room.setPrice(ValueUtil.getStringByObject(obj[4]));
        room.setTimeCreated(ValueUtil.getLongByObject(obj[5]));
        room.setTimeModified(ValueUtil.getLongByObject(obj[6]));
        room.setIdUserCreated(ValueUtil.getIntegerByObject(obj[7]));
        room.setIdUserModified(ValueUtil.getIntegerByObject(obj[8]));
        room.setIsActive(ValueUtil.getIntegerByObject(obj[9]));
        room.setLimitAmountPeople(ValueUtil.getIntegerByObject(obj[10]));
        room.setQuantityHired(ValueUtil.getIntegerByObject(obj[11]));
        room.setRemainAmount(ValueUtil.getIntegerByObject(obj[12]));
        room.setLimitAmountPeopleRegister(ValueUtil.getIntegerByObject(obj[13]));
        room.setQuantityRegistered(ValueUtil.getIntegerByObject(obj[14]));
        room.setRemainAmountRegister(ValueUtil.getIntegerByObject(obj[15]));
        room.setCodeRoom(ValueUtil.getStringByObject(obj[16]));
        return room;
    }

    @Override
    public Page<FindAllRoomsDto> findAllRooms(FindAllRoomsRequest request, Pageable pageable) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select ro.id_room, ro.title, ro.id_department,  " +
                "       ro.sex_room, ro.price, ro.time_created,  " +
                "       ro.time_modified, ro.id_user_created,  " +
                "       ro.id_user_modified, ro.is_active,  " +
                "       ro.limit_amount_people, ro.quantity_hired,  " +
                "       ro.remain_amount, ro.limit_amount_people_register,  " +
                "       ro.quantity_registered, ro.remain_amount_register,  " +
                "       ro.code_room,  " +
                "       de.title, de.code_department,  " +
                "       kuCreated.user_name, kuCreated.full_name,  " +
                "       kuModified.user_name, kuModified.full_name  " +
                "from room ro  " +
                "    inner join ktx_user kuCreated on ro.id_user_created = kuCreated.id_ktx_user  " +
                "    inner join ktx_user kuModified on ro.id_user_modified = kuModified.id_ktx_user  " +
                "    inner join department de on ro.id_department = de.id_department  " +
                "where de.code_department = :codeDepartment ");
        setConditionFindAllRoom(request, sb);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllRoom(request, query);
        PageUtils.buildQuery(pageable, query);
        List<FindAllRoomsDto> findAllRoomsDtos = new ArrayList<>();
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                FindAllRoomsDto roomDto = new FindAllRoomsDto();
                roomDto.setIdRoom(ValueUtil.getIntegerByObject(obj[0]));
                roomDto.setTitle(ValueUtil.getStringByObject(obj[1]));
                roomDto.setIdDepartment(ValueUtil.getIntegerByObject(obj[2]));
                roomDto.setSexRoom(ValueUtil.getIntegerByObject(obj[3]));
                roomDto.setPrice(ValueUtil.getStringByObject(obj[4]));
                roomDto.setTimeCreated(ValueUtil.getLongByObject(obj[5]));
                roomDto.setTimeModified(ValueUtil.getLongByObject(obj[6]));
                roomDto.setIdUserCreated(ValueUtil.getIntegerByObject(obj[7]));
                roomDto.setIdUserModified(ValueUtil.getIntegerByObject(obj[8]));
                roomDto.setIsActive(ValueUtil.getIntegerByObject(obj[9]));
                roomDto.setLimitAmountPeople(ValueUtil.getIntegerByObject(obj[10]));
                roomDto.setQuantityHired(ValueUtil.getIntegerByObject(obj[11]));
                roomDto.setRemainAmount(ValueUtil.getIntegerByObject(obj[12]));
                roomDto.setLimitAmountPeopleRegister(ValueUtil.getIntegerByObject(obj[13]));
                roomDto.setQuantityRegistered(ValueUtil.getIntegerByObject(obj[14]));
                roomDto.setRemainAmountRegister(ValueUtil.getIntegerByObject(obj[15]));
                roomDto.setCodeRoom(ValueUtil.getStringByObject(obj[16]));
                roomDto.setTitleDepartment(ValueUtil.getStringByObject(obj[17]));
                roomDto.setCodeDepartment(ValueUtil.getStringByObject(obj[18]));
                roomDto.setUserNameCreated(ValueUtil.getStringByObject(obj[19]));
                roomDto.setFullNameCreated(ValueUtil.getStringByObject(obj[20]));
                roomDto.setUserNameModified(ValueUtil.getStringByObject(obj[21]));
                roomDto.setFullNameModified(ValueUtil.getStringByObject(obj[22]));
                findAllRoomsDtos.add(roomDto);
            }
        }
        return new PageImpl<>(findAllRoomsDtos, pageable, countFindAllRooms(request));
    }

    @Override
    public Optional<Room> findRoomByTitleRoom(String titleRoom) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select ro.id_room, ro.title, ro.id_department, " +
                "       ro.sex_room, ro.price, ro.time_created, " +
                "       ro.time_modified, ro.id_user_created, " +
                "       ro.id_user_modified, ro.is_active, " +
                "       ro.limit_amount_people, ro.quantity_hired, " +
                "       ro.remain_amount, ro.limit_amount_people_register, " +
                "       ro.quantity_registered, ro.remain_amount_register, " +
                "       ro.code_room " +
                "from room ro  " +
                "where ro.title = :titleRoom ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("titleRoom", titleRoom);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                return Optional.of(writeDataRoom(obj));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Room> findRoomByTitleRoomAndCodeDepartment(String title, String codeDepartment) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select ro.id_room, ro.title, ro.id_department, ro.sex_room,  " +
                "       ro.price, ro.time_created, ro.time_modified,  " +
                "       ro.id_user_created, ro.id_user_modified, ro.is_active,  " +
                "       ro.limit_amount_people, ro.quantity_hired, ro.remain_amount,  " +
                "       ro.limit_amount_people_register, ro.quantity_registered,  " +
                "       ro.remain_amount_register, ro.code_room  " +
                "from room ro  " +
                "    inner join department de on ro.id_department = de.id_department  " +
                "where ro.title = :titleRoom  " +
                "and de.code_department = :codeDepartment ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("titleRoom", title);
        query.setParameter("codeDepartment", codeDepartment);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                return Optional.of(writeDataRoom(obj));
            }
        }
        return Optional.empty();
    }

    private void setParameterFindAllRoom(FindAllRoomsRequest request, Query query) {
        query.setParameter("codeDepartment", request.getCodeDepartment());
        if (StringUtils.isNotBlank(request.getTitleRoom())) {
            query.setParameter("titleRoom", request.getTitleRoom());
        }
        if (ObjectUtils.isNotEmpty(request.getStatus())){
            query.setParameter("active", request.getStatus());
        }
        if (ObjectUtils.isNotEmpty(request.getSex())){
            query.setParameter("sexRoom", request.getSex());
        }
    }

    private void setConditionFindAllRoom(FindAllRoomsRequest request, StringBuilder sb) {
        if (StringUtils.isNotBlank(request.getTitleRoom())) {
            sb.append(" and ro.title REGEXP :titleRoom ");
        }
        if (ObjectUtils.isNotEmpty(request.getStatus())){
            sb.append(" and ro.is_active = :active ");
        }
        if (ObjectUtils.isNotEmpty(request.getSex())){
            sb.append(" and ro.sex_room = :sexRoom ");
        }
        if (ObjectUtils.isNotEmpty(request.getStatusRemain())){
            if (request.getStatusRemain().equals(Constants.STATUS_REMAIN_UN_EMPTY)) {
                sb.append(" and ro.remain_amount = 0 ");
            } else {
                sb.append(" and ro.remain_amount > 0 ");
            }
        }
        sb.append(" order by ro.id_room desc ");
    }

    @Override
    public Page<FindAllRoomsDto> findAllRoomsForRent(FindAllRoomsForRentRequest request, Pageable pageable) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select ro.id_room, ro.title, ro.id_department,   " +
                "       ro.sex_room, ro.price, ro.time_created,   " +
                "       ro.time_modified, ro.id_user_created,    " +
                "       ro.id_user_modified, ro.is_active,   " +
                "       ro.limit_amount_people, ro.quantity_hired,   " +
                "       ro.remain_amount, ro.limit_amount_people_register,   " +
                "       ro.quantity_registered, ro.remain_amount_register,   " +
                "       ro.code_room,   " +
                "       de.code_department, de.title,    " +
                "       kuCreated.user_name, kuCreated.full_name,   " +
                "       kuModified.user_name, kuModified.full_name   " +
                "from room ro   " +
                "        inner join department de on ro.id_department = de.id_department   " +
                "        inner join ktx_user kuCreated on ro.id_user_created = kuCreated.id_ktx_user   " +
                "        inner join ktx_user kuModified on ro.id_user_modified = kuModified.id_ktx_user   " +
                "where de.code_department = :codeDepartment   " +
                "and ro.remain_amount_register > 0 and ro.is_active = :status ");
        setConditionFindAllRoomsForRent(sb, request);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParamsRoomsForStudentRent(query, request);
        PageUtils.buildQuery(pageable, query);
        List<Object[]> result = query.getResultList();
        List<FindAllRoomsDto> findAllRoomsDtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj: result){
                FindAllRoomsDto dto = new FindAllRoomsDto();
                dto.setIdRoom(ValueUtil.getIntegerByObject(obj[0]));
                dto.setTitle(ValueUtil.getStringByObject(obj[1]));
                dto.setIdDepartment(ValueUtil.getIntegerByObject(obj[2]));
                dto.setSexRoom(ValueUtil.getIntegerByObject(obj[3]));
                dto.setPrice(ValueUtil.getStringByObject(obj[4]));
                dto.setTimeCreated(ValueUtil.getLongByObject(obj[5]));
                dto.setTimeModified(ValueUtil.getLongByObject(obj[6]));
                dto.setIdUserCreated(ValueUtil.getIntegerByObject(obj[7]));
                dto.setIdUserModified(ValueUtil.getIntegerByObject(obj[8]));
                dto.setIsActive(ValueUtil.getIntegerByObject(obj[9]));
                dto.setLimitAmountPeople(ValueUtil.getIntegerByObject(obj[10]));
                dto.setQuantityHired(ValueUtil.getIntegerByObject(obj[11]));
                dto.setRemainAmount(ValueUtil.getIntegerByObject(obj[12]));
                dto.setLimitAmountPeopleRegister(ValueUtil.getIntegerByObject(obj[13]));
                dto.setQuantityRegistered(ValueUtil.getIntegerByObject(obj[14]));
                dto.setRemainAmountRegister(ValueUtil.getIntegerByObject(obj[15]));
                dto.setCodeRoom(ValueUtil.getStringByObject(obj[16]));
                dto.setCodeDepartment(ValueUtil.getStringByObject(obj[17]));
                dto.setTitleDepartment(ValueUtil.getStringByObject(obj[18]));
                dto.setUserNameCreated(ValueUtil.getStringByObject(obj[19]));
                dto.setFullNameCreated(ValueUtil.getStringByObject(obj[20]));
                dto.setUserNameModified(ValueUtil.getStringByObject(obj[21]));
                dto.setFullNameModified(ValueUtil.getStringByObject(obj[22]));
                findAllRoomsDtos.add(dto);
            }
        }
        return new PageImpl<>(findAllRoomsDtos, pageable, countRoomsForStudentRent(request));
    }


    private long countRoomsForStudentRent(FindAllRoomsForRentRequest request){
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(0) " +
                "from room ro " +
                "        inner join department de on ro.id_department = de.id_department " +
                "        inner join ktx_user kuCreated on ro.id_user_created = kuCreated.id_ktx_user " +
                "        inner join ktx_user kuModified on ro.id_user_modified = kuModified.id_ktx_user " +
                "where de.code_department = :codeDepartment " +
                "and ro.remain_amount_register > 0 and ro.is_active = :status ");
        setConditionFindAllRoomsForRent(sb, request);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParamsRoomsForStudentRent(query, request);
        return  ValueUtil.getLongByObject(query.getSingleResult());
    }

    private void setParamsRoomsForStudentRent(Query query, FindAllRoomsForRentRequest request) {
        query.setParameter("codeDepartment", request.getCodeDepartment());
        query.setParameter("status", Constants.STATUS_ROOM_IS_ACTIVED);
        if (StringUtils.isNotBlank(request.getTitleRoom())){
            query.setParameter("titleRoom", request.getTitleRoom());
        }
        if (ObjectUtils.isNotEmpty(request.getGender())) {
            query.setParameter("gender", request.getGender());
        }
    }

    private void setConditionFindAllRoomsForRent(StringBuilder sb, FindAllRoomsForRentRequest request) {
        if (StringUtils.isNotBlank(request.getTitleRoom())) {
            sb.append(" and ro.title REGEXP :titleRoom ");
        }
        if (ObjectUtils.isNotEmpty(request.getGender())) {
            sb.append(" and ro.sex_room = :gender ");
        }
        sb.append(" order by ro.id_room desc  ");
    }

    private void setParamsFindStudentsHiredRoom(Query query, StudentsHiredRoomRequest request) {
        if (StringUtils.isNotBlank(request.getKeyword())){
            query.setParameter("keyword", request.getKeyword());
        }
    }

    private void setConditionFindStudentsHiredRoom(StringBuilder sb, StudentsHiredRoomRequest request) {
        if (StringUtils.isNotBlank(request.getKeyword())) {
            sb.append(" and ( ( ktxUser.full_name REGEXP '[' + :keyword + ']' ) OR " +
                    "                ( ktxUser.number_student REGEXP '[' + :keyword + ']' ) OR " +
                    "                ( ktxUser.phone_number REGEXP '[' + :keyword + ']' ) ) ");
        }
    }

    private Long countFindAllRooms(FindAllRoomsRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(0)  " +
                "from room ro  " +
                "    inner join ktx_user kuCreated on ro.id_user_created = kuCreated.id_ktx_user  " +
                "    inner join ktx_user kuModified on ro.id_user_modified = kuModified.id_ktx_user  " +
                "    inner join department de on ro.id_department = de.id_department  " +
                "where de.code_department = :codeDepartment ");
        setConditionFindAllRoom(request, sb);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllRoom(request, query);
        return  ValueUtil.getLongByObject(query.getSingleResult());
    }
}
