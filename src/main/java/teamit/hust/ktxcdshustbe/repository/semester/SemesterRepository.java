package teamit.hust.ktxcdshustbe.repository.semester;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamit.hust.ktxcdshustbe.entity.Semester;

import java.util.Optional;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Integer>, SemesterRepositoryCustom {
    boolean existsByTitle(String title);

    Optional<Semester> findByCodeSemester(String codeSemester);

}
