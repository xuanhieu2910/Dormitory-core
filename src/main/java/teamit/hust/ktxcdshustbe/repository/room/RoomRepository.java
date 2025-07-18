package teamit.hust.ktxcdshustbe.repository.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import teamit.hust.ktxcdshustbe.entity.Room;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;
@Repository
public interface RoomRepository extends JpaRepository<Room, Integer>, RoomRepositoryCustom {
}
