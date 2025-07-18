package teamit.hust.ktxcdshustbe.repository.roleAllowAssign;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamit.hust.ktxcdshustbe.entity.RoleAllowAssign;

@Repository
public interface RoleAllowAssignRepository extends JpaRepository<RoleAllowAssign, Integer>, RoleAllowAssignRepositoryCustom {
}
