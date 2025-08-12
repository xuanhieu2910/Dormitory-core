package teamit.hust.ktxcdshustbe.repository.batchesRegistrationRoom.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.dto.batchesRegistrationRoom.FindAllBatchesRegistrationRoomDto;
import teamit.hust.ktxcdshustbe.entity.BatchesRegistrationRoom;
import teamit.hust.ktxcdshustbe.repository.batchesRegistrationRoom.BatchesRegistrationRoomRepositoryCustom;
import teamit.hust.ktxcdshustbe.request.batchesRegistrationRoom.FindAllBatchesRegistrationRoomRequest;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.PageUtils;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BatchesRegistrationRoomRepositoryImpl implements BatchesRegistrationRoomRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Optional<List<BatchesRegistrationRoom>>
    findAllBatchesRegistrationRoomByCodeBatchesRegistrationCustom(String codeBatchesRegistration) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select brr.id_batches_registration_room, brr.id_batches_registration,  " +
                "       brr.id_room, brr.status, brr.time_created,  " +
                "       brr.time_modified, brr.id_user_created, brr.id_user_modified  " +
                "from batches_registration_room brr  " +
                "    inner join batches_registration br on brr.id_batches_registration = br.id_batches_registration    " +
                "    inner join room ro on brr.id_room  = ro.id_room  " +
                "where br.code_batches_registration = :codeBatchesRegistration   ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeBatchesRegistration", codeBatchesRegistration);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            List<BatchesRegistrationRoom> batchesRegistrationRooms = new ArrayList<>();
            for (Object[] obj : result){
                batchesRegistrationRooms.add(writeDataBatchesRegistrationRoom(obj));
            }
            return Optional.of(batchesRegistrationRooms);
        }

        return Optional.empty();
    }

    @Override
    public Page<FindAllBatchesRegistrationRoomDto> findAllBatchesRegistrationRoom(Pageable pageable,
                                                                                  FindAllBatchesRegistrationRoomRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select ro.id_room, ro.title, ro.id_department, ro.sex_room,  " +
                "       ro.price, ro.time_created, ro.time_modified, ro.id_user_created,  " +
                "       ro.id_user_modified, ro.is_active, ro.limit_amount_people,   " +
                "       ro.quantity_hired, ro.remain_amount, ro.limit_amount_people_register,  " +
                "       ro.quantity_registered, ro.remain_amount_register, ro.code_room  " +
                "from batches_registration br  " +
                "    inner join batches_registration_room brr on br.id_batches_registration = brr.id_batches_registration  " +
                "    inner join room ro on brr.id_room = ro.id_room  " +
                "    inner join department de on ro.id_department = de.id_department  " +
                "where br.code_batches_registration = :codeBatchesRegistration  " +
                "and de.code_department = :codeDepartment ");
        setConditionFindAllBatchesRegisterRoom(sb, request);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllBatchesRegisterRoom(query, request);
        PageUtils.buildQuery(pageable, query);
        List<Object[]> result = query.getResultList();
        List<FindAllBatchesRegistrationRoomDto> registrationRoomDtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                registrationRoomDtos.add(writeDataFindAllBatchesRegisterRoom(obj));
            }
        }
        return new PageImpl<>(registrationRoomDtos, pageable, countFindAllBatchesRegisterRoom(request));
    }

    private long countFindAllBatchesRegisterRoom(FindAllBatchesRegistrationRoomRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(ro.id_room)   " +
                "from batches_registration br   " +
                "    inner join batches_registration_room brr on br.id_batches_registration = brr.id_batches_registration   " +
                "    inner join room ro on brr.id_room = ro.id_room   " +
                "    inner join department de on ro.id_department = de.id_department   " +
                "where br.code_batches_registration = :codeBatchesRegistration   " +
                "and de.code_department = :codeDepartment ");
        setConditionFindAllBatchesRegisterRoom(sb, request);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllBatchesRegisterRoom(query, request);
        return ValueUtil.getLongByObject(query.getFirstResult());
    }

    private FindAllBatchesRegistrationRoomDto writeDataFindAllBatchesRegisterRoom(Object[] obj) {
        FindAllBatchesRegistrationRoomDto batchesRegistrationRoomDto = new FindAllBatchesRegistrationRoomDto();
        batchesRegistrationRoomDto.setIdRoom(ValueUtil.getIntegerByObject(obj[0]));
        batchesRegistrationRoomDto.setTitle(ValueUtil.getStringByObject(obj[1]));
        batchesRegistrationRoomDto.setIdDepartment(ValueUtil.getIntegerByObject(obj[2]));
        batchesRegistrationRoomDto.setSexRoom(ValueUtil.getIntegerByObject(obj[3]));
        batchesRegistrationRoomDto.setPrice(ValueUtil.getStringByObject(obj[4]));
        batchesRegistrationRoomDto.setTimeCreated(ValueUtil.getLongByObject(obj[5]));
        batchesRegistrationRoomDto.setTimeModified(ValueUtil.getLongByObject(obj[6]));
        batchesRegistrationRoomDto.setIdUserCreated(ValueUtil.getIntegerByObject(obj[7]));
        batchesRegistrationRoomDto.setIdUserModified(ValueUtil.getIntegerByObject(obj[8]));
        batchesRegistrationRoomDto.setIsActive(ValueUtil.getIntegerByObject(obj[9]));
        batchesRegistrationRoomDto.setLimitAmountPeople(ValueUtil.getIntegerByObject(obj[10]));
        batchesRegistrationRoomDto.setQuantityHired(ValueUtil.getIntegerByObject(obj[11]));
        batchesRegistrationRoomDto.setRemainAmount(ValueUtil.getIntegerByObject(obj[12]));
        batchesRegistrationRoomDto.setLimitAmountPeopleRegister(ValueUtil.getIntegerByObject(obj[13]));
        batchesRegistrationRoomDto.setQuantityRegistered(ValueUtil.getIntegerByObject(obj[14]));
        batchesRegistrationRoomDto.setRemainAmountRegister(ValueUtil.getIntegerByObject(obj[15]));
        batchesRegistrationRoomDto.setCodeRoom(ValueUtil.getStringByObject(obj[16]));
        return batchesRegistrationRoomDto;
    }

    private void setParameterFindAllBatchesRegisterRoom(Query query, FindAllBatchesRegistrationRoomRequest request) {
        query.setParameter("codeBatchesRegistration", request.getCodeBatchesRegisterRoom());
        query.setParameter("codeDepartment", request.getCodeDepartment());
        if (StringUtils.isNotEmpty(request.getTitleRoom())){
            query.setParameter("titleRoom", request.getTitleRoom());
        }
    }

    private void setConditionFindAllBatchesRegisterRoom(StringBuilder sb,
                                                        FindAllBatchesRegistrationRoomRequest request) {
        if (StringUtils.isNotEmpty(request.getTitleRoom())){
            sb.append(" and ro.title REGEXP :titleRoom ");
        }
        if (ObjectUtils.isNotEmpty(request.getStatus())){
            sb.append(" and ro.is_active = :status ");
        }
        if (ObjectUtils.isNotEmpty(request.getSex())){
            sb.append(" and ro.sex_room = :sex ");
        }
        if (ObjectUtils.isNotEmpty(request.getStatusRemain())){
            if (request.getStatusRemain().equals(Constants.STATUS_REMAIN_UN_EMPTY)) {
                sb.append(" and ro.remain_amount = 0 ");
            } else {
                sb.append(" and ro.remain_amount > 0 ");
            }
        }
        sb.append(" order by ro.id_room desc  ");
    }

    private BatchesRegistrationRoom writeDataBatchesRegistrationRoom(Object[] obj) {
        BatchesRegistrationRoom registrationRoom = new BatchesRegistrationRoom();
        registrationRoom.setIdBatchesRegistrationRoom(ValueUtil.getIntegerByObject(obj[0]));
        registrationRoom.setIdBatchesRegistration(ValueUtil.getIntegerByObject(obj[1]));
        registrationRoom.setIdRoom(ValueUtil.getIntegerByObject(obj[2]));
        registrationRoom.setStatus(ValueUtil.getIntegerByObject(obj[3]));
        registrationRoom.setTimeCreated(ValueUtil.getLongByObject(obj[4]));
        registrationRoom.setTimeModified(ValueUtil.getLongByObject(obj[5]));
        registrationRoom.setIdUserCreated(ValueUtil.getIntegerByObject(obj[6]));
        registrationRoom.setIdUserModified(ValueUtil.getIntegerByObject(obj[7]));
        return registrationRoom;
    }
}
