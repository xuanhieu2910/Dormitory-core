package teamit.hust.ktxcdshustbe.repository.timeHired;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamit.hust.ktxcdshustbe.entity.TimeHired;

@Repository
public interface TimeHiredRepository extends JpaRepository<TimeHired, Integer>, TimeHiredRepositoryCustom {
}
