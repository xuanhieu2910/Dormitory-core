package teamit.hust.ktxcdshustbe.repository.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamit.hust.ktxcdshustbe.entity.Orders;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer>, OrdersRepositoryCustom {
}
