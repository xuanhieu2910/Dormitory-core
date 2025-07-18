package teamit.hust.ktxcdshustbe.repository.userRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamit.hust.ktxcdshustbe.entity.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer>, UserRoleRepositoryCustom {
}
