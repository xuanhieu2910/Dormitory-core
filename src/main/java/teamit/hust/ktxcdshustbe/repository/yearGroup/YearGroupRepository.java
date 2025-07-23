package teamit.hust.ktxcdshustbe.repository.yearGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamit.hust.ktxcdshustbe.entity.YearGroup;

@Repository
public interface YearGroupRepository extends JpaRepository<YearGroup,Integer>, YearGroupRepositoryCustom{

}
