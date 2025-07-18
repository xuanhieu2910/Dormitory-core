package teamit.hust.ktxcdshustbe.repository.studentRegisterRoom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamit.hust.ktxcdshustbe.entity.StudentRegisterRoom;


@Repository
public interface StudentRegisterRoomRepository extends JpaRepository<StudentRegisterRoom, Integer>, StudentRegisterRoomRepositoryCustom {
}
