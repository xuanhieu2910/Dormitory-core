package teamit.hust.ktxcdshustbe.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamit.hust.ktxcdshustbe.entity.KtxUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface KtxUserRepository extends JpaRepository<KtxUser, Integer>, KtxUserRepositoryCustom {

    @Modifying
    @Transactional
    @Query("update KtxUser k set k.idPriorityGroup = null where k.idPriorityGroup = :id")
    void deleteIdPriorityGroup(@Param("id") int idPriorityGroup);

}
