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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.dto.room.FindAllRoomsDto;
import teamit.hust.ktxcdshustbe.dto.room.SearchInformationRegisterRoomDto;
import teamit.hust.ktxcdshustbe.dto.room.StudentSearchRoomDto;
import teamit.hust.ktxcdshustbe.entity.KtxUser;
import teamit.hust.ktxcdshustbe.entity.Room;
import teamit.hust.ktxcdshustbe.repository.room.RoomRepositoryCustom;
import teamit.hust.ktxcdshustbe.request.room.*;
import teamit.hust.ktxcdshustbe.response.room.SearchRoomResponse;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.PageUtils;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.*;

public class RoomRepositoryImpl implements RoomRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;


    @Modifying
    @Transactional
    @Override
    public int updateQuantityAndRemainAmountCancelRegisterRoom(Integer idRoom, Integer quantity, Integer userIdModified){
        StringBuilder sb = new StringBuilder();
        sb.append("update room ro " +
                "set ro.remain_amount_register = ro.remain_amount_register + if (ro.quantity_registered > ro.limit_amount_people_register,0,:quantity), " +
                "    ro.quantity_registered    = ro.quantity_registered - :quantity, " +
                "    ro.time_modified          = :timeModified, " +
                "    ro.id_user_modified       = :userIdModified " +
                "where ro.id_room = :roomId " +
                "  and (ro.quantity_registered > 0) ");
        Query query = entityManager.createNativeQuery(sb.toString());
        Long timeCurrent = new Date().getTime();
        query.setParameter("timeModified",timeCurrent);
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
                "    ro.remain_amount_register          = ro.remain_amount_register - :quantity,  " +
                "    ro.time_modified                = :timeModified,  " +
                "    ro.id_user_modified             = :userIdModified  " +
                "where ro.id_room = :roomId  " +
                "  and ro.remain_amount > 0  " +
                "  and (ro.quantity_hired < ro.limit_amount_people) ");
        Query query = entityManager.createNativeQuery(sb.toString());
        Long timeCurrent = new Date().getTime();
        query.setParameter("timeModified",timeCurrent);
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
                "    ro.time_modified                = :timeModified, " +
                "    ro.id_user_modified             = :userIdModified " +
                "where ro.id_room = :roomId " +
                "  and ro.remain_amount > 0 " +
                "  and (ro.quantity_hired < ro.limit_amount_people)  ");
        Query query = entityManager.createNativeQuery(sb.toString());
        Long timeCurrent = new Date().getTime();
        query.setParameter("timeModified",timeCurrent);
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

    //    @Override
