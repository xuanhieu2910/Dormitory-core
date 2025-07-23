package teamit.hust.ktxcdshustbe.repository.batchesRegistrationRoom.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.entity.BatchesRegistrationRoom;
import teamit.hust.ktxcdshustbe.repository.batchesRegistrationRoom.BatchesRegistrationRoomRepositoryCustom;
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
        query.setParameter("codeBatchesRegistration", query.getResultList());
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
