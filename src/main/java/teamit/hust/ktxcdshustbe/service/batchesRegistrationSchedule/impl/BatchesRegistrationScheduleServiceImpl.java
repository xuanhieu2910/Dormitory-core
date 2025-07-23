package teamit.hust.ktxcdshustbe.service.batchesRegistrationSchedule.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import teamit.hust.ktxcdshustbe.entity.BatchesRegistrationSchedule;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.repository.batchesRegistration.BatchesRegistrationRepository;
import teamit.hust.ktxcdshustbe.repository.batchesRegistrationSchedule.BatchesRegistrationScheduleRepository;
import teamit.hust.ktxcdshustbe.service.batchesRegistrationSchedule.BatchesRegistrationScheduleService;

import java.util.List;
import java.util.Optional;

@Service
public class BatchesRegistrationScheduleServiceImpl implements BatchesRegistrationScheduleService {

    @Autowired
    BatchesRegistrationScheduleRepository batchesRegistrationScheduleRepository;

    @Override
    public List<BatchesRegistrationSchedule> saveAll(List<BatchesRegistrationSchedule> registrationSchedules) {
        return batchesRegistrationScheduleRepository.saveAll(registrationSchedules);
    }

    @Override
    public List<BatchesRegistrationSchedule> findAllBatchesRegistrationScheduleByCodeBatchesRegistration(String codeBatchesRegistration) {
        Optional<List<BatchesRegistrationSchedule>> batchesRegistrationSchedules =
                batchesRegistrationScheduleRepository.findAllBatchesRegistrationScheduleByCodeBatchesRegistration(codeBatchesRegistration);
        if (batchesRegistrationSchedules.isEmpty()){
            throw new NotFoundException();
        }
        return batchesRegistrationSchedules.get();
    }

    @Override
    public void deleteAll(List<BatchesRegistrationSchedule> batchesRegistrationScheduleDelete) {
        batchesRegistrationScheduleRepository.deleteAll(batchesRegistrationScheduleDelete);
    }
}
