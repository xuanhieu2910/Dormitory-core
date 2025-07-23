package teamit.hust.ktxcdshustbe.service.batchesYearGroupRegistration;

import teamit.hust.ktxcdshustbe.entity.BatchesYearGroupRegistration;

import java.util.List;

public interface BatchesYearGroupRegistrationService {

    BatchesYearGroupRegistration save(BatchesYearGroupRegistration batchesYearGroupRegistration);
    List<BatchesYearGroupRegistration> saveAll(List<BatchesYearGroupRegistration> batchesYearGroupRegistrations);
    void deleteAll(List<BatchesYearGroupRegistration> batchesYearGroupRegistrations);

    List<BatchesYearGroupRegistration> findAllBatchesYearGroupByCodeBatchesRegistration(String codeBatchesRegistration);
}
