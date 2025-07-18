package teamit.hust.ktxcdshustbe.repository.department;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamit.hust.ktxcdshustbe.entity.Department;


@Repository
public interface DepartmentRepository extends JpaRepository<Department,Integer>, DepartmentRepositoryCustom {
}
