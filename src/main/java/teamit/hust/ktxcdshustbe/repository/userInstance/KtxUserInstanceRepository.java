package teamit.hust.ktxcdshustbe.repository.userInstance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamit.hust.ktxcdshustbe.entity.KtxUserInstance;

@Repository
public interface KtxUserInstanceRepository extends JpaRepository<KtxUserInstance, Integer>,KtxUserInstanceRepositoryCustom {
}
