package teamit.hust.ktxcdshustbe.repository.batchesRegistration;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamit.hust.ktxcdshustbe.dto.batchesRegistration.BatchesRegistrationDetailDto;
import teamit.hust.ktxcdshustbe.dto.batchesRegistration.FindAllBatchesRegistrationDto;
import teamit.hust.ktxcdshustbe.entity.BatchesRegistration;
import teamit.hust.ktxcdshustbe.request.batchesRegistration.FindAllBatchesRegistrationRequest;

import java.util.List;
import java.util.Optional;

public interface BatchesRegistrationRepositoryCustom {
    Page<FindAllBatchesRegistrationDto> findAllBatchesRegistration(FindAllBatchesRegistrationRequest request, Pageable pageable);

    Optional<BatchesRegistrationDetailDto> getDetailBatchesRegistration(String codeBatchesRegistration);
    boolean checkNotExitsBatchesRegistration (List<String> codeYearGroups, Long startDate, Long endDate);
    Optional<BatchesRegistration> findBatchesRegistrationByCodeBatchesRegistration(String codeBatchesRegistration);
}
