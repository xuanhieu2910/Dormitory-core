package teamit.hust.ktxcdshustbe.service.semester;

import org.springframework.data.domain.Page;
import teamit.hust.ktxcdshustbe.entity.Semester;
import teamit.hust.ktxcdshustbe.request.semester.CreateSemesterRequest;
import teamit.hust.ktxcdshustbe.request.semester.FindAllSemesterRequest;
import teamit.hust.ktxcdshustbe.request.semester.UpdateSemesterRequest;
import teamit.hust.ktxcdshustbe.response.semester.FindAllSemesterResponse;

public interface SemesterService {

    Page<FindAllSemesterResponse> findAllSemester(FindAllSemesterRequest request);
    Semester create(CreateSemesterRequest request);
    Semester update(UpdateSemesterRequest request);
    void delete(String codeSemester);
}
