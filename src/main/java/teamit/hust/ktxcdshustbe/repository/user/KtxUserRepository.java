package teamit.hust.ktxcdshustbe.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamit.hust.ktxcdshustbe.entity.KtxUser;

@Repository
public interface KtxUserRepository extends JpaRepository<KtxUser, Integer>, KtxUserRepositoryCustom {
}
