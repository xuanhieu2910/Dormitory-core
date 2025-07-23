package teamit.hust.ktxcdshustbe.repository.batchesRegistrationSchedule.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.entity.BatchesRegistrationSchedule;
import teamit.hust.ktxcdshustbe.repository.batchesRegistrationSchedule.BatchesRegistrationScheduleRepositoryCustom;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BatchesRegistrationScheduleRepositoryImpl implements BatchesRegistrationScheduleRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public Optional<List<BatchesRegistrationSchedule>> findAllBatchesRegistrationScheduleByCodeBatchesRegistration(String codeBatchesRegistration) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select brs.id_batches_registration_schedule, brs.id_batches_registration, brs.id_priority_group,  " +
                "       brs.status, brs.time_created, brs.time_modified, brs.id_user_created,  " +
                "       brs.id_user_modified, brs.registration_start_time, brs.registration_end_time  " +
                "from batches_registration_schedule brs  " +
                "    inner join batches_registration br on brs.id_batches_registration = br.id_batches_registration  " +
                "    inner join priority_group pg on brs.id_priority_group = pg.id_priority_group  " +
                "where br.code_batches_registration = :codeBatchesRegistration ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeBatchesRegistration", codeBatchesRegistration);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            List<BatchesRegistrationSchedule> registrationSchedules = new ArrayList<>();
            for (Object[] obj : result){
                registrationSchedules.add(writeDataRegistrationSchedule(obj));
            }
            return Optional.of(registrationSchedules);
        }
        return Optional.empty();
    }

    private BatchesRegistrationSchedule writeDataRegistrationSchedule(Object[] obj) {
        BatchesRegistrationSchedule batchesRegistrationSchedule = new BatchesRegistrationSchedule();
        batchesRegistrationSchedule.setIdBatchesRegistrationSchedule(ValueUtil.getIntegerByObject(obj[0]));
        batchesRegistrationSchedule.setIdBatchesRegistration(ValueUtil.getIntegerByObject(obj[1]));
        batchesRegistrationSchedule.setIdPriorityGroup(ValueUtil.getIntegerByObject(obj[2]));
        batchesRegistrationSchedule.setStatus(ValueUtil.getIntegerByObject(obj[3]));
        batchesRegistrationSchedule.setTimeCreated(ValueUtil.getLongByObject(obj[4]));
        batchesRegistrationSchedule.setTimeModified(ValueUtil.getLongByObject(obj[5]));
        batchesRegistrationSchedule.setIdUserCreated(ValueUtil.getIntegerByObject(obj[6]));
        batchesRegistrationSchedule.setIdUserModified(ValueUtil.getIntegerByObject(obj[7]));
        batchesRegistrationSchedule.setRegistrationStartTime(ValueUtil.getLongByObject(obj[8]));
        batchesRegistrationSchedule.setRegistrationEndTime(ValueUtil.getLongByObject(obj[9]));
        return batchesRegistrationSchedule;
    }
}
