package teamit.hust.ktxcdshustbe.service.batchesRegistration;

import org.springframework.data.domain.Page;
import teamit.hust.ktxcdshustbe.entity.BatchesRegistration;
import teamit.hust.ktxcdshustbe.request.batchesRegistration.CreateBatchesRegistrationRequest;
import teamit.hust.ktxcdshustbe.request.batchesRegistration.FindAllBatchesRegistrationRequest;
import teamit.hust.ktxcdshustbe.request.batchesRegistration.UpdateBatchesRegistrationRequest;
import teamit.hust.ktxcdshustbe.response.batchesRegistration.BatchesRegistrationDetailResponse;
import teamit.hust.ktxcdshustbe.response.batchesRegistration.FindAllBatchesRegistrationResponse;

public interface BatchesRegistrationService {
    Page<FindAllBatchesRegistrationResponse> findAll(FindAllBatchesRegistrationRequest request);

    BatchesRegistrationDetailResponse getDetailBatchesRegistration(String codeBatchesRegistration);

    void createBatchesRegistration(CreateBatchesRegistrationRequest request);

    void updateBatchesRegistration(UpdateBatchesRegistrationRequest request);

    BatchesRegistration getBatchesRegistrationCurrentByIdYearGroupAndIdPriorityGroup(Long timeCurrent, Integer idYearGroup,
                                                                                     Integer idPriorityGroup);
}
