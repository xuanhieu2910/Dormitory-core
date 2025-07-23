package teamit.hust.ktxcdshustbe.service.batchesRegistrationSchedule;

import teamit.hust.ktxcdshustbe.entity.BatchesRegistrationSchedule;

import java.util.List;

public interface BatchesRegistrationScheduleService {

    List<BatchesRegistrationSchedule> saveAll(List<BatchesRegistrationSchedule> registrationSchedules);

    List<BatchesRegistrationSchedule> findAllBatchesRegistrationScheduleByCodeBatchesRegistration(String codeBatchesRegistration);

    void deleteAll(List<BatchesRegistrationSchedule> batchesRegistrationScheduleDelete);
}
