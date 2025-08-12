package teamit.hust.ktxcdshustbe.repository.batchesYearGroupRegistration.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.entity.BatchesYearGroupRegistration;
import teamit.hust.ktxcdshustbe.repository.batchesYearGroupRegistration.BatchesYearGroupRegistrationRepositoryCustom;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BatchesYearGroupRegistrationRepositoryImpl implements BatchesYearGroupRegistrationRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public Optional<List<BatchesYearGroupRegistration>> findAllBatchesYearGroupByCodeBatchesRegistration(String codeBatchesRegistration) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select bygr.id_batches_year_group_registration, bygr.id_batches_registration,  " +
                "       bygr.id_year_group, bygr.status, bygr.time_created, bygr.time_modified,  " +
                "       bygr.id_user_created, bygr.id_user_modified  " +
                "from batches_year_group_registration bygr  " +
                "    inner join batches_registration br on bygr.id_batches_registration = br.id_batches_registration  " +
                "    inner join year_group yg on bygr.id_year_group = yg.id_year_group  " +
                "where br.code_batches_registration = :codeBatchesRegistration ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeBatchesRegistration", codeBatchesRegistration);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            List<BatchesYearGroupRegistration> yearGroupRegistrations = new ArrayList<>();
            for (Object[] obj : result){
                yearGroupRegistrations.add(writeDataBatchesYearGroupRegistration(obj));
                return Optional.of(yearGroupRegistrations);
            }
        }
        return Optional.empty();
    }

    private BatchesYearGroupRegistration writeDataBatchesYearGroupRegistration(Object[] obj) {
        BatchesYearGroupRegistration batchesYearGroupRegistration = new BatchesYearGroupRegistration();
        batchesYearGroupRegistration.setIdBatchesYearGroupRegistration(ValueUtil.getIntegerByObject(obj[0]));
        batchesYearGroupRegistration.setIdBatchesRegistration(ValueUtil.getIntegerByObject(obj[1]));
        batchesYearGroupRegistration.setIdYearGroup(ValueUtil.getIntegerByObject(obj[2]));
        batchesYearGroupRegistration.setStatus(ValueUtil.getIntegerByObject(obj[3]));
        batchesYearGroupRegistration.setTimeCreated(ValueUtil.getLongByObject(obj[4]));
        batchesYearGroupRegistration.setTimeModified(ValueUtil.getLongByObject(obj[5]));
        batchesYearGroupRegistration.setIdUserCreated(ValueUtil.getIntegerByObject(obj[6]));
        batchesYearGroupRegistration.setIdUserModified(ValueUtil.getIntegerByObject(obj[7]));
        return batchesYearGroupRegistration;
    }
}
