package teamit.hust.ktxcdshustbe.repository.semester;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamit.hust.ktxcdshustbe.dto.semester.FindAllSemesterDto;
import teamit.hust.ktxcdshustbe.dto.semester.FindSemesterDetailDto;
import teamit.hust.ktxcdshustbe.entity.Semester;
import teamit.hust.ktxcdshustbe.request.semester.FindAllSemesterRequest;
import teamit.hust.ktxcdshustbe.response.semester.DetailSemesterResponse;
import teamit.hust.ktxcdshustbe.response.semester.FindAllSemesterResponse;

import java.util.List;
import java.util.Optional;

public interface SemesterRepositoryCustom {
    Page<FindAllSemesterDto> findAllSemester(FindAllSemesterRequest request, Pageable pageable);

    //Optional<FindAllSemesterResponse> findSemesterDetailsByCodeSemester(String codeSemester);

    FindSemesterDetailDto findSemesterDetailByCode(String codeSemester);
}
