package teamit.hust.ktxcdshustbe.repository.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamit.hust.ktxcdshustbe.entity.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer>, ServiceRepositoryCustom {
}
