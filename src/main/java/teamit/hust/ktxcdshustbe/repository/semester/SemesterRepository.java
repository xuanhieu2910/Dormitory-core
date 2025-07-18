package teamit.hust.ktxcdshustbe.repository.semester;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamit.hust.ktxcdshustbe.entity.Semester;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Integer>, SemesterRepositoryCustom {
}
