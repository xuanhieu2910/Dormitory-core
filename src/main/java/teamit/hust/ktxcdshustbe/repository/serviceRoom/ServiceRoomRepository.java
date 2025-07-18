package teamit.hust.ktxcdshustbe.repository.serviceRoom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamit.hust.ktxcdshustbe.entity.ServiceRoom;

@Repository
public interface ServiceRoomRepository extends JpaRepository<ServiceRoom,Integer>, ServiceRoomRepositoryCustom {

}
