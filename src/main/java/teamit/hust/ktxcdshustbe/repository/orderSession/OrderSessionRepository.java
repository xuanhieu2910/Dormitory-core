package teamit.hust.ktxcdshustbe.repository.orderSession;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamit.hust.ktxcdshustbe.entity.OrderSessions;

@Repository
public interface OrderSessionRepository extends JpaRepository<OrderSessions, Integer>,
        OrderSessionRepositoryCustom {



}
