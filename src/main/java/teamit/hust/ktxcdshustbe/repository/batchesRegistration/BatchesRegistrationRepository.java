package teamit.hust.ktxcdshustbe.repository.batchesRegistration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import teamit.hust.ktxcdshustbe.entity.BatchesRegistration;

@Repository
public interface BatchesRegistrationRepository extends JpaRepository<BatchesRegistration, Integer>, BatchesRegistrationRepositoryCustom {
    void deleteByIdSemester(int idSemester);

    @Modifying
    @Transactional
    @Query("update BatchesRegistration b set b.idSemester = null where b.idSemester = :id")
    void deleteIdSemester(@Param("id") int idSemester);
}
