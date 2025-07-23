package teamit.hust.ktxcdshustbe.repository.batchesYearGroupRegistration;


import teamit.hust.ktxcdshustbe.entity.BatchesYearGroupRegistration;

import java.util.List;
import java.util.Optional;

public interface BatchesYearGroupRegistrationRepositoryCustom {


    Optional<List<BatchesYearGroupRegistration>> findAllBatchesYearGroupByCodeBatchesRegistration(String codeBatchesRegistration);
}
