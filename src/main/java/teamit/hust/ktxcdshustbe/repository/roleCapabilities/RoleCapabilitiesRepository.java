package teamit.hust.ktxcdshustbe.repository.roleCapabilities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamit.hust.ktxcdshustbe.entity.RoleCapabilities;

@Repository
public interface RoleCapabilitiesRepository extends JpaRepository<RoleCapabilities, Integer>, RoleCapabilitiesRepositoryCustom {
}
