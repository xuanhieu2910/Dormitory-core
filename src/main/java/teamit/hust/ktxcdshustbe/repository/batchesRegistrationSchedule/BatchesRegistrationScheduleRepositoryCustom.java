package teamit.hust.ktxcdshustbe.repository.batchesRegistrationSchedule;

import teamit.hust.ktxcdshustbe.entity.BatchesRegistrationSchedule;

import java.util.List;
import java.util.Optional;

public interface BatchesRegistrationScheduleRepositoryCustom {
    Optional<List<BatchesRegistrationSchedule>> findAllBatchesRegistrationScheduleByCodeBatchesRegistration(String codeBatchesRegistration);
}
