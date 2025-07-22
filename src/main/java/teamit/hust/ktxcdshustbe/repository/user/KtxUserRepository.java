package teamit.hust.ktxcdshustbe.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import teamit.hust.ktxcdshustbe.entity.KtxUser;

@Repository
public interface KtxUserRepository extends JpaRepository<KtxUser, Integer>, KtxUserRepositoryCustom {

    @Modifying
    @Transactional
    @Query("update KtxUser k set k.idPriorityGroup = null where k.idPriorityGroup = :id")
    void deleteIdPriorityGroup(@Param("id") int idPriorityGroup);
}
