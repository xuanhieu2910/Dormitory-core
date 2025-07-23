package teamit.hust.ktxcdshustbe.service.batchesYearGroupRegistration.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import teamit.hust.ktxcdshustbe.entity.BatchesYearGroupRegistration;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.repository.batchesYearGroupRegistration.BatchesYearGroupRegistrationRepository;
import teamit.hust.ktxcdshustbe.service.batchesYearGroupRegistration.BatchesYearGroupRegistrationService;

import java.util.List;
import java.util.Optional;

@Service
public class BatchesYearGroupRegistrationServiceImpl implements BatchesYearGroupRegistrationService {

    @Autowired
    BatchesYearGroupRegistrationRepository batchesYearGroupRegistrationRepository;

    @Override
    public BatchesYearGroupRegistration save(BatchesYearGroupRegistration batchesYearGroupRegistration) {
        return batchesYearGroupRegistrationRepository.save(batchesYearGroupRegistration);
    }

    @Override
    public List<BatchesYearGroupRegistration> saveAll(List<BatchesYearGroupRegistration> batchesYearGroupRegistrations) {
        return batchesYearGroupRegistrationRepository.saveAll(batchesYearGroupRegistrations);
    }

    @Override
    public void deleteAll(List<BatchesYearGroupRegistration> batchesYearGroupRegistrations) {
        batchesYearGroupRegistrationRepository.deleteAll(batchesYearGroupRegistrations);
    }

    @Override
    public List<BatchesYearGroupRegistration> findAllBatchesYearGroupByCodeBatchesRegistration(String codeBatchesRegistration) {
        Optional<List<BatchesYearGroupRegistration>> yearGroupRegistrations =
                batchesYearGroupRegistrationRepository.findAllBatchesYearGroupByCodeBatchesRegistration(codeBatchesRegistration);
        if (yearGroupRegistrations.isEmpty()){
            throw new NotFoundException();
        }
        return yearGroupRegistrations.get();
    }
}
