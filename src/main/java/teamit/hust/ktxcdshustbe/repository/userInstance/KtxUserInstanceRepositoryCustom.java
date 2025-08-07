package teamit.hust.ktxcdshustbe.repository.userInstance;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamit.hust.ktxcdshustbe.entity.KtxUserInstance;
import teamit.hust.ktxcdshustbe.request.userInstance.FindAllUserInstanceRequest;
import teamit.hust.ktxcdshustbe.response.userInstance.StatisticsUserInstanceResponse;

import java.util.List;

public interface KtxUserInstanceRepositoryCustom {
    Page<KtxUserInstance> findAllUserInstance(FindAllUserInstanceRequest request, Pageable pageable);

    List<KtxUserInstance> findAllUserInstanceByIds(List<Integer> ids);

    StatisticsUserInstanceResponse getStatisticUserInstance();
}
