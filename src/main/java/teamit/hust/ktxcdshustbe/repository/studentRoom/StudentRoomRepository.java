package teamit.hust.ktxcdshustbe.repository.studentRoom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamit.hust.ktxcdshustbe.entity.StudentRoom;

@Repository
public interface StudentRoomRepository extends JpaRepository<StudentRoom, Integer>, StudentRoomRepositoryCustom {
}