//    public Optional<SearchRoomResponse> searchRoomToTranfer(String titleDepartment, String titleRoom, Integer sex) {
//        StringBuilder sb = new StringBuilder();
//        sb.append(" select de.id departmentId, de.title titleDepartment, " +
//                "       ro.id roomId, ro.title titleRoom " +
//                "from room ro " +
//                "    inner join department de on ro.department_id = de.id " +
//                "where ro.title = :titleRoom " +
//                "  and de.title = :titleDepartment " +
//                "  and ro.is_actived = 1 " +
//                "  and ro.remain_amount > 0 " +
//                "  and ro.sex_room = :sexUser ");
//        Query query = entityManager.createNativeQuery(sb.toString());
//        query.setParameter("titleRoom", titleRoom);
//        query.setParameter("titleDepartment", titleDepartment);
//        query.setParameter("sexUser", sex);
//        List<Object[]> result = query.getResultList();
//        SearchRoomResponse response = new SearchRoomResponse();
//        if (!CollectionUtils.isEmpty(result)) {
//            for (Object[] obj: result){
//                response.setDepartmentId(ValueUtil.getIntegerByObject(obj[0]));
//                response.setTitleDepartment(ValueUtil.getStringByObject(obj[1]));
//                response.setRoomId(ValueUtil.getIntegerByObject(obj[2]));
//                response.setTitleRoom(ValueUtil.getStringByObject(obj[3]));
//                return Optional.of(response);
//            }
//        }
//        return Optional.empty();
//    }

    @Override
    public Optional<SearchRoomResponse> searchRoomToTranfer(String titleDepartment, String titleRoom, Integer sex) {
        StringBuilder sb = new StringBuilder();
        sb.append("""
                select d.code_department, d.title, r.code_room, r.title,
                    r.sex_room, r.price, r.limit_amount_people, r.quantity_hired,
                    r.remain_amount, r.is_active
                from room r
                    inner join department d on r.id_department = d.id_department
                where 
                    d.title = :titleDepartment and
                    r.title = :titleRoom and
                    r.is_active = 1 and
                    r.remain_amount > 0 and
                    r.sex_room = :sex
                """);
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("titleDeparment", titleDepartment);
        query.setParameter("titleRoom", titleRoom);
        query.setParameter("sex", sex);
        List<Object[]> result = query.getResultList();
        SearchRoomResponse response = new SearchRoomResponse();
        if(!CollectionUtils.isEmpty(result)){
            for(Object[] obj : result){
                response.setCodeDepartment(ValueUtil.getStringByObject(obj[0]));
                response.setTitleDepartment(ValueUtil.getStringByObject(obj[1]));
                response.setCodeRoom(ValueUtil.getStringByObject(obj[2]));
                response.setTitleRoom(ValueUtil.getStringByObject(obj[3]));
                response.setSex(ValueUtil.getIntegerByObject(obj[4]));
                response.setPrice(ValueUtil.getStringByObject(obj[5]));
                response.setCapacity(ValueUtil.getIntegerByObject(obj[6]));
                response.setQuantity(ValueUtil.getIntegerByObject(obj[7]));
                response.setRemainQuantity(ValueUtil.getIntegerByObject(obj[8]));
                response.setStatus(ValueUtil.getStringByObject(obj[9]));
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
                "where ro.id_room = :roomId " +
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
                "where ro.id_room = :roomId " +
                "  and (ro.remain_amount_register > 0 and ro.remain_amount_register <= ro.limit_amount_people_register) " +
                "  and (ro.quantity_registered < ro.limit_amount_people_register) ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("roomId", roomId);
        return query.executeUpdate();
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
                "    ro.time_modified          = :timeModified, " +
                "    ro.id_user_modified       = :userIdModified " +
                "where ro.code_room = :codeRoom " +
                "  and (ro.quantity_hired > 0 and ro.remain_amount < ro.limit_amount_people) ");
        Query query = entityManager.createNativeQuery(sb.toString());
        Long timeCurrent = new Date().getTime();
        query.setParameter("timeModified",timeCurrent);
        query.setParameter("userIdModified", idUserModified);
        query.setParameter("codeRoom", codeRoom);
        return query.executeUpdate();
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
                "       kuCreated.user_name,  " +
                "       kuCreated.value,  " +
                "       kuModified.user_name, " +
                "       kuModified.value " +
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
//                roomDto.setValueCreated(ValueUtil.getStringByObject(obj[20]));
                roomDto.setUserNameModified(ValueUtil.getStringByObject(obj[20]));
//                roomDto.setValueModified(ValueUtil.getStringByObject(obj[22]));
                findAllRoomsDtos.add(roomDto);
            }
        }
        return new PageImpl<>(findAllRoomsDtos, pageable, countFindAllRooms(request));
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

    @Override
    public Optional<Room> findRoomByIdRoom(Integer idRoom) {
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
                "where ro.id_room = :idRoom ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("idRoom", idRoom);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                return Optional.of(writeDataRoom(obj));
            }
        }
        return Optional.empty();
    }

    @Override
    public Page<StudentSearchRoomDto> findAllRoomStudentSearch(StudentSearchRoomRequest request, Pageable pageable) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select de.id_department, de.code_department, de.title,   " +
                "       ro.id_room, ro.code_room, ro.title,   " +
                "       ro.price, ro.limit_amount_people_register,   " +
                "       ro.remain_amount_register, ro.sex_room   " +
                "from batches_registration br   " +
                "    inner join batches_registration_room brr on br.id_batches_registration = brr.id_batches_registration   " +
                "    inner join batches_registration_schedule brs on br.id_batches_registration = brs.id_batches_registration   " +
                "    inner join priority_group pg on brs.id_priority_group = pg.id_priority_group   " +
                "    inner join batches_year_group_registration bygr on br.id_batches_registration = bygr.id_batches_registration   " +
                "    inner join year_group yg on bygr.id_year_group = yg.id_year_group   " +
                "    inner join room ro on brr.id_room = ro.id_room   " +
                "    inner join department de on ro.id_department = de.id_department   " +
                "where :currentTime  between brs.registration_start_time and brs.registration_end_time  " +
                "and pg.id_priority_group = :idPriorityGroup   " +
                "and yg.id_year_group = :idYearGroup   " +
                "and de.code_department = :codeDepartment   " +
                "and ro.sex_room = :sexRoom ");
        setConditionFindAllRoomSearchStudentRegister(sb, request);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllRoomSearchStudent(query, request);
        PageUtils.buildQuery(pageable, query);
        List<Object[]> result = query.getResultList();
        List<StudentSearchRoomDto> searchRoomRegisterRoomDtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                StudentSearchRoomDto searchRoomRegisterRoomDto = new StudentSearchRoomDto();
                searchRoomRegisterRoomDto.setIdDepartment(ValueUtil.getIntegerByObject(obj[0]));
                searchRoomRegisterRoomDto.setCodeDepartment(ValueUtil.getStringByObject(obj[1]));
                searchRoomRegisterRoomDto.setTitleDepartment(ValueUtil.getStringByObject(obj[2]));
                searchRoomRegisterRoomDto.setIdRoom(ValueUtil.getIntegerByObject(obj[3]));
                searchRoomRegisterRoomDto.setCodeRoom(ValueUtil.getStringByObject(obj[4]));
                searchRoomRegisterRoomDto.setTitleRoom(ValueUtil.getStringByObject(obj[5]));
                searchRoomRegisterRoomDto.setPrice(ValueUtil.getStringByObject(obj[6]));
                searchRoomRegisterRoomDto.setLimitationAmountRegisterRoom(ValueUtil.getIntegerByObject(obj[7]));
                searchRoomRegisterRoomDto.setRemainAmountRegisterRoom(ValueUtil.getIntegerByObject(obj[8]));
                searchRoomRegisterRoomDto.setSexRoom(ValueUtil.getIntegerByObject(obj[9]));
                searchRoomRegisterRoomDtos.add(searchRoomRegisterRoomDto);
            }
        }
        return new PageImpl<>(searchRoomRegisterRoomDtos, pageable, countFindAllSearchRoomRegisterRoom(request));
    }

    private long countFindAllSearchRoomRegisterRoom(StudentSearchRoomRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(0)   " +
                "from batches_registration br   " +
                "    inner join batches_registration_room brr on br.id_batches_registration = brr.id_batches_registration   " +
                "    inner join batches_registration_schedule brs on br.id_batches_registration = brs.id_batches_registration   " +
                "    inner join priority_group pg on brs.id_priority_group = pg.id_priority_group   " +
                "    inner join batches_year_group_registration bygr on br.id_batches_registration = bygr.id_batches_registration   " +
                "    inner join year_group yg on bygr.id_year_group = yg.id_year_group   " +
                "    inner join room ro on brr.id_room = ro.id_room   " +
                "    inner join department de on ro.id_department = de.id_department   " +
                "where :currentTime  between brs.registration_start_time and brs.registration_end_time   " +
                "and pg.id_priority_group = :idPriorityGroup   " +
                "and yg.id_year_group = :idYearGroup   " +
                "and de.code_department = :codeDepartment   " +
                "and ro.sex_room = :sexRoom ");
        setConditionFindAllRoomSearchStudentRegister(sb, request);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllRoomSearchStudent(query, request);
        return ValueUtil.getIntegerByObject(query.getSingleResult());
    }

    private void setParameterFindAllRoomSearchStudent(Query query, StudentSearchRoomRequest request) {
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        query.setParameter("currentTime", new Date().getTime());
        query.setParameter("idPriorityGroup",ktxUser.getIdPriorityGroup());
        query.setParameter("idYearGroup", ktxUser.getIdYearGroup());
        query.setParameter("codeDepartment", request.getCodeDepartment());
        query.setParameter("sexRoom", ktxUser.getSex());
        if (StringUtils.isNotBlank(request.getTitleRoom())){
            query.setParameter("titleRoom", request.getTitleRoom());
        }
    }

    private void setConditionFindAllRoomSearchStudentRegister(StringBuilder sb, StudentSearchRoomRequest request) {
        if (StringUtils.isNotBlank(request.getTitleRoom())){
            sb.append(" and ro.title REGEXP :titleRoom ");
        }
        sb.append(" order by ro.id_room desc  ");
    }

    @Override
    public Page<SearchInformationRegisterRoomDto> findInformationRegisterRoom(SearchInformationRegisterRoomRequest request, Pageable pageable){
        StringBuilder sb = new StringBuilder();
        sb.append("""
                select ku.code_user, ku.user_name,
                       d.code_department, r.title,
                       br.start_time, se.title
                from room r
                         inner join department d on d.id_department = r.id_department
                         inner join student_room sr on r.id_room = sr.id_room
                         inner join ktx_user ku on ku.id_ktx_user = sr.id_user
                         inner join time_hired ti on sr.id_time_hired = ti.id_time_hired
                         inner join batches_registration br on ti.id_time_hired = br.id_time_hired
                         inner join semester se on br.id_semester = se.id_semester
                where d.code_department = :codeDepartment and
                    r.title = :titleRoom and
                    se.title = :titleSemester
                """);
        setConditionFindInformationResgisterRoom(request, sb);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindInformationResgisterRoom(request, query);
        PageUtils.buildQuery(pageable, query);
        List<SearchInformationRegisterRoomDto> searchInformationRegisterRoomDtos = new ArrayList<>();
        List<Object[]> results = query.getResultList();
        if (!CollectionUtils.isEmpty(results)) {
            for (Object[] obj : results) {
                SearchInformationRegisterRoomDto dto = new SearchInformationRegisterRoomDto();
                dto.setCodeUser(ValueUtil.getIntegerByObject(obj[0]));
                dto.setUserName(ValueUtil.getStringByObject(obj[1]));
                dto.setCodeDepartment(ValueUtil.getStringByObject(obj[2]));
                dto.setTitleRoom(ValueUtil.getStringByObject(obj[3]));
                dto.setTimeStarted(ValueUtil.getTimestampByObject(obj[4]));
                dto.setTitleSemester(ValueUtil.getStringByObject(obj[5]));
                searchInformationRegisterRoomDtos.add(dto);
            }
        }
        return new PageImpl<>(searchInformationRegisterRoomDtos, pageable, countFindInformationRegisterRoom(request));
    }

    private Long countFindInformationRegisterRoom(SearchInformationRegisterRoomRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("""
                select count(0)
                from room r
                         inner join department d on d.id_department = r.id_department
                         inner join student_room sr on r.id_room = sr.id_room
                         inner join ktx_user ku on ku.id_ktx_user = sr.id_user
                         inner join time_hired ti on sr.id_time_hired = ti.id_time_hired
                         inner join batches_registration br on ti.id_time_hired = br.id_time_hired
                         inner join semester se on br.id_semester = se.id_semester
                where d.code_department = :codeDepartment and
                    r.title = :titleRoom and
                    se.title = :titleSemester
                """);
        setConditionFindInformationResgisterRoom(request, sb);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindInformationResgisterRoom(request, query);
        return ValueUtil.getLongByObject(query.getSingleResult());
    }
    private void setParameterFindInformationResgisterRoom(SearchInformationRegisterRoomRequest request, Query query) {
        if (StringUtils.isNotBlank(request.getCodeDepartment())) {
            query.setParameter("codeDepartment", request.getCodeDepartment());
        }
        if (StringUtils.isNotBlank(request.getTitleRoom())){
            query.setParameter("titleRoom", request.getTitleRoom());
        }
        if (StringUtils.isNotBlank(request.getTitleSemester())) {
            query.setParameter("titleSemester", request.getTitleSemester());
        }
    }
    private void setConditionFindInformationResgisterRoom(SearchInformationRegisterRoomRequest request, StringBuilder sb) {
        if (StringUtils.isNotBlank(request.getTitleRoom())){
            sb.append(" and r.title REGEXP :titleRoom ");
        }
        if (StringUtils.isNotBlank(request.getTitleSemester())){
            sb.append(" and se.title REGEXP :titleSemester ");
        }
        if (StringUtils.isNotBlank(request.getCodeDepartment())){
            sb.append(" and d.code_department = :codeDepartment ");
        }
        sb.append(" order by se.title desc");
    }

    @Override
    public Optional<List<Room>> findAllRoomByListCodeRoom(List<String> codesRoom) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select id_room, title, id_department,   " +
                "       sex_room, price, time_created,   " +
                "       time_modified, id_user_created,   " +
                "       id_user_modified, is_active,   " +
                "       limit_amount_people, quantity_hired,   " +
                "       remain_amount, limit_amount_people_register,   " +
                "       quantity_registered, remain_amount_register,   " +
                "       code_room   " +
                "from room   " +
                "where code_room in (:codesRoom) ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codesRoom", codesRoom);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            List<Room> rooms = new ArrayList<>();
            for (Object[] obj : result){
                rooms.add(writeDataRoom(obj));
            }
            return Optional.of(rooms);
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<Room>> findAllRoomByIdsRoom(List<Integer> idsRoom) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select id_room, title, id_department,   " +
                "       sex_room, price, time_created,   " +
                "       time_modified, id_user_created,   " +
                "       id_user_modified, is_active,   " +
                "       limit_amount_people, quantity_hired,   " +
                "       remain_amount, limit_amount_people_register,   " +
                "       quantity_registered, remain_amount_register,   " +
                "       code_room   " +
                "from room   " +
                "where id_room in (:idsRoom) ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("idsRoom", idsRoom);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            List<Room> rooms = new ArrayList<>();
            for (Object[] obj : result){
                rooms.add(writeDataRoom(obj));
            }
            return Optional.of(rooms);
        }
        return Optional.empty();
    }

    @Transactional
    @Modifying
    @Override
    public int updateRemainQuantityRoomWhenStudentRegisterHoldingRoomByIdRoom(Integer idRoom) {
        StringBuilder sb = new StringBuilder();
        sb.append(" update room set remain_amount_register = remain_amount_register - :quantity,  " +
                "                quantity_registered = quantity_registered + :quantity,  " +
                "                time_modified = :timeModified  " +
                "where id_room = :idRoom  " +
                "and remain_amount_register > 0  " +
                "and quantity_registered < limit_amount_people_register ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("idRoom", idRoom);
        query.setParameter("quantity", Constants.QUANTITY_UPDATE_HIRED_ROOM);
        query.setParameter("timeModified", new Date().getTime());
        return query.executeUpdate();
    }

    @Transactional
    @Modifying
    @Override
    public int updateRemainQuantityRegisterRoomWhenStudentChangeRoom(Integer idRoom) {
        StringBuilder sb = new StringBuilder();
        sb.append(" update room   " +
                "set remain_amount_register = remain_amount_register + :quantity,   " +
                "    quantity_registered = quantity_registered - :quantity,   " +
                "     time_modified = :timeModified   " +
                "where id_room = :idRoom   " +
                "and remain_amount_register >= 0   " +
                "and quantity_registered <= limit_amount_people_register  ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("idRoom", idRoom);
        query.setParameter("quantity", Constants.QUANTITY_UPDATE_HIRED_ROOM);
        query.setParameter("timeModified", new Date().getTime());
        return query.executeUpdate();
    }

    @Override
    public Page<FindAllRoomsDto> findAllRoomsRegister(FindAllRoomsRequest request, Pageable pageable) {
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
                "       kuCreated.user_name,  " +
                "       kuCreated.value,  " +
                "       kuModified.user_name, " +
                "       kuModified.value " +
                "from room ro  " +
                "    inner join ktx_user kuCreated on ro.id_user_created = kuCreated.id_ktx_user  " +
                "    inner join ktx_user kuModified on ro.id_user_modified = kuModified.id_ktx_user  " +
                "    inner join department de on ro.id_department = de.id_department " +
                "    inner join  batches_registration_room brr on brr.id_room = ro.id_room " +
                "where de.code_department = :codeDepartment and brr.status = :statusBatchesRegistration ");
        setConditionFindAllRoom(request, sb);
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("statusBatchesRegistration",Constants.STATUS_BATCHES_REGISTRATION_ROOM_ACTIVE);
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
//                roomDto.setValueCreated(ValueUtil.getStringByObject(obj[20]));
                roomDto.setUserNameModified(ValueUtil.getStringByObject(obj[20]));
//                roomDto.setValueModified(ValueUtil.getStringByObject(obj[22]));
                findAllRoomsDtos.add(roomDto);
            }
        }
        return new PageImpl<>(findAllRoomsDtos, pageable, countFindAllRoomsRegister(request));
    }

    @Override
    public List<FindAllRoomsDto> findAllListRoomByCodeDepartment(String codeDepartment) {
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
                "       kuCreated.user_name,  " +
                "       kuModified.user_name, " +
                "       brr.status" +
                "from room ro  " +
                "    inner join ktx_user kuCreated on ro.id_user_created = kuCreated.id_ktx_user  " +
                "    inner join ktx_user kuModified on ro.id_user_modified = kuModified.id_ktx_user  " +
                "    inner join department de on ro.id_department = de.id_department " +
                "    inner join  batches_registration_room brr on brr.id_room = ro.id_room " +
                "where de.code_department = :codeDepartment ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeDepartment",codeDepartment);
        List<Object[]> result = query.getResultList();
        List<FindAllRoomsDto> findAllRoomsDtos = new ArrayList<>();
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
                roomDto.setUserNameModified(ValueUtil.getStringByObject(obj[20]));
                roomDto.setStatusRegister(ValueUtil.getIntegerByObject(obj[21]));
                findAllRoomsDtos.add(roomDto);
            }
        }

        return findAllRoomsDtos;
    }

    private long countFindAllRoomsRegister(FindAllRoomsRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(0)  " +
                "from room ro  " +
                "    inner join ktx_user kuCreated on ro.id_user_created = kuCreated.id_ktx_user  " +
                "    inner join ktx_user kuModified on ro.id_user_modified = kuModified.id_ktx_user  " +
                "    inner join department de on ro.id_department = de.id_department  " +
                "    inner join  batches_registration_room brr on brr.id_room = ro.id_room " +
                "where de.code_department = :codeDepartment and brr.status = :statusBatchesRegistration ");
        setConditionFindAllRoom(request, sb);
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("statusBatchesRegistration",Constants.STATUS_BATCHES_REGISTRATION_ROOM_ACTIVE);
        setParameterFindAllRoom(request, query);
        return  ValueUtil.getLongByObject(query.getSingleResult());
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
                "       kuCreated.user_name, kuCreated.value,   " +
                "       kuModified.user_name, kuModified.value   " +
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
                dto.setValueCreated(ValueUtil.getStringByObject(obj[20]));
                dto.setUserNameModified(ValueUtil.getStringByObject(obj[21]));
                dto.setValueModified(ValueUtil.getStringByObject(obj[22]));
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
