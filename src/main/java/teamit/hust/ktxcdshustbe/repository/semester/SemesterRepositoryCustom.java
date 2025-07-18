package teamit.hust.ktxcdshustbe.repository.semester;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamit.hust.ktxcdshustbe.dto.semester.FindAllSemesterDto;
import teamit.hust.ktxcdshustbe.entity.Semester;
import teamit.hust.ktxcdshustbe.request.semester.FindAllSemesterRequest;

import java.util.List;

public interface SemesterRepositoryCustom {
    Page<FindAllSemesterDto> findAllSemester(FindAllSemesterRequest request, Pageable pageable);
}
